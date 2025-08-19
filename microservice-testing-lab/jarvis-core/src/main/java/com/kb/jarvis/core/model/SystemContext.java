package com.kb.jarvis.core.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SystemContext {
    private Map<String, Object> variables;
    private List<SystemEvent> recentEvents;
    private Map<String, Object> environment;
    private LocalDateTime lastUpdated;
    private UserIntent lastIntent;
    private LocalDateTime lastUpdate;
    private int activeEvents;

    // Default constructor
    public SystemContext() {}

    // Constructor with all fields
    public SystemContext(Map<String, Object> variables, List<SystemEvent> recentEvents, Map<String, Object> environment,
                        LocalDateTime lastUpdated, UserIntent lastIntent, LocalDateTime lastUpdate, int activeEvents) {
        this.variables = variables;
        this.recentEvents = recentEvents;
        this.environment = environment;
        this.lastUpdated = lastUpdated;
        this.lastIntent = lastIntent;
        this.lastUpdate = lastUpdate;
        this.activeEvents = activeEvents;
    }

    // Builder pattern
    public static SystemContextBuilder builder() {
        return new SystemContextBuilder();
    }

    // Getter methods
    public Map<String, Object> getVariables() { return variables; }
    public List<SystemEvent> getRecentEvents() { return recentEvents; }
    public Map<String, Object> getEnvironment() { return environment; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public UserIntent getLastIntent() { return lastIntent; }
    public LocalDateTime getLastUpdate() { return lastUpdate; }
    public int getActiveEvents() { return activeEvents; }

    // Setter methods
    public void setVariables(Map<String, Object> variables) { this.variables = variables; }
    public void setRecentEvents(List<SystemEvent> recentEvents) { this.recentEvents = recentEvents; }
    public void setEnvironment(Map<String, Object> environment) { this.environment = environment; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
    public void setLastIntent(UserIntent lastIntent) { this.lastIntent = lastIntent; }
    public void setLastUpdate(LocalDateTime lastUpdate) { this.lastUpdate = lastUpdate; }
    public void setActiveEvents(int activeEvents) { this.activeEvents = activeEvents; }

    // equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SystemContext that = (SystemContext) o;
        return activeEvents == that.activeEvents &&
                Objects.equals(variables, that.variables) &&
                Objects.equals(recentEvents, that.recentEvents) &&
                Objects.equals(environment, that.environment) &&
                Objects.equals(lastUpdated, that.lastUpdated) &&
                Objects.equals(lastIntent, that.lastIntent) &&
                Objects.equals(lastUpdate, that.lastUpdate);
    }

    // hashCode method
    @Override
    public int hashCode() {
        return Objects.hash(variables, recentEvents, environment, lastUpdated, lastIntent, lastUpdate, activeEvents);
    }

    // toString method
    @Override
    public String toString() {
        return "SystemContext{" +
                "variables=" + variables +
                ", recentEvents=" + recentEvents +
                ", environment=" + environment +
                ", lastUpdated=" + lastUpdated +
                ", lastIntent=" + lastIntent +
                ", lastUpdate=" + lastUpdate +
                ", activeEvents=" + activeEvents +
                '}';
    }

    // Builder class
    public static class SystemContextBuilder {
        private Map<String, Object> variables;
        private List<SystemEvent> recentEvents;
        private Map<String, Object> environment;
        private LocalDateTime lastUpdated;
        private UserIntent lastIntent;
        private LocalDateTime lastUpdate;
        private int activeEvents;

        public SystemContextBuilder variables(Map<String, Object> variables) { this.variables = variables; return this; }
        public SystemContextBuilder recentEvents(List<SystemEvent> recentEvents) { this.recentEvents = recentEvents; return this; }
        public SystemContextBuilder environment(Map<String, Object> environment) { this.environment = environment; return this; }
        public SystemContextBuilder lastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; return this; }
        public SystemContextBuilder lastIntent(UserIntent lastIntent) { this.lastIntent = lastIntent; return this; }
        public SystemContextBuilder lastUpdate(LocalDateTime lastUpdate) { this.lastUpdate = lastUpdate; return this; }
        public SystemContextBuilder activeEvents(int activeEvents) { this.activeEvents = activeEvents; return this; }

        public SystemContext build() {
            return new SystemContext(variables, recentEvents, environment, lastUpdated, lastIntent, lastUpdate, activeEvents);
        }
    }
} 