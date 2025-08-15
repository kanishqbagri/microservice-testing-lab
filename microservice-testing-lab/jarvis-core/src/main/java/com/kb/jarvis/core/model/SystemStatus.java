package com.kb.jarvis.core.model;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class SystemStatus {
    private List<ActiveTest> activeTests;
    private List<TestFailure> recentFailures;
    private SystemHealth systemHealth;
    private PerformanceMetrics performanceMetrics;
    private LocalDateTime lastUpdated;
} 