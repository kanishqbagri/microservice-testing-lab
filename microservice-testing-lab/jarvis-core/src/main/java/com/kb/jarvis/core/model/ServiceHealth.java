package com.kb.jarvis.core.model;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ServiceHealth {
    private String serviceName;
    private HealthStatus status;
    private double responseTime;
    private double availability;
    private List<String> issues;
    private String message;
    private LocalDateTime timestamp;
} 