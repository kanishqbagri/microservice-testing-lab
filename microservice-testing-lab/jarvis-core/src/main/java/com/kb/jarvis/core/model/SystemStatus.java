package com.kb.jarvis.core.model;

import java.time.LocalDateTime;
import java.util.List;

public class SystemStatus {
    private List<ActiveTest> activeTests;
    private List<TestFailure> recentFailures;
    private SystemHealth systemHealth;
    private PerformanceMetrics performanceMetrics;
    private LocalDateTime lastUpdated;

    public SystemStatus() {
    }

    public SystemStatus(List<ActiveTest> activeTests, List<TestFailure> recentFailures, SystemHealth systemHealth, PerformanceMetrics performanceMetrics, LocalDateTime lastUpdated) {
        this.activeTests = activeTests;
        this.recentFailures = recentFailures;
        this.systemHealth = systemHealth;
        this.performanceMetrics = performanceMetrics;
        this.lastUpdated = lastUpdated;
    }

    public static SystemStatusBuilder builder() {
        return new SystemStatusBuilder();
    }

    public List<ActiveTest> getActiveTests() {
        return activeTests;
    }

    public void setActiveTests(List<ActiveTest> activeTests) {
        this.activeTests = activeTests;
    }

    public List<TestFailure> getRecentFailures() {
        return recentFailures;
    }

    public void setRecentFailures(List<TestFailure> recentFailures) {
        this.recentFailures = recentFailures;
    }

    public SystemHealth getSystemHealth() {
        return systemHealth;
    }

    public void setSystemHealth(SystemHealth systemHealth) {
        this.systemHealth = systemHealth;
    }

    public PerformanceMetrics getPerformanceMetrics() {
        return performanceMetrics;
    }

    public void setPerformanceMetrics(PerformanceMetrics performanceMetrics) {
        this.performanceMetrics = performanceMetrics;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public static class SystemStatusBuilder {
        private List<ActiveTest> activeTests;
        private List<TestFailure> recentFailures;
        private SystemHealth systemHealth;
        private PerformanceMetrics performanceMetrics;
        private LocalDateTime lastUpdated;

        public SystemStatusBuilder activeTests(List<ActiveTest> activeTests) {
            this.activeTests = activeTests;
            return this;
        }

        public SystemStatusBuilder recentFailures(List<TestFailure> recentFailures) {
            this.recentFailures = recentFailures;
            return this;
        }

        public SystemStatusBuilder systemHealth(SystemHealth systemHealth) {
            this.systemHealth = systemHealth;
            return this;
        }

        public SystemStatusBuilder performanceMetrics(PerformanceMetrics performanceMetrics) {
            this.performanceMetrics = performanceMetrics;
            return this;
        }

        public SystemStatusBuilder lastUpdated(LocalDateTime lastUpdated) {
            this.lastUpdated = lastUpdated;
            return this;
        }

        public SystemStatus build() {
            return new SystemStatus(activeTests, recentFailures, systemHealth, performanceMetrics, lastUpdated);
        }
    }
} 