package com.kb.jarvis.core.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SystemHealth {
    private HealthStatus overallStatus;
    private Map<String, ServiceHealth> serviceHealth;
    private List<String> issues;
    private LocalDateTime lastCheck;
    private HealthStatus status;
    private LocalDateTime timestamp;

    // Default constructor
    public SystemHealth() {}

    // Constructor with all fields
    public SystemHealth(HealthStatus overallStatus, Map<String, ServiceHealth> serviceHealth, List<String> issues,
                       LocalDateTime lastCheck, HealthStatus status, LocalDateTime timestamp) {
        this.overallStatus = overallStatus;
        this.serviceHealth = serviceHealth;
        this.issues = issues;
        this.lastCheck = lastCheck;
        this.status = status;
        this.timestamp = timestamp;
    }

    // Builder pattern
    public static SystemHealthBuilder builder() {
        return new SystemHealthBuilder();
    }

    // Getter methods
    public HealthStatus getOverallStatus() { return overallStatus; }
    public Map<String, ServiceHealth> getServiceHealth() { return serviceHealth; }
    public List<String> getIssues() { return issues; }
    public LocalDateTime getLastCheck() { return lastCheck; }
    public HealthStatus getStatus() { return status; }
    public LocalDateTime getTimestamp() { return timestamp; }

    // Setter methods
    public void setOverallStatus(HealthStatus overallStatus) { this.overallStatus = overallStatus; }
    public void setServiceHealth(Map<String, ServiceHealth> serviceHealth) { this.serviceHealth = serviceHealth; }
    public void setIssues(List<String> issues) { this.issues = issues; }
    public void setLastCheck(LocalDateTime lastCheck) { this.lastCheck = lastCheck; }
    public void setStatus(HealthStatus status) { this.status = status; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    // equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SystemHealth that = (SystemHealth) o;
        return Objects.equals(overallStatus, that.overallStatus) &&
                Objects.equals(serviceHealth, that.serviceHealth) &&
                Objects.equals(issues, that.issues) &&
                Objects.equals(lastCheck, that.lastCheck) &&
                Objects.equals(status, that.status) &&
                Objects.equals(timestamp, that.timestamp);
    }

    // hashCode method
    @Override
    public int hashCode() {
        return Objects.hash(overallStatus, serviceHealth, issues, lastCheck, status, timestamp);
    }

    // toString method
    @Override
    public String toString() {
        return "SystemHealth{" +
                "overallStatus=" + overallStatus +
                ", serviceHealth=" + serviceHealth +
                ", issues=" + issues +
                ", lastCheck=" + lastCheck +
                ", status=" + status +
                ", timestamp=" + timestamp +
                '}';
    }

    // Builder class
    public static class SystemHealthBuilder {
        private HealthStatus overallStatus;
        private Map<String, ServiceHealth> serviceHealth;
        private List<String> issues;
        private LocalDateTime lastCheck;
        private HealthStatus status;
        private LocalDateTime timestamp;

        public SystemHealthBuilder overallStatus(HealthStatus overallStatus) { this.overallStatus = overallStatus; return this; }
        public SystemHealthBuilder serviceHealth(Map<String, ServiceHealth> serviceHealth) { this.serviceHealth = serviceHealth; return this; }
        public SystemHealthBuilder issues(List<String> issues) { this.issues = issues; return this; }
        public SystemHealthBuilder lastCheck(LocalDateTime lastCheck) { this.lastCheck = lastCheck; return this; }
        public SystemHealthBuilder status(HealthStatus status) { this.status = status; return this; }
        public SystemHealthBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }

        public SystemHealth build() {
            return new SystemHealth(overallStatus, serviceHealth, issues, lastCheck, status, timestamp);
        }
    }
} 