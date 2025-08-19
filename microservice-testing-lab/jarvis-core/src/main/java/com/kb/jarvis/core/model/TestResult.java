package com.kb.jarvis.core.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "test_results")
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

    // Default constructor
    public TestResult() {}

    // Constructor with all fields
    public TestResult(UUID id, String testName, String serviceName, TestType testType, TestStatus status,
                     Long executionTimeMs, LocalDateTime startTime, LocalDateTime endTime, String errorMessage,
                     String stackTrace, Map<String, Object> testParameters, Map<String, Object> testOutput,
                     Map<String, Object> performanceMetrics, Map<String, Object> environmentInfo,
                     Double confidenceScore, RiskLevel riskLevel, String tags, LocalDateTime createdAt,
                     LocalDateTime updatedAt, String testId, double duration, LocalDateTime timestamp) {
        this.id = id;
        this.testName = testName;
        this.serviceName = serviceName;
        this.testType = testType;
        this.status = status;
        this.executionTimeMs = executionTimeMs;
        this.startTime = startTime;
        this.endTime = endTime;
        this.errorMessage = errorMessage;
        this.stackTrace = stackTrace;
        this.testParameters = testParameters;
        this.testOutput = testOutput;
        this.performanceMetrics = performanceMetrics;
        this.environmentInfo = environmentInfo;
        this.confidenceScore = confidenceScore;
        this.riskLevel = riskLevel;
        this.tags = tags;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.testId = testId;
        this.duration = duration;
        this.timestamp = timestamp;
    }

    // Builder pattern
    public static TestResultBuilder builder() {
        return new TestResultBuilder();
    }

    // Getter methods
    public UUID getId() { return id; }
    public String getTestName() { return testName; }
    public String getServiceName() { return serviceName; }
    public TestType getTestType() { return testType; }
    public TestStatus getStatus() { return status; }
    public Long getExecutionTimeMs() { return executionTimeMs; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public String getErrorMessage() { return errorMessage; }
    public String getStackTrace() { return stackTrace; }
    public Map<String, Object> getTestParameters() { return testParameters; }
    public Map<String, Object> getTestOutput() { return testOutput; }
    public Map<String, Object> getPerformanceMetrics() { return performanceMetrics; }
    public Map<String, Object> getEnvironmentInfo() { return environmentInfo; }
    public Double getConfidenceScore() { return confidenceScore; }
    public RiskLevel getRiskLevel() { return riskLevel; }
    public String getTags() { return tags; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public String getTestId() { return testId; }
    public double getDuration() { return duration; }
    public LocalDateTime getTimestamp() { return timestamp; }

    // Setter methods
    public void setId(UUID id) { this.id = id; }
    public void setTestName(String testName) { this.testName = testName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public void setTestType(TestType testType) { this.testType = testType; }
    public void setStatus(TestStatus status) { this.status = status; }
    public void setExecutionTimeMs(Long executionTimeMs) { this.executionTimeMs = executionTimeMs; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public void setStackTrace(String stackTrace) { this.stackTrace = stackTrace; }
    public void setTestParameters(Map<String, Object> testParameters) { this.testParameters = testParameters; }
    public void setTestOutput(Map<String, Object> testOutput) { this.testOutput = testOutput; }
    public void setPerformanceMetrics(Map<String, Object> performanceMetrics) { this.performanceMetrics = performanceMetrics; }
    public void setEnvironmentInfo(Map<String, Object> environmentInfo) { this.environmentInfo = environmentInfo; }
    public void setConfidenceScore(Double confidenceScore) { this.confidenceScore = confidenceScore; }
    public void setRiskLevel(RiskLevel riskLevel) { this.riskLevel = riskLevel; }
    public void setTags(String tags) { this.tags = tags; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public void setTestId(String testId) { this.testId = testId; }
    public void setDuration(double duration) { this.duration = duration; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    // equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestResult that = (TestResult) o;
        return Double.compare(that.duration, duration) == 0 &&
                Objects.equals(id, that.id) &&
                Objects.equals(testName, that.testName) &&
                Objects.equals(serviceName, that.serviceName) &&
                testType == that.testType &&
                status == that.status &&
                Objects.equals(executionTimeMs, that.executionTimeMs) &&
                Objects.equals(startTime, that.startTime) &&
                Objects.equals(endTime, that.endTime) &&
                Objects.equals(errorMessage, that.errorMessage) &&
                Objects.equals(stackTrace, that.stackTrace) &&
                Objects.equals(testParameters, that.testParameters) &&
                Objects.equals(testOutput, that.testOutput) &&
                Objects.equals(performanceMetrics, that.performanceMetrics) &&
                Objects.equals(environmentInfo, that.environmentInfo) &&
                Objects.equals(confidenceScore, that.confidenceScore) &&
                riskLevel == that.riskLevel &&
                Objects.equals(tags, that.tags) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt) &&
                Objects.equals(testId, that.testId) &&
                Objects.equals(timestamp, that.timestamp);
    }

    // hashCode method
    @Override
    public int hashCode() {
        return Objects.hash(id, testName, serviceName, testType, status, executionTimeMs, startTime, endTime,
                errorMessage, stackTrace, testParameters, testOutput, performanceMetrics, environmentInfo,
                confidenceScore, riskLevel, tags, createdAt, updatedAt, testId, duration, timestamp);
    }

    // toString method
    @Override
    public String toString() {
        return "TestResult{" +
                "id=" + id +
                ", testName='" + testName + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", testType=" + testType +
                ", status=" + status +
                ", executionTimeMs=" + executionTimeMs +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", errorMessage='" + errorMessage + '\'' +
                ", stackTrace='" + stackTrace + '\'' +
                ", testParameters=" + testParameters +
                ", testOutput=" + testOutput +
                ", performanceMetrics=" + performanceMetrics +
                ", environmentInfo=" + environmentInfo +
                ", confidenceScore=" + confidenceScore +
                ", riskLevel=" + riskLevel +
                ", tags='" + tags + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", testId='" + testId + '\'' +
                ", duration=" + duration +
                ", timestamp=" + timestamp +
                '}';
    }

    // Builder class
    public static class TestResultBuilder {
        private UUID id;
        private String testName;
        private String serviceName;
        private TestType testType;
        private TestStatus status;
        private Long executionTimeMs;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private String errorMessage;
        private String stackTrace;
        private Map<String, Object> testParameters;
        private Map<String, Object> testOutput;
        private Map<String, Object> performanceMetrics;
        private Map<String, Object> environmentInfo;
        private Double confidenceScore;
        private RiskLevel riskLevel;
        private String tags;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String testId;
        private double duration;
        private LocalDateTime timestamp;

        public TestResultBuilder id(UUID id) { this.id = id; return this; }
        public TestResultBuilder testName(String testName) { this.testName = testName; return this; }
        public TestResultBuilder serviceName(String serviceName) { this.serviceName = serviceName; return this; }
        public TestResultBuilder testType(TestType testType) { this.testType = testType; return this; }
        public TestResultBuilder status(TestStatus status) { this.status = status; return this; }
        public TestResultBuilder executionTimeMs(Long executionTimeMs) { this.executionTimeMs = executionTimeMs; return this; }
        public TestResultBuilder startTime(LocalDateTime startTime) { this.startTime = startTime; return this; }
        public TestResultBuilder endTime(LocalDateTime endTime) { this.endTime = endTime; return this; }
        public TestResultBuilder errorMessage(String errorMessage) { this.errorMessage = errorMessage; return this; }
        public TestResultBuilder stackTrace(String stackTrace) { this.stackTrace = stackTrace; return this; }
        public TestResultBuilder testParameters(Map<String, Object> testParameters) { this.testParameters = testParameters; return this; }
        public TestResultBuilder testOutput(Map<String, Object> testOutput) { this.testOutput = testOutput; return this; }
        public TestResultBuilder performanceMetrics(Map<String, Object> performanceMetrics) { this.performanceMetrics = performanceMetrics; return this; }
        public TestResultBuilder environmentInfo(Map<String, Object> environmentInfo) { this.environmentInfo = environmentInfo; return this; }
        public TestResultBuilder confidenceScore(Double confidenceScore) { this.confidenceScore = confidenceScore; return this; }
        public TestResultBuilder riskLevel(RiskLevel riskLevel) { this.riskLevel = riskLevel; return this; }
        public TestResultBuilder tags(String tags) { this.tags = tags; return this; }
        public TestResultBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public TestResultBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
        public TestResultBuilder testId(String testId) { this.testId = testId; return this; }
        public TestResultBuilder duration(double duration) { this.duration = duration; return this; }
        public TestResultBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }

        public TestResult build() {
            return new TestResult(id, testName, serviceName, testType, status, executionTimeMs, startTime, endTime,
                    errorMessage, stackTrace, testParameters, testOutput, performanceMetrics, environmentInfo,
                    confidenceScore, riskLevel, tags, createdAt, updatedAt, testId, duration, timestamp);
        }
    }
    
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
