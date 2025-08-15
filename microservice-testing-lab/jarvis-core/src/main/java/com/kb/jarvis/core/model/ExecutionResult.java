package com.kb.jarvis.core.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ExecutionResult {
    private boolean success;
    private String message;
    private Map<String, Object> data;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private long duration;
    private List<String> errors;
    private List<String> warnings;

    public ExecutionResult() {
    }

    public ExecutionResult(boolean success, String message, Map<String, Object> data, LocalDateTime startTime, LocalDateTime endTime, long duration, List<String> errors, List<String> warnings) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.errors = errors;
        this.warnings = warnings;
    }

    public static ExecutionResultBuilder builder() {
        return new ExecutionResultBuilder();
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }

    public static class ExecutionResultBuilder {
        private boolean success;
        private String message;
        private Map<String, Object> data;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private long duration;
        private List<String> errors;
        private List<String> warnings;

        public ExecutionResultBuilder success(boolean success) {
            this.success = success;
            return this;
        }

        public ExecutionResultBuilder message(String message) {
            this.message = message;
            return this;
        }

        public ExecutionResultBuilder data(Map<String, Object> data) {
            this.data = data;
            return this;
        }

        public ExecutionResultBuilder startTime(LocalDateTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public ExecutionResultBuilder endTime(LocalDateTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public ExecutionResultBuilder duration(long duration) {
            this.duration = duration;
            return this;
        }

        public ExecutionResultBuilder errors(List<String> errors) {
            this.errors = errors;
            return this;
        }

        public ExecutionResultBuilder warnings(List<String> warnings) {
            this.warnings = warnings;
            return this;
        }

        public ExecutionResult build() {
            return new ExecutionResult(success, message, data, startTime, endTime, duration, errors, warnings);
        }
    }
} 