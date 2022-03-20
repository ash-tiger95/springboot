package com.example.apigatewayservice.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest; // reactive: Spring5부터 WebFlux 지원
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {

    public CustomFilter() {
        super(Config.class);
    }

    public static class Config { // <CustomFilter.Config>에서 Config라는 데이터타입 필요해서 생성
        // (필요시) Put the configuration properties
    }

    @Override
    public GatewayFilter apply(Config config) { // Custom Filter 작성
        // Custom Pre Filter
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest(); // (비동기) <-> ServletRequset(동기)
            ServerHttpResponse response = exchange.getResponse();

            log.info("Custom Pre Filter: request id -> {}", request.getId());

            // Custom Post Filter
            return chain.filter(exchange).then(Mono.fromRunnable(() -> { // Mono: 비동기 방식(WebFlux)에서 단일값 전달할 때 사용
                log.info("Custom Post Filter: response code -> {}", response.getStatusCode());
            }));
        };
    }
}
