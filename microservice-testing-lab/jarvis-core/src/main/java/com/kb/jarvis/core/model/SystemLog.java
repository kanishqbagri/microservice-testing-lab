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
@Table(name = "system_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @NotNull
    @Column(name = "log_level", nullable = false)
    @Enumerated(EnumType.STRING)
    private LogLevel logLevel;
    
    @NotNull
    @Column(name = "component", nullable = false)
    private String component;
    
    @NotNull
    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;
    
    @Column(name = "service_name")
    private String serviceName;
    
    @Column(name = "test_name")
    private String testName;
    
    @Column(name = "user_id")
    private String userId;
    
    @Column(name = "session_id")
    private String sessionId;
    
    @Column(name = "request_id")
    private String requestId;
    
    @Column(name = "exception", columnDefinition = "TEXT")
    private String exception;
    
    @Column(name = "stack_trace", columnDefinition = "TEXT")
    private String stackTrace;
    
    @Column(name = "context_data", columnDefinition = "JSONB")
    private Map<String, Object> contextData;
    
    @Column(name = "performance_metrics", columnDefinition = "JSONB")
    private Map<String, Object> performanceMetrics;
    
    @Column(name = "tags")
    private String tags;
    
    @Column(name = "correlation_id")
    private String correlationId;
    
    @Column(name = "parent_log_id")
    private UUID parentLogId;
    
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }
}
