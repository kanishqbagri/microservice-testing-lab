package com.kb.jarvis.core.model;

import lombok.Builder;
import lombok.Data;
import java.util.Map;

@Data
@Builder
public class PerformanceMetrics {
    private double averageResponseTime;
    private double throughput;
    private double errorRate;
    private double cpuUsage;
    private double memoryUsage;
    private Map<String, Double> customMetrics;
} 