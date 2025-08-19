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
@Table(name = "test_generation_requests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestGenerationRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @NotNull
    @Column(name = "request_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TestGenerationType requestType;
    
    @NotNull
    @Column(name = "service_name", nullable = false)
    private String serviceName;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "requirements", columnDefinition = "JSONB")
    private Map<String, Object> requirements;
    
    @Column(name = "constraints", columnDefinition = "JSONB")
    private Map<String, Object> constraints;
    
    @Column(name = "priority")
    @Enumerated(EnumType.STRING)
    private Priority priority;
    
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TestGenerationStatus status;
    
    @Column(name = "progress")
    private Double progress;
    
    @Column(name = "estimated_completion_time")
    private LocalDateTime estimatedCompletionTime;
    
    @Column(name = "actual_completion_time")
    private LocalDateTime actualCompletionTime;
    
    @Column(name = "generated_tests", columnDefinition = "JSONB")
    private Map<String, Object> generatedTests;
    
    @Column(name = "metadata", columnDefinition = "JSONB")
    private Map<String, Object> metadata;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
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
public class TestGenerationRequest {
    private java.util.UUID id;
    private TestGenerationType requestType;
    private String serviceName;
    private String description;
    private java.util.Map<String, Object> requirements;
    private java.util.Map<String, Object> constraints;
    private Priority priority;
    private TestGenerationStatus status;
    private Double progress;
    private java.time.LocalDateTime estimatedCompletionTime;
    private java.time.LocalDateTime actualCompletionTime;
    private java.util.Map<String, Object> generatedTests;
    private java.util.Map<String, Object> metadata;
    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime updatedAt;
    
    public TestGenerationRequest() {}
    
    public java.util.UUID getId() { return id; }
    public void setId(java.util.UUID id) { this.id = id; }
    public TestGenerationType getRequestType() { return requestType; }
    public void setRequestType(TestGenerationType requestType) { this.requestType = requestType; }
    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public java.util.Map<String, Object> getRequirements() { return requirements; }
    public void setRequirements(java.util.Map<String, Object> requirements) { this.requirements = requirements; }
    public java.util.Map<String, Object> getConstraints() { return constraints; }
    public void setConstraints(java.util.Map<String, Object> constraints) { this.constraints = constraints; }
    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }
    public TestGenerationStatus getStatus() { return status; }
    public void setStatus(TestGenerationStatus status) { this.status = status; }
    public Double getProgress() { return progress; }
    public void setProgress(Double progress) { this.progress = progress; }
    public java.time.LocalDateTime getEstimatedCompletionTime() { return estimatedCompletionTime; }
    public void setEstimatedCompletionTime(java.time.LocalDateTime estimatedCompletionTime) { this.estimatedCompletionTime = estimatedCompletionTime; }
    public java.time.LocalDateTime getActualCompletionTime() { return actualCompletionTime; }
    public void setActualCompletionTime(java.time.LocalDateTime actualCompletionTime) { this.actualCompletionTime = actualCompletionTime; }
    public java.util.Map<String, Object> getGeneratedTests() { return generatedTests; }
    public void setGeneratedTests(java.util.Map<String, Object> generatedTests) { this.generatedTests = generatedTests; }
    public java.util.Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(java.util.Map<String, Object> metadata) { this.metadata = metadata; }
    public java.time.LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(java.time.LocalDateTime createdAt) { this.createdAt = createdAt; }
    public java.time.LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(java.time.LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
