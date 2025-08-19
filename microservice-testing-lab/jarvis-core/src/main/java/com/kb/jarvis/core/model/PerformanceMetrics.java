package com.kb.jarvis.core.model;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

public class PerformanceMetrics {
    private double averageResponseTime;
    private double throughput;
    private double errorRate;
    private double cpuUsage;
    private double memoryUsage;
    private double diskUsage;
    private double networkLatency;
    private LocalDateTime timestamp;
    private Map<String, Double> customMetrics;

    // Default constructor
    public PerformanceMetrics() {}

    // Constructor with all fields
    public PerformanceMetrics(double averageResponseTime, double throughput, double errorRate, double cpuUsage,
                             double memoryUsage, double diskUsage, double networkLatency, LocalDateTime timestamp,
                             Map<String, Double> customMetrics) {
        this.averageResponseTime = averageResponseTime;
        this.throughput = throughput;
        this.errorRate = errorRate;
        this.cpuUsage = cpuUsage;
        this.memoryUsage = memoryUsage;
        this.diskUsage = diskUsage;
        this.networkLatency = networkLatency;
        this.timestamp = timestamp;
        this.customMetrics = customMetrics;
    }

    // Builder pattern
    public static PerformanceMetricsBuilder builder() {
        return new PerformanceMetricsBuilder();
    }

    // Getter methods
    public double getAverageResponseTime() { return averageResponseTime; }
    public double getThroughput() { return throughput; }
    public double getErrorRate() { return errorRate; }
    public double getCpuUsage() { return cpuUsage; }
    public double getMemoryUsage() { return memoryUsage; }
    public double getDiskUsage() { return diskUsage; }
    public double getNetworkLatency() { return networkLatency; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public Map<String, Double> getCustomMetrics() { return customMetrics; }

    // Setter methods
    public void setAverageResponseTime(double averageResponseTime) { this.averageResponseTime = averageResponseTime; }
    public void setThroughput(double throughput) { this.throughput = throughput; }
    public void setErrorRate(double errorRate) { this.errorRate = errorRate; }
    public void setCpuUsage(double cpuUsage) { this.cpuUsage = cpuUsage; }
    public void setMemoryUsage(double memoryUsage) { this.memoryUsage = memoryUsage; }
    public void setDiskUsage(double diskUsage) { this.diskUsage = diskUsage; }
    public void setNetworkLatency(double networkLatency) { this.networkLatency = networkLatency; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public void setCustomMetrics(Map<String, Double> customMetrics) { this.customMetrics = customMetrics; }

    // equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PerformanceMetrics that = (PerformanceMetrics) o;
        return Double.compare(that.averageResponseTime, averageResponseTime) == 0 &&
                Double.compare(that.throughput, throughput) == 0 &&
                Double.compare(that.errorRate, errorRate) == 0 &&
                Double.compare(that.cpuUsage, cpuUsage) == 0 &&
                Double.compare(that.memoryUsage, memoryUsage) == 0 &&
                Double.compare(that.diskUsage, diskUsage) == 0 &&
                Double.compare(that.networkLatency, networkLatency) == 0 &&
                Objects.equals(timestamp, that.timestamp) &&
                Objects.equals(customMetrics, that.customMetrics);
    }

    // hashCode method
    @Override
    public int hashCode() {
        return Objects.hash(averageResponseTime, throughput, errorRate, cpuUsage, memoryUsage, diskUsage, networkLatency, timestamp, customMetrics);
    }

    // toString method
    @Override
    public String toString() {
        return "PerformanceMetrics{" +
                "averageResponseTime=" + averageResponseTime +
                ", throughput=" + throughput +
                ", errorRate=" + errorRate +
                ", cpuUsage=" + cpuUsage +
                ", memoryUsage=" + memoryUsage +
                ", diskUsage=" + diskUsage +
                ", networkLatency=" + networkLatency +
                ", timestamp=" + timestamp +
                ", customMetrics=" + customMetrics +
                '}';
    }

    // Builder class
    public static class PerformanceMetricsBuilder {
        private double averageResponseTime;
        private double throughput;
        private double errorRate;
        private double cpuUsage;
        private double memoryUsage;
        private double diskUsage;
        private double networkLatency;
        private LocalDateTime timestamp;
        private Map<String, Double> customMetrics;

        public PerformanceMetricsBuilder averageResponseTime(double averageResponseTime) { this.averageResponseTime = averageResponseTime; return this; }
        public PerformanceMetricsBuilder throughput(double throughput) { this.throughput = throughput; return this; }
        public PerformanceMetricsBuilder errorRate(double errorRate) { this.errorRate = errorRate; return this; }
        public PerformanceMetricsBuilder cpuUsage(double cpuUsage) { this.cpuUsage = cpuUsage; return this; }
        public PerformanceMetricsBuilder memoryUsage(double memoryUsage) { this.memoryUsage = memoryUsage; return this; }
        public PerformanceMetricsBuilder diskUsage(double diskUsage) { this.diskUsage = diskUsage; return this; }
        public PerformanceMetricsBuilder networkLatency(double networkLatency) { this.networkLatency = networkLatency; return this; }
        public PerformanceMetricsBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }
        public PerformanceMetricsBuilder customMetrics(Map<String, Double> customMetrics) { this.customMetrics = customMetrics; return this; }

        public PerformanceMetrics build() {
            return new PerformanceMetrics(averageResponseTime, throughput, errorRate, cpuUsage, memoryUsage, diskUsage, networkLatency, timestamp, customMetrics);
        }
    }
} 