package com.kb.jarvis.core.model;

// TEMPORARILY COMMENTED OUT FOR QUICK AI INTEGRATION TESTING
// TODO: Convert to manual implementation after AI integration testing
/*
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
*/

// Temporary placeholder for compilation
public class LearningData {
    private java.util.UUID id;
    private LearningDataType dataType;
    private String serviceName;
    private java.util.Map<String, Object> testPattern;
    private java.util.Map<String, Object> failurePattern;
    private java.util.Map<String, Object> performancePattern;
    private java.util.Map<String, Object> optimizationPattern;
    private java.util.Map<String, Object> featureVector;
    private String label;
    private Double confidenceScore;
    private String modelVersion;
    private Boolean trainingUsed;
    private Boolean validationUsed;
    private java.util.Map<String, Object> metadata;
    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime updatedAt;
    private LearningDataType type;
    private java.util.Map<String, Object> data;
    private String insights;
    private java.time.LocalDateTime timestamp;
    
    public LearningData() {}
    
    public java.util.UUID getId() { return id; }
    public void setId(java.util.UUID id) { this.id = id; }
    public LearningDataType getDataType() { return dataType; }
    public void setDataType(LearningDataType dataType) { this.dataType = dataType; }
    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public java.util.Map<String, Object> getTestPattern() { return testPattern; }
    public void setTestPattern(java.util.Map<String, Object> testPattern) { this.testPattern = testPattern; }
    public java.util.Map<String, Object> getFailurePattern() { return failurePattern; }
    public void setFailurePattern(java.util.Map<String, Object> failurePattern) { this.failurePattern = failurePattern; }
    public java.util.Map<String, Object> getPerformancePattern() { return performancePattern; }
    public void setPerformancePattern(java.util.Map<String, Object> performancePattern) { this.performancePattern = performancePattern; }
    public java.util.Map<String, Object> getOptimizationPattern() { return optimizationPattern; }
    public void setOptimizationPattern(java.util.Map<String, Object> optimizationPattern) { this.optimizationPattern = optimizationPattern; }
    public java.util.Map<String, Object> getFeatureVector() { return featureVector; }
    public void setFeatureVector(java.util.Map<String, Object> featureVector) { this.featureVector = featureVector; }
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public Double getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(Double confidenceScore) { this.confidenceScore = confidenceScore; }
    public String getModelVersion() { return modelVersion; }
    public void setModelVersion(String modelVersion) { this.modelVersion = modelVersion; }
    public Boolean getTrainingUsed() { return trainingUsed; }
    public void setTrainingUsed(Boolean trainingUsed) { this.trainingUsed = trainingUsed; }
    public Boolean getValidationUsed() { return validationUsed; }
    public void setValidationUsed(Boolean validationUsed) { this.validationUsed = validationUsed; }
    public java.util.Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(java.util.Map<String, Object> metadata) { this.metadata = metadata; }
    public java.time.LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(java.time.LocalDateTime createdAt) { this.createdAt = createdAt; }
    public java.time.LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(java.time.LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public LearningDataType getType() { return type; }
    public void setType(LearningDataType type) { this.type = type; }
    public java.util.Map<String, Object> getData() { return data; }
    public void setData(java.util.Map<String, Object> data) { this.data = data; }
    public String getInsights() { return insights; }
    public void setInsights(String insights) { this.insights = insights; }
    public java.time.LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(java.time.LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    // Builder pattern
    public static LearningDataBuilder builder() {
        return new LearningDataBuilder();
    }
    
    // Builder class
    public static class LearningDataBuilder {
        private java.util.UUID id;
        private LearningDataType dataType;
        private String serviceName;
        private java.util.Map<String, Object> testPattern;
        private java.util.Map<String, Object> failurePattern;
        private java.util.Map<String, Object> performancePattern;
        private java.util.Map<String, Object> optimizationPattern;
        private java.util.Map<String, Object> featureVector;
        private String label;
        private Double confidenceScore;
        private String modelVersion;
        private Boolean trainingUsed;
        private Boolean validationUsed;
        private java.util.Map<String, Object> metadata;
        private java.time.LocalDateTime createdAt;
        private java.time.LocalDateTime updatedAt;
        private LearningDataType type;
        private java.util.Map<String, Object> data;
        private String insights;
        private java.time.LocalDateTime timestamp;
        
        public LearningDataBuilder id(java.util.UUID id) { this.id = id; return this; }
        public LearningDataBuilder dataType(LearningDataType dataType) { this.dataType = dataType; return this; }
        public LearningDataBuilder serviceName(String serviceName) { this.serviceName = serviceName; return this; }
        public LearningDataBuilder testPattern(java.util.Map<String, Object> testPattern) { this.testPattern = testPattern; return this; }
        public LearningDataBuilder failurePattern(java.util.Map<String, Object> failurePattern) { this.failurePattern = failurePattern; return this; }
        public LearningDataBuilder performancePattern(java.util.Map<String, Object> performancePattern) { this.performancePattern = performancePattern; return this; }
        public LearningDataBuilder optimizationPattern(java.util.Map<String, Object> optimizationPattern) { this.optimizationPattern = optimizationPattern; return this; }
        public LearningDataBuilder featureVector(java.util.Map<String, Object> featureVector) { this.featureVector = featureVector; return this; }
        public LearningDataBuilder label(String label) { this.label = label; return this; }
        public LearningDataBuilder confidenceScore(Double confidenceScore) { this.confidenceScore = confidenceScore; return this; }
        public LearningDataBuilder modelVersion(String modelVersion) { this.modelVersion = modelVersion; return this; }
        public LearningDataBuilder trainingUsed(Boolean trainingUsed) { this.trainingUsed = trainingUsed; return this; }
        public LearningDataBuilder validationUsed(Boolean validationUsed) { this.validationUsed = validationUsed; return this; }
        public LearningDataBuilder metadata(java.util.Map<String, Object> metadata) { this.metadata = metadata; return this; }
        public LearningDataBuilder createdAt(java.time.LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public LearningDataBuilder updatedAt(java.time.LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
        public LearningDataBuilder type(LearningDataType type) { this.type = type; return this; }
        public LearningDataBuilder data(java.util.Map<String, Object> data) { this.data = data; return this; }
        public LearningDataBuilder insights(String insights) { this.insights = insights; return this; }
        public LearningDataBuilder timestamp(java.time.LocalDateTime timestamp) { this.timestamp = timestamp; return this; }
        
        public LearningData build() {
            LearningData data = new LearningData();
            data.setId(id);
            data.setDataType(dataType);
            data.setServiceName(serviceName);
            data.setTestPattern(testPattern);
            data.setFailurePattern(failurePattern);
            data.setPerformancePattern(performancePattern);
            data.setOptimizationPattern(optimizationPattern);
            data.setFeatureVector(featureVector);
            data.setLabel(label);
            data.setConfidenceScore(confidenceScore);
            data.setModelVersion(modelVersion);
            data.setTrainingUsed(trainingUsed);
            data.setValidationUsed(validationUsed);
            data.setMetadata(metadata);
            data.setCreatedAt(createdAt);
            data.setUpdatedAt(updatedAt);
            data.setType(type);
            data.setData(this.data);
            data.setInsights(insights);
            data.setTimestamp(timestamp);
            return data;
        }
    }
}
