package com.kb.jarvis.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "test_results")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestResult {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @NotNull
    @Column(name = "test_name", nullable = false)
    private String testName;
    
    @NotNull
    @Column(name = "service_name", nullable = false)
    private String serviceName;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "test_type", nullable = false)
    private TestType testType;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TestStatus status;
    
    @Column(name = "execution_time_ms")
    private Long executionTimeMs;
    
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;
    
    @Column(name = "end_time")
    private LocalDateTime endTime;
    
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;
    
    @Column(name = "stack_trace", columnDefinition = "TEXT")
    private String stackTrace;
    
    @Column(name = "test_parameters", columnDefinition = "JSONB")
    private Map<String, Object> testParameters;
    
    @Column(name = "test_output", columnDefinition = "JSONB")
    private Map<String, Object> testOutput;
    
    @Column(name = "performance_metrics", columnDefinition = "JSONB")
    private Map<String, Object> performanceMetrics;
    
    @Column(name = "environment_info", columnDefinition = "JSONB")
    private Map<String, Object> environmentInfo;
    
    @Column(name = "confidence_score")
    private Double confidenceScore;
    
    @Column(name = "risk_level")
    @Enumerated(EnumType.STRING)
    private RiskLevel riskLevel;
    
    @Column(name = "tags")
    private String tags;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Additional fields for AI integration
    private String testId;
    private double duration;
    private LocalDateTime timestamp;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (startTime == null) {
            startTime = LocalDateTime.now();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
