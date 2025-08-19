package com.kb.jarvis.core.model;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

public class DecisionAction {
    private ActionType type;
    private String description;
    private TestParameters parameters;
    private String estimatedTime;
    private Priority priority;
    private ExecutionStrategy executionStrategy;
    private double confidence;
    private LocalDateTime timestamp;
    private Map<String, Object> metadata;

    // Default constructor
    public DecisionAction() {}

    // Constructor with all fields
    public DecisionAction(ActionType type, String description, TestParameters parameters, String estimatedTime,
                         Priority priority, ExecutionStrategy executionStrategy, double confidence,
                         LocalDateTime timestamp, Map<String, Object> metadata) {
        this.type = type;
        this.description = description;
        this.parameters = parameters;
        this.estimatedTime = estimatedTime;
        this.priority = priority;
        this.executionStrategy = executionStrategy;
        this.confidence = confidence;
        this.timestamp = timestamp;
        this.metadata = metadata;
    }

    // Builder pattern
    public static DecisionActionBuilder builder() {
        return new DecisionActionBuilder();
    }

    // Getter methods
    public ActionType getType() { return type; }
    public String getDescription() { return description; }
    public TestParameters getParameters() { return parameters; }
    public String getEstimatedTime() { return estimatedTime; }
    public Priority getPriority() { return priority; }
    public ExecutionStrategy getExecutionStrategy() { return executionStrategy; }
    public double getConfidence() { return confidence; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public Map<String, Object> getMetadata() { return metadata; }

    // Setter methods
    public void setType(ActionType type) { this.type = type; }
    public void setDescription(String description) { this.description = description; }
    public void setParameters(TestParameters parameters) { this.parameters = parameters; }
    public void setEstimatedTime(String estimatedTime) { this.estimatedTime = estimatedTime; }
    public void setPriority(Priority priority) { this.priority = priority; }
    public void setExecutionStrategy(ExecutionStrategy executionStrategy) { this.executionStrategy = executionStrategy; }
    public void setConfidence(double confidence) { this.confidence = confidence; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }

    // equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DecisionAction that = (DecisionAction) o;
        return Double.compare(that.confidence, confidence) == 0 &&
                type == that.type &&
                Objects.equals(description, that.description) &&
                Objects.equals(parameters, that.parameters) &&
                Objects.equals(estimatedTime, that.estimatedTime) &&
                priority == that.priority &&
                executionStrategy == that.executionStrategy &&
                Objects.equals(timestamp, that.timestamp) &&
                Objects.equals(metadata, that.metadata);
    }

    // hashCode method
    @Override
    public int hashCode() {
        return Objects.hash(type, description, parameters, estimatedTime, priority, executionStrategy, confidence, timestamp, metadata);
    }

    // toString method
    @Override
    public String toString() {
        return "DecisionAction{" +
                "type=" + type +
                ", description='" + description + '\'' +
                ", parameters=" + parameters +
                ", estimatedTime='" + estimatedTime + '\'' +
                ", priority=" + priority +
                ", executionStrategy=" + executionStrategy +
                ", confidence=" + confidence +
                ", timestamp=" + timestamp +
                ", metadata=" + metadata +
                '}';
    }

    // Builder class
    public static class DecisionActionBuilder {
        private ActionType type;
        private String description;
        private TestParameters parameters;
        private String estimatedTime;
        private Priority priority;
        private ExecutionStrategy executionStrategy;
        private double confidence;
        private LocalDateTime timestamp;
        private Map<String, Object> metadata;

        public DecisionActionBuilder type(ActionType type) { this.type = type; return this; }
        public DecisionActionBuilder description(String description) { this.description = description; return this; }
        public DecisionActionBuilder parameters(TestParameters parameters) { this.parameters = parameters; return this; }
        public DecisionActionBuilder estimatedTime(String estimatedTime) { this.estimatedTime = estimatedTime; return this; }
        public DecisionActionBuilder priority(Priority priority) { this.priority = priority; return this; }
        public DecisionActionBuilder executionStrategy(ExecutionStrategy executionStrategy) { this.executionStrategy = executionStrategy; return this; }
        public DecisionActionBuilder confidence(double confidence) { this.confidence = confidence; return this; }
        public DecisionActionBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }
        public DecisionActionBuilder metadata(Map<String, Object> metadata) { this.metadata = metadata; return this; }

        public DecisionAction build() {
            return new DecisionAction(type, description, parameters, estimatedTime, priority, executionStrategy, confidence, timestamp, metadata);
        }
    }
} 