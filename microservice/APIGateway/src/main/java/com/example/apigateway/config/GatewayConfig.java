package com.example.apigateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("member-service", r -> r
                        .path("/api/members/**", "/api/auth/**")
                        .uri("http://localhost:8081"))
                .route("recognition-service", r -> r
                        .path("/api/recognition/**")
                        .uri("lb://RECOGNITION-SERVICE"))
                .route("verification-service", r -> r
                        .path("/api/verification/**", "/api/alerts/**")
                        .uri("http://localhost:8083"))
                .build();
    }
}