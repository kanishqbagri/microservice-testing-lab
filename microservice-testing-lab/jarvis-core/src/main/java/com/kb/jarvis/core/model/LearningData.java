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
@Table(name = "learning_data")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LearningData {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @NotNull
    @Column(name = "data_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private LearningDataType dataType;
    
    @NotNull
    @Column(name = "service_name", nullable = false)
    private String serviceName;
    
    @Column(name = "test_pattern", columnDefinition = "JSONB")
    private Map<String, Object> testPattern;
    
    @Column(name = "failure_pattern", columnDefinition = "JSONB")
    private Map<String, Object> failurePattern;
    
    @Column(name = "performance_pattern", columnDefinition = "JSONB")
    private Map<String, Object> performancePattern;
    
    @Column(name = "optimization_pattern", columnDefinition = "JSONB")
    private Map<String, Object> optimizationPattern;
    
    @Column(name = "feature_vector", columnDefinition = "JSONB")
    private Map<String, Object> featureVector;
    
    @Column(name = "label")
    private String label;
    
    @Column(name = "confidence_score")
    private Double confidenceScore;
    
    @Column(name = "model_version")
    private String modelVersion;
    
    @Column(name = "training_used")
    private Boolean trainingUsed;
    
    @Column(name = "validation_used")
    private Boolean validationUsed;
    
    @Column(name = "metadata", columnDefinition = "JSONB")
    private Map<String, Object> metadata;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Additional fields for AI integration
    private LearningDataType type;
    private Map<String, Object> data;
    private String insights;
    private LocalDateTime timestamp;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
