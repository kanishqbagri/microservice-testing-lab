package com.kb.gateway_service.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("user-service", r -> r
                .path("/api/users/**")
                .filters(f -> f
                    .stripPrefix(0)
                    .addRequestHeader("X-Response-Time", System.currentTimeMillis() + ""))
                .uri("lb://user-service"))
            .route("product-service", r -> r
                .path("/api/products/**")
                .filters(f -> f
                    .stripPrefix(0)
                    .addRequestHeader("X-Response-Time", System.currentTimeMillis() + ""))
                .uri("lb://product-service"))
            .route("order-service", r -> r
                .path("/api/orders/**")
                .filters(f -> f
                    .stripPrefix(0)
                    .addRequestHeader("X-Response-Time", System.currentTimeMillis() + ""))
                .uri("lb://order-service"))
            .route("notification-service", r -> r
                .path("/api/notifications/**")
                .filters(f -> f
                    .stripPrefix(0)
                    .addRequestHeader("X-Response-Time", System.currentTimeMillis() + ""))
                .uri("lb://notification-service"))
            .build();
    }
} 