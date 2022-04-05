package com.example.userservice.security;

import com.example.userservice.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration // Spring의 Bean으로 등록되기 위함 + 다른 Bean보다 우선순위 먼저 등록된다.
@EnableWebSecurity // Web Security 용도로 사용
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private UserService userService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private Environment environment;

    // @Configuration에서는 @Autowired를 하지 않아도 Autowired 이전에 필요한 상태가 준비된다.
    public WebSecurity(Environment environment, UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.environment = environment; // 토근 관련 정보를 불러올 수 있다.
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception { // 권한관련 configure
        http.csrf().disable();
//        http.authorizeRequests().antMatchers("/users/**").permitAll(); // /users/** 요청은 모두 허락

        // Actuator 요청은 필터를 적용하지 않는(권한 없이) 시나리오, (이와 관련된 정보는 application.yml)
        http.authorizeRequests().antMatchers("/actuator/**").permitAll();

        // 요청정보에 대해 getAuthenticationFilter()를 적용해야 한다.
        http.authorizeRequests().antMatchers("/**")
                .hasIpAddress("172.23.0.1") // IP 제한
                .and()
                .addFilter(getAuthenticationFilter()); // 인증이 된(필터를 통과한) 상태에서만 요청 허락
    
        http.headers().frameOptions().disable(); // h2 page의 프레임별 옵션을 무시
    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception{
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager(), userService, environment); // 인증처리를 위한 메니저는 시큐리티에서 가져온 authenticationManager
//        authenticationFilter.setAuthenticationManager(authenticationManager()); // 생성자를 통해 호출하니깐 호출할 필요가 없다.

        return authenticationFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception { // 인증관련 configure
        auth.userDetailsService(userService) // select pwd from user
                .passwordEncoder(bCryptPasswordEncoder); // 사용자가 입력한 email, pwd(패스워드를 암호화한다)로 로그인 처리
    }

}
