package com.kb.jarvis.core.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class ServiceHealth {
    private String serviceName;
    private HealthStatus status;
    private double responseTime;
    private double availability;
    private List<String> issues;
    private String message;
    private LocalDateTime timestamp;

    // Default constructor
    public ServiceHealth() {}

    // Constructor with all fields
    public ServiceHealth(String serviceName, HealthStatus status, double responseTime, double availability,
                        List<String> issues, String message, LocalDateTime timestamp) {
        this.serviceName = serviceName;
        this.status = status;
        this.responseTime = responseTime;
        this.availability = availability;
        this.issues = issues;
        this.message = message;
        this.timestamp = timestamp;
    }

    // Builder pattern
    public static ServiceHealthBuilder builder() {
        return new ServiceHealthBuilder();
    }

    // Getter methods
    public String getServiceName() { return serviceName; }
    public HealthStatus getStatus() { return status; }
    public double getResponseTime() { return responseTime; }
    public double getAvailability() { return availability; }
    public List<String> getIssues() { return issues; }
    public String getMessage() { return message; }
    public LocalDateTime getTimestamp() { return timestamp; }

    // Setter methods
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public void setStatus(HealthStatus status) { this.status = status; }
    public void setResponseTime(double responseTime) { this.responseTime = responseTime; }
    public void setAvailability(double availability) { this.availability = availability; }
    public void setIssues(List<String> issues) { this.issues = issues; }
    public void setMessage(String message) { this.message = message; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    // equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceHealth that = (ServiceHealth) o;
        return Double.compare(that.responseTime, responseTime) == 0 &&
                Double.compare(that.availability, availability) == 0 &&
                Objects.equals(serviceName, that.serviceName) &&
                status == that.status &&
                Objects.equals(issues, that.issues) &&
                Objects.equals(message, that.message) &&
                Objects.equals(timestamp, that.timestamp);
    }

    // hashCode method
    @Override
    public int hashCode() {
        return Objects.hash(serviceName, status, responseTime, availability, issues, message, timestamp);
    }

    // toString method
    @Override
    public String toString() {
        return "ServiceHealth{" +
                "serviceName='" + serviceName + '\'' +
                ", status=" + status +
                ", responseTime=" + responseTime +
                ", availability=" + availability +
                ", issues=" + issues +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    // Builder class
    public static class ServiceHealthBuilder {
        private String serviceName;
        private HealthStatus status;
        private double responseTime;
        private double availability;
        private List<String> issues;
        private String message;
        private LocalDateTime timestamp;

        public ServiceHealthBuilder serviceName(String serviceName) { this.serviceName = serviceName; return this; }
        public ServiceHealthBuilder status(HealthStatus status) { this.status = status; return this; }
        public ServiceHealthBuilder responseTime(double responseTime) { this.responseTime = responseTime; return this; }
        public ServiceHealthBuilder availability(double availability) { this.availability = availability; return this; }
        public ServiceHealthBuilder issues(List<String> issues) { this.issues = issues; return this; }
        public ServiceHealthBuilder message(String message) { this.message = message; return this; }
        public ServiceHealthBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }

        public ServiceHealth build() {
            return new ServiceHealth(serviceName, status, responseTime, availability, issues, message, timestamp);
        }
    }
} 