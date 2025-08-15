package com.kb.gateway_service.service;

import com.kb.gateway_service.dto.GatewayStatusResponse;
import com.kb.gateway_service.dto.ServiceHealthResponse;
import com.kb.gateway_service.service.impl.GatewayServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GatewayServiceTest {

    private DiscoveryClient discoveryClient;
    private GatewayService gatewayService;

    @BeforeEach
    void setup() {
        discoveryClient = mock(DiscoveryClient.class);
        gatewayService = new GatewayServiceImpl(discoveryClient);
    }

    @Test
    void getGatewayStatus_ShouldReturnValidStatus() {
        GatewayStatusResponse response = gatewayService.getGatewayStatus();
        
        assertNotNull(response);
        assertEquals("UP", response.getStatus());
        assertEquals("1.0.0", response.getVersion());
        assertNotNull(response.getActiveRoutes());
        assertTrue(response.getActiveRoutes().contains("user-service"));
        assertTrue(response.getActiveRoutes().contains("product-service"));
    }

    @Test
    void getServiceHealth_ShouldReturnUp_WhenServiceHasInstances() {
        ServiceInstance instance = new DefaultServiceInstance("user-service-1", "user-service", "localhost", 8081, false);
        when(discoveryClient.getInstances("user-service"))
            .thenReturn(Arrays.asList(instance));

        ServiceHealthResponse response = gatewayService.getServiceHealth("user-service");
        
        assertNotNull(response);
        assertEquals("user-service", response.getService());
        assertEquals("UP", response.getStatus());
        assertTrue(response.getMessage().contains("Found 1 instance(s)"));
    }

    @Test
    void getServiceHealth_ShouldReturnDown_WhenServiceHasNoInstances() {
        when(discoveryClient.getInstances("user-service"))
            .thenReturn(Collections.emptyList());

        ServiceHealthResponse response = gatewayService.getServiceHealth("user-service");
        
        assertNotNull(response);
        assertEquals("user-service", response.getService());
        assertEquals("DOWN", response.getStatus());
        assertEquals("No instances available", response.getMessage());
    }
} 