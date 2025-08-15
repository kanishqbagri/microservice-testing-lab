package com.kb.gateway_service.controller;

import com.kb.gateway_service.dto.GatewayStatusResponse;
import com.kb.gateway_service.dto.ServiceHealthResponse;
import com.kb.gateway_service.service.GatewayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gateway")
public class GatewayController {

    private final GatewayService gatewayService;

    public GatewayController(GatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    @GetMapping("/status")
    public ResponseEntity<GatewayStatusResponse> getGatewayStatus() {
        return ResponseEntity.ok(gatewayService.getGatewayStatus());
    }

    @GetMapping("/health")
    public ResponseEntity<List<ServiceHealthResponse>> getServicesHealth() {
        return ResponseEntity.ok(gatewayService.getServicesHealth());
    }

    @GetMapping("/health/{serviceName}")
    public ResponseEntity<ServiceHealthResponse> getServiceHealth(@PathVariable String serviceName) {
        return ResponseEntity.ok(gatewayService.getServiceHealth(serviceName));
    }

    @GetMapping("/fallback")
    public ResponseEntity<String> fallback() {
        return ResponseEntity.ok("Service temporarily unavailable. Please try again later.");
    }
} 