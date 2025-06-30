package com.kb.gateway_service.service;

import com.kb.gateway_service.dto.GatewayStatusResponse;
import com.kb.gateway_service.dto.ServiceHealthResponse;

import java.util.List;

public interface GatewayService {
    GatewayStatusResponse getGatewayStatus();
    List<ServiceHealthResponse> getServicesHealth();
    ServiceHealthResponse getServiceHealth(String serviceName);
} 