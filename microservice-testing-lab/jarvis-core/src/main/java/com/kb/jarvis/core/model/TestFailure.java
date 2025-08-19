package com.kb.jarvis.core.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class TestFailure {
    private String testId;
    private String testName;
    private String service;
    private String errorMessage;
    private String stackTrace;
    private LocalDateTime failureTime;
    private FailureType type;
    private double severity;
    private FailureType failureType;
    private String message;
    private String serviceName;
    private LocalDateTime timestamp;

    // Default constructor
    public TestFailure() {}

    // Constructor with all fields
    public TestFailure(String testId, String testName, String service, String errorMessage, String stackTrace,
                      LocalDateTime failureTime, FailureType type, double severity, FailureType failureType,
                      String message, String serviceName, LocalDateTime timestamp) {
        this.testId = testId;
        this.testName = testName;
        this.service = service;
        this.errorMessage = errorMessage;
        this.stackTrace = stackTrace;
        this.failureTime = failureTime;
        this.type = type;
        this.severity = severity;
        this.failureType = failureType;
        this.message = message;
        this.serviceName = serviceName;
        this.timestamp = timestamp;
    }

    // Builder pattern
    public static TestFailureBuilder builder() {
        return new TestFailureBuilder();
    }

    // Getter methods
    public String getTestId() { return testId; }
    public String getTestName() { return testName; }
    public String getService() { return service; }
    public String getErrorMessage() { return errorMessage; }
    public String getStackTrace() { return stackTrace; }
    public LocalDateTime getFailureTime() { return failureTime; }
    public FailureType getType() { return type; }
    public double getSeverity() { return severity; }
    public FailureType getFailureType() { return failureType; }
    public String getMessage() { return message; }
    public String getServiceName() { return serviceName; }
    public LocalDateTime getTimestamp() { return timestamp; }

    // Setter methods
    public void setTestId(String testId) { this.testId = testId; }
    public void setTestName(String testName) { this.testName = testName; }
    public void setService(String service) { this.service = service; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public void setStackTrace(String stackTrace) { this.stackTrace = stackTrace; }
    public void setFailureTime(LocalDateTime failureTime) { this.failureTime = failureTime; }
    public void setType(FailureType type) { this.type = type; }
    public void setSeverity(double severity) { this.severity = severity; }
    public void setFailureType(FailureType failureType) { this.failureType = failureType; }
    public void setMessage(String message) { this.message = message; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    // equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestFailure that = (TestFailure) o;
        return Double.compare(that.severity, severity) == 0 &&
                Objects.equals(testId, that.testId) &&
                Objects.equals(testName, that.testName) &&
                Objects.equals(service, that.service) &&
                Objects.equals(errorMessage, that.errorMessage) &&
                Objects.equals(stackTrace, that.stackTrace) &&
                Objects.equals(failureTime, that.failureTime) &&
                type == that.type &&
                failureType == that.failureType &&
                Objects.equals(message, that.message) &&
                Objects.equals(serviceName, that.serviceName) &&
                Objects.equals(timestamp, that.timestamp);
    }

    // hashCode method
    @Override
    public int hashCode() {
        return Objects.hash(testId, testName, service, errorMessage, stackTrace, failureTime, type, severity, failureType, message, serviceName, timestamp);
    }

    // toString method
    @Override
    public String toString() {
        return "TestFailure{" +
                "testId='" + testId + '\'' +
                ", testName='" + testName + '\'' +
                ", service='" + service + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", stackTrace='" + stackTrace + '\'' +
                ", failureTime=" + failureTime +
                ", type=" + type +
                ", severity=" + severity +
                ", failureType=" + failureType +
                ", message='" + message + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    // Builder class
    public static class TestFailureBuilder {
        private String testId;
        private String testName;
        private String service;
        private String errorMessage;
        private String stackTrace;
        private LocalDateTime failureTime;
        private FailureType type;
        private double severity;
        private FailureType failureType;
        private String message;
        private String serviceName;
        private LocalDateTime timestamp;

        public TestFailureBuilder testId(String testId) { this.testId = testId; return this; }
        public TestFailureBuilder testName(String testName) { this.testName = testName; return this; }
        public TestFailureBuilder service(String service) { this.service = service; return this; }
        public TestFailureBuilder errorMessage(String errorMessage) { this.errorMessage = errorMessage; return this; }
        public TestFailureBuilder stackTrace(String stackTrace) { this.stackTrace = stackTrace; return this; }
        public TestFailureBuilder failureTime(LocalDateTime failureTime) { this.failureTime = failureTime; return this; }
        public TestFailureBuilder type(FailureType type) { this.type = type; return this; }
        public TestFailureBuilder severity(double severity) { this.severity = severity; return this; }
        public TestFailureBuilder failureType(FailureType failureType) { this.failureType = failureType; return this; }
        public TestFailureBuilder message(String message) { this.message = message; return this; }
        public TestFailureBuilder serviceName(String serviceName) { this.serviceName = serviceName; return this; }
        public TestFailureBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }

        public TestFailure build() {
            return new TestFailure(testId, testName, service, errorMessage, stackTrace, failureTime, type, severity, failureType, message, serviceName, timestamp);
        }
    }
} 