package com.example.firstservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

// 1-1. 우리가 만든 주소: http:localhost:8081/welcome
// 1-2. apigateway로 전달받은 요청: http://localhost:8081/first-service/**
@RestController
@RequestMapping("/first-service") // 1-3. path에 맞게 들어올 수 있게 RequestMapping 설정
@Slf4j // 2-1. Lombok 로그 사용하기 위함
public class FirstServiceController {

    Environment env;

    @Autowired
    public FirstServiceController(Environment env) {
        this.env = env;
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to the First Service";
    }

    // [API Gateway Service] - Filter 적용
    @GetMapping("/message")
    public String message(@RequestHeader("first-request") String header) {
        log.info(header); // 2-2. log 사용
        return "GET: message(): Hello World in First Service";
    }

    // [API Gateway Service] - Custom Filter 적용
    @GetMapping("/check")
    public String check(HttpServletRequest request) {
        log.info("Server port={}", request.getServerPort());

        return String.format("Hi, Custom Filter from First Service on Port %s", env.getProperty("local.server.port"));
    }
}
