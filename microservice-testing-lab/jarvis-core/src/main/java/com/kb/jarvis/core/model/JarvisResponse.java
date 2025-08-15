package com.kb.jarvis.core.model;

import java.time.LocalDateTime;
import java.util.Map;

public class JarvisResponse {
    private String message;
    private JarvisResponseStatus status;
    private DecisionAction action;
    private Map<String, Object> data;
    private LocalDateTime timestamp;

    public JarvisResponse() {
    }

    public JarvisResponse(String message, JarvisResponseStatus status, DecisionAction action, Map<String, Object> data, LocalDateTime timestamp) {
        this.message = message;
        this.status = status;
        this.action = action;
        this.data = data;
        this.timestamp = timestamp;
    }

    public static JarvisResponseBuilder builder() {
        return new JarvisResponseBuilder();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JarvisResponseStatus getStatus() {
        return status;
    }

    public void setStatus(JarvisResponseStatus status) {
        this.status = status;
    }

    public DecisionAction getAction() {
        return action;
    }

    public void setAction(DecisionAction action) {
        this.action = action;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public static class JarvisResponseBuilder {
        private String message;
        private JarvisResponseStatus status;
        private DecisionAction action;
        private Map<String, Object> data;
        private LocalDateTime timestamp;

        public JarvisResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public JarvisResponseBuilder status(JarvisResponseStatus status) {
            this.status = status;
            return this;
        }

        public JarvisResponseBuilder action(DecisionAction action) {
            this.action = action;
            return this;
        }

        public JarvisResponseBuilder data(Map<String, Object> data) {
            this.data = data;
            return this;
        }

        public JarvisResponseBuilder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public JarvisResponse build() {
            return new JarvisResponse(message, status, action, data, timestamp);
        }
    }
} 