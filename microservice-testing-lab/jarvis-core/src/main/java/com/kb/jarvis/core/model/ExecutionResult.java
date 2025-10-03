package com.kb.jarvis.core.model;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Execution Result Model
 * Represents the result of an action execution
 */
public class ExecutionResult {
    
    private boolean success;
    private String message;
    private Map<String, Object> data;
    private LocalDateTime timestamp;
    private String executionId;
    private String serviceName;
    private ActionType actionType;
    private TestType testType;
    private String duration;
    private String errorDetails;
    private Map<String, Object> metrics;
    private Map<String, Object> recommendations;
    
    // Additional fields for comprehensive results
    private int totalSteps;
    private int successfulSteps;
    private int failedSteps;
    private double successRate;
    private String executionStrategy;
    private String estimatedDuration;
    private String actualDuration;

    // Constructor
    public ExecutionResult() {}

    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ExecutionResult result = new ExecutionResult();

        public Builder success(boolean success) {
            result.success = success;
            return this;
        }

        public Builder message(String message) {
            result.message = message;
            return this;
        }

        public Builder data(Map<String, Object> data) {
            result.data = data;
            return this;
        }

        public Builder timestamp(LocalDateTime timestamp) {
            result.timestamp = timestamp;
            return this;
        }

        public Builder executionId(String executionId) {
            result.executionId = executionId;
            return this;
        }

        public Builder serviceName(String serviceName) {
            result.serviceName = serviceName;
            return this;
        }

        public Builder actionType(ActionType actionType) {
            result.actionType = actionType;
            return this;
        }

        public Builder testType(TestType testType) {
            result.testType = testType;
            return this;
        }

        public Builder duration(String duration) {
            result.duration = duration;
            return this;
        }

        public Builder errorDetails(String errorDetails) {
            result.errorDetails = errorDetails;
            return this;
        }

        public Builder metrics(Map<String, Object> metrics) {
            result.metrics = metrics;
            return this;
        }

        public Builder recommendations(Map<String, Object> recommendations) {
            result.recommendations = recommendations;
            return this;
        }

        public Builder totalSteps(int totalSteps) {
            result.totalSteps = totalSteps;
            return this;
        }

        public Builder successfulSteps(int successfulSteps) {
            result.successfulSteps = successfulSteps;
            return this;
        }

        public Builder failedSteps(int failedSteps) {
            result.failedSteps = failedSteps;
            return this;
        }

        public Builder successRate(double successRate) {
            result.successRate = successRate;
            return this;
        }

        public Builder executionStrategy(String executionStrategy) {
            result.executionStrategy = executionStrategy;
            return this;
        }

        public Builder estimatedDuration(String estimatedDuration) {
            result.estimatedDuration = estimatedDuration;
            return this;
        }

        public Builder actualDuration(String actualDuration) {
            result.actualDuration = actualDuration;
            return this;
        }

        public ExecutionResult build() {
            return result;
        }
    }

    // Getters
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getExecutionId() {
        return executionId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public TestType getTestType() {
        return testType;
    }

    public String getDuration() {
        return duration;
    }

    public String getErrorDetails() {
        return errorDetails;
    }

    public Map<String, Object> getMetrics() {
        return metrics;
    }

    public Map<String, Object> getRecommendations() {
        return recommendations;
    }

    public int getTotalSteps() {
        return totalSteps;
    }

    public int getSuccessfulSteps() {
        return successfulSteps;
    }

    public int getFailedSteps() {
        return failedSteps;
    }

    public double getSuccessRate() {
        return successRate;
    }

    public String getExecutionStrategy() {
        return executionStrategy;
    }

    public String getEstimatedDuration() {
        return estimatedDuration;
    }

    public String getActualDuration() {
        return actualDuration;
    }
}