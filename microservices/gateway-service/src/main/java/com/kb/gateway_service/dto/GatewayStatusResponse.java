package com.kb.gateway_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GatewayStatusResponse {
    private String status;
    private String version;
    private List<String> activeRoutes;
    private long uptime;
} 