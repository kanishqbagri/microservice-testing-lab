package com.kb.gateway_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceHealthResponse {
    private String service;
    private String status;
    private String message;
    private long timestamp;
} 