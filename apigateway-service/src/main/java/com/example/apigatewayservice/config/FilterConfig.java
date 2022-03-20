package com.example.apigatewayservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// application.yml 작업과 동일한 기능

// @Configuration // 단순히 정보 등록
public class FilterConfig {
    // @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder){
        return builder.routes()
                .route(r -> r.path("/first-service/**")
                            .filters(f -> f.addRequestHeader("first-request","first-request-value")
                                    .addResponseHeader("first-response","first-response-value")
                            )
                            .uri("http://localhost:8081/") // path(): path가 들어오면 / filters(): 필터 적용 후 / uri(): uri로 이동하겠다.
                )
                .route(r -> r.path("/second-service/**")
                        .filters(f -> f.addRequestHeader("second-request","second-request-value")
                                .addResponseHeader("second-response","second-response-value")
                        )
                        .uri("http://localhost:8082/")
                )
                .build(); // route(): 추가하고자 하는 라우트 정보 / build(): 라우트에 필요한 내용을 메모리에 반영
    }
}
