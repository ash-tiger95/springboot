package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.entity.UserEntity;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.vo.ResponseOrder;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    BCryptPasswordEncoder bCryptPasswordEncoder;

    Environment environment;
    RestTemplate restTemplate;

    /* 생성자가 스프링 컨텍스트로 인해 만들어지면서 자동으로 우리가 만든 빈을 주입(메모리 등록)하고 사용한다. */
    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, Environment environment, RestTemplate restTemplate) { // Main에서 인스턴스를 생성(Bean)해주어야 한다.
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.environment = environment;
        this.restTemplate = restTemplate;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT); // DATA 모양이 딱 맞아떨어져야함

        UserEntity userEntity = mapper.map(userDto, UserEntity.class); // userDto를 UserEntity 모양으로 변환해주세요
        userEntity.setEncryptedPwd(bCryptPasswordEncoder.encode(userDto.getPwd())); // 같은 값이여도 암호화하면 다르다.

        userRepository.save(userEntity);

        UserDto returnUserDto = mapper.map(userEntity, UserDto.class);

        return returnUserDto;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null) {
            throw new UsernameNotFoundException("User Not found"); // 이거는 바꾸자 (얘는 보통 로그인 안될 때 사용되는 시큐리티 함수)
        }

        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

//        List<ResponseOrder> orders = new ArrayList<>();

        /* Using RestTemplate */
        String orderUrl = "http://127.0.0.1:8000/order-service/%s/orders";
        ResponseEntity<List<ResponseOrder>> orderListResponse = restTemplate.exchange(
                orderUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ResponseOrder>>() {
                });

        List<ResponseOrder> orderList = orderListResponse.getBody(); // list타입으로 변환

        userDto.setOrders(orderList);

        return userDto;
    }

    @Override
    public Iterable<UserEntity> getUserByAll() {
        return userRepository.findAll();
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null) {
            throw new UsernameNotFoundException(email);
        }

        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
        return userDto;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username);

        if (userEntity == null) {
            throw new UsernameNotFoundException(username);
        }

        return new User(userEntity.getEmail(), userEntity.getEncryptedPwd(),
                true, true, true, true,
                new ArrayList<>()); // 로그인되었을 때, 권한을 추가하는 작업
    }
}
