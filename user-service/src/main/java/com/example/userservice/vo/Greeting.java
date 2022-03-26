package com.example.userservice.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor // 모든 속성을 가진 생성자
@NoArgsConstructor // 빈 생성자
public class Greeting { // application.yml의 값 가져오기, 방법2) @Value

    @Value("${greeting.message}")
    private String message;
}
