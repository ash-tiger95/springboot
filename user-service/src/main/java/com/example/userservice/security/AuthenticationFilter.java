package com.example.userservice.security;

import com.example.userservice.dto.UserDto;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.RequestLogin;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter { // Bean으로 등록된 상태가 아니라 WebSecurity에서 인스턴스를 생성해서 사용하고 있다.

    private UserService userService;
    private Environment env;

    // WebSecurity를 통해 가지고 있던 빈의 정보를 AuthenticationFilter에서 사용한다.
    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                UserService userService,
                                Environment env) {
        super.setAuthenticationManager(authenticationManager);
        this.userService = userService;
        this.env = env;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException { // 로그인 요청 발생 시 로직
        try {
            RequestLogin creds = new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class); // 사용자 입력

            // Authentication Manager에 인증 작업 요청
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(creds.getEmail(), // 사용자가 입력한 email, password를 스프링 시큐리티에서 쓸 수 있는 형태로 변경, ArrayList: 권한관련을 위한 인자
                            creds.getPassword(),
                            new ArrayList<>()
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException { // 로그인 성공시 처리할 로직(인증 성공 시 사용자에게 토큰 발행하는 로직)
        // 로그인 성공 시
        String userName = ((User)authResult.getPrincipal()).getUsername();

        // UserId를 가지고 토큰을 만들기 때문에 DB에서 불러와야한다.
        UserDto userDetails = userService.getUserDetailsByEmail(userName);

        String token = Jwts.builder()
                .setSubject(userDetails.getUserId())
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(env.getProperty("token.expiration_time")))) // 만기일 하루
                .signWith(SignatureAlgorithm.HS256, env.getProperty("token.secret")) // 암호알고리즘
                .compact(); // 토큰 생성
        response.addHeader("token", token);
        response.addHeader("userId",userDetails.getUserId());

    }
}
