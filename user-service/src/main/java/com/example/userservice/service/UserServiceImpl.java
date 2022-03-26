package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.entity.UserEntity;
import com.example.userservice.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    BCryptPasswordEncoder bCryptPasswordEncoder;

    /* 생성자가 스프링 컨텍스트로 인해 만들어지면서 자동으로 우리가 만든 빈을 주입(메모리 등록)하고 사용한다. */
    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) { // Main에서 인스턴스를 생성(Bean)해주어야 한다.
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
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

}
