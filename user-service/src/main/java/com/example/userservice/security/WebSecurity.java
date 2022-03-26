package com.example.userservice.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration // Spring의 Bean으로 등록되기 위함 + 다른 Bean보다 우선순위 먼저 등록된다.
@EnableWebSecurity // Web Security 용도로 사용
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests().antMatchers("/users/**").permitAll(); // /users/** 요청은 모두 허락
    
        http.headers().frameOptions().disable(); // h2 page의 프레임별 옵션을 무시
    }
}
