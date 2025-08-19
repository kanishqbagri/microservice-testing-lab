package com.kb.jarvis.core.model;

import java.time.LocalDateTime;
import java.util.Map;

public class UserIntent {
    private String rawInput;
    private IntentType type;
    private String description;
    private Map<String, Object> parameters;
    private double confidence;
    private LocalDateTime timestamp;

    public UserIntent() {
    }

    public UserIntent(String rawInput, IntentType type, String description, Map<String, Object> parameters, double confidence, LocalDateTime timestamp) {
        this.rawInput = rawInput;
        this.type = type;
        this.description = description;
        this.parameters = parameters;
        this.confidence = confidence;
        this.timestamp = timestamp;
    }

    public static UserIntentBuilder builder() {
        return new UserIntentBuilder();
    }

    public String getRawInput() {
        return rawInput;
    }

    public void setRawInput(String rawInput) {
        this.rawInput = rawInput;
    }

    public IntentType getType() {
        return type;
    }

    public void setType(IntentType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getServiceName() {
        if (parameters != null && parameters.containsKey("serviceName")) {
            return (String) parameters.get("serviceName");
        }
        return "unknown";
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public static class UserIntentBuilder {
        private String rawInput;
        private IntentType type;
        private String description;
        private Map<String, Object> parameters;
        private double confidence;
        private LocalDateTime timestamp;

        public UserIntentBuilder rawInput(String rawInput) {
            this.rawInput = rawInput;
            return this;
        }

        public UserIntentBuilder type(IntentType type) {
            this.type = type;
            return this;
        }

        public UserIntentBuilder description(String description) {
            this.description = description;
            return this;
        }

        public UserIntentBuilder parameters(Map<String, Object> parameters) {
            this.parameters = parameters;
            return this;
        }

        public UserIntentBuilder confidence(double confidence) {
            this.confidence = confidence;
            return this;
        }

        public UserIntentBuilder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public UserIntent build() {
            return new UserIntent(rawInput, type, description, parameters, confidence, timestamp);
        }
    }
} 