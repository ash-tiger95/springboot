package com.example.apigatewayservice.filter;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    Environment env;

    public AuthorizationHeaderFilter(Environment env) {
        super(Config.class); // Config정보를 부모클래스에 알려줘야 오류가 안난다
        this.env = env;
    }

    public static class Config {

    }

    // login -> token 반환 -> 클라이언트에서 요청할 때(토큰 정보가지고) -> 서버는 토큰 정보가 적합한지 검사(header에 들어가있음)
    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest(); // 사용자가 요청했을 때

            // 받은 토큰을 전달해주는 로직
            if(!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){ // 권한
                return onError(exchange, "no authorization header", HttpStatus.UNAUTHORIZED);
            }

            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String jwt = authorizationHeader.replace("Bearer","");

            if(!isJwtValid(jwt)){
                return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
            }

            return chain.filter(exchange);
        });
    }

    private boolean isJwtValid(String jwt) {
        boolean returnValue = true;

        String subject = null; // jwt 내에 존재, 우리가 원하는 데이터 타입을 확인할 수 있다.
        try{
            subject = Jwts.parser().setSigningKey(env.getProperty("token.secret")) // decoder
                    .parseClaimsJws(jwt) // 토큰을 문자형으로 파싱
                    .getBody() // 그 안에서
                    .getSubject(); // subject 추출
        } catch(Exception e){
            returnValue = false;
        }
        if(subject == null || subject.isEmpty()){
            returnValue= false;
        }

        return returnValue;
    }

    // Mono(단일값), Flux(다중값) 데이터 타입 -> Spring WebFlux(클라이언트에게 반환하는 데이터 타입)
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        log.error(err);
        return response.setComplete();
    }
}
