package com.kb.jarvis.core.model;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class PerformanceMetrics {
    private double averageResponseTime;
    private double throughput;
    private double errorRate;
    private double cpuUsage;
    private double memoryUsage;
    private double diskUsage;
    private double networkLatency;
    private LocalDateTime timestamp;
    private Map<String, Double> customMetrics;
} 