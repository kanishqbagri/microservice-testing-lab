package com.kb.jarvis.core.model;

import java.util.List;
import java.util.Map;

/**
 * Context information for a command
 * Contains urgency, scope, priority, timing, and other contextual information
 */
public class CommandContext {
    
    private String urgency;
    private String scope;
    private String priority;
    private String timing;
    private List<String> constraints;
    private Map<String, Object> environment;
    private String userRole;
    private String executionMode;
    private List<String> exclusions;
    private Map<String, Object> preferences;
    
    // Default constructor
    public CommandContext() {}
    
    // Builder pattern
    public static CommandContextBuilder builder() {
        return new CommandContextBuilder();
    }
    
    // Getters
    public String getUrgency() { return urgency; }
    public String getScope() { return scope; }
    public String getPriority() { return priority; }
    public String getTiming() { return timing; }
    public List<String> getConstraints() { return constraints; }
    public Map<String, Object> getEnvironment() { return environment; }
    public String getUserRole() { return userRole; }
    public String getExecutionMode() { return executionMode; }
    public List<String> getExclusions() { return exclusions; }
    public Map<String, Object> getPreferences() { return preferences; }
    
    // Setters
    public void setUrgency(String urgency) { this.urgency = urgency; }
    public void setScope(String scope) { this.scope = scope; }
    public void setPriority(String priority) { this.priority = priority; }
    public void setTiming(String timing) { this.timing = timing; }
    public void setConstraints(List<String> constraints) { this.constraints = constraints; }
    public void setEnvironment(Map<String, Object> environment) { this.environment = environment; }
    public void setUserRole(String userRole) { this.userRole = userRole; }
    public void setExecutionMode(String executionMode) { this.executionMode = executionMode; }
    public void setExclusions(List<String> exclusions) { this.exclusions = exclusions; }
    public void setPreferences(Map<String, Object> preferences) { this.preferences = preferences; }
    
    // Builder class
    public static class CommandContextBuilder {
        private String urgency;
        private String scope;
        private String priority;
        private String timing;
        private List<String> constraints;
        private Map<String, Object> environment;
        private String userRole;
        private String executionMode;
        private List<String> exclusions;
        private Map<String, Object> preferences;
        
        public CommandContextBuilder urgency(String urgency) { this.urgency = urgency; return this; }
        public CommandContextBuilder scope(String scope) { this.scope = scope; return this; }
        public CommandContextBuilder priority(String priority) { this.priority = priority; return this; }
        public CommandContextBuilder timing(String timing) { this.timing = timing; return this; }
        public CommandContextBuilder constraints(List<String> constraints) { this.constraints = constraints; return this; }
        public CommandContextBuilder environment(Map<String, Object> environment) { this.environment = environment; return this; }
        public CommandContextBuilder userRole(String userRole) { this.userRole = userRole; return this; }
        public CommandContextBuilder executionMode(String executionMode) { this.executionMode = executionMode; return this; }
        public CommandContextBuilder exclusions(List<String> exclusions) { this.exclusions = exclusions; return this; }
        public CommandContextBuilder preferences(Map<String, Object> preferences) { this.preferences = preferences; return this; }
        
        public CommandContext build() {
            CommandContext context = new CommandContext();
            context.setUrgency(urgency);
            context.setScope(scope);
            context.setPriority(priority);
            context.setTiming(timing);
            context.setConstraints(constraints);
            context.setEnvironment(environment);
            context.setUserRole(userRole);
            context.setExecutionMode(executionMode);
            context.setExclusions(exclusions);
            context.setPreferences(preferences);
            return context;
        }
    }
}
