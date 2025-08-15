package com.kb.jarvis.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestGenerationRequest {
    
    private UUID id;
    
    @NotNull
    private String serviceName;
    
    @NotNull
    private TestType testType;
    
    private String serviceSpecification; // OpenAPI/Swagger spec
    
    private String serviceEndpoint;
    
    private List<String> testScenarios;
    
    private Map<String, Object> testParameters;
    
    private TestScope testScope;
    
    private ExecutionStrategy executionStrategy;
    
    private Integer numberOfTests;
    
    private Boolean includeEdgeCases;
    
    private Boolean includeNegativeTests;
    
    private Boolean includePerformanceTests;
    
    private Boolean includeSecurityTests;
    
    private Map<String, Object> constraints;
    
    private String priority;
    
    private String description;
    
    private LocalDateTime requestedAt;
    
    private String requestedBy;
    
    private Map<String, Object> metadata;
    
    @PrePersist
    protected void onCreate() {
        if (requestedAt == null) {
            requestedAt = LocalDateTime.now();
        }
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
}
