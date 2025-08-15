package com.kb.jarvis.core.model;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class SystemHealth {
    private HealthStatus overallStatus;
    private Map<String, ServiceHealth> serviceHealth;
    private List<String> issues;
    private LocalDateTime lastCheck;
    private HealthStatus status;
    private LocalDateTime timestamp;
} 