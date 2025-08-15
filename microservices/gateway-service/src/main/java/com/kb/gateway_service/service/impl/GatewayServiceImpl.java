package com.kb.gateway_service.service.impl;

import com.kb.gateway_service.dto.GatewayStatusResponse;
import com.kb.gateway_service.dto.ServiceHealthResponse;
import com.kb.gateway_service.service.GatewayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GatewayServiceImpl implements GatewayService {

    private static final Logger log = LoggerFactory.getLogger(GatewayServiceImpl.class);
    private final DiscoveryClient discoveryClient;

    public GatewayServiceImpl(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @Override
    public GatewayStatusResponse getGatewayStatus() {
        List<String> activeRoutes = Arrays.asList(
            "user-service", "product-service", "order-service", "notification-service"
        );
        
        return new GatewayStatusResponse(
            "UP",
            "1.0.0",
            activeRoutes,
            System.currentTimeMillis()
        );
    }

    @Override
    public List<ServiceHealthResponse> getServicesHealth() {
        return Arrays.asList("user-service", "product-service", "order-service", "notification-service")
            .stream()
            .map(this::getServiceHealth)
            .collect(Collectors.toList());
    }

    @Override
    public ServiceHealthResponse getServiceHealth(String serviceName) {
        try {
            List<String> instances = discoveryClient.getInstances(serviceName)
                .stream()
                .map(instance -> instance.getUri().toString())
                .collect(Collectors.toList());
            
            String status = instances.isEmpty() ? "DOWN" : "UP";
            String message = instances.isEmpty() ? "No instances available" : 
                String.format("Found %d instance(s): %s", instances.size(), String.join(", ", instances));
            
            return new ServiceHealthResponse(serviceName, status, message, System.currentTimeMillis());
        } catch (Exception e) {
            log.error("Error checking health for service: {}", serviceName, e);
            return new ServiceHealthResponse(serviceName, "ERROR", e.getMessage(), System.currentTimeMillis());
        }
    }
} 