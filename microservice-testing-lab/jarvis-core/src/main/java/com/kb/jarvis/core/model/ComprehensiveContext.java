package com.kb.jarvis.core.model;

import java.util.List;
import java.util.Map;

/**
 * Comprehensive context that combines all aspects of a command
 * Contains parsed command, test types, services, actions, dependencies, and execution plan
 */
public class ComprehensiveContext {
    
    private String command;
    private ParsedCommand parsedCommand;
    private List<TestType> testTypes;
    private List<String> services;
    private List<ActionType> actions;
    private DependencyGraph dependencies;
    private ExecutionPlan executionPlan;
    private RiskAssessment riskAssessment;
    private ResourceRequirements resourceRequirements;
    private String estimatedDuration;
    private Map<String, Object> metadata;
    private List<String> warnings;
    private List<String> suggestions;
    private double confidence;
    
    // Default constructor
    public ComprehensiveContext() {}
    
    // Builder pattern
    public static ComprehensiveContextBuilder builder() {
        return new ComprehensiveContextBuilder();
    }
    
    // Getters
    public String getCommand() { return command; }
    public ParsedCommand getParsedCommand() { return parsedCommand; }
    public List<TestType> getTestTypes() { return testTypes; }
    public List<String> getServices() { return services; }
    public List<ActionType> getActions() { return actions; }
    public DependencyGraph getDependencies() { return dependencies; }
    public ExecutionPlan getExecutionPlan() { return executionPlan; }
    public RiskAssessment getRiskAssessment() { return riskAssessment; }
    public ResourceRequirements getResourceRequirements() { return resourceRequirements; }
    public String getEstimatedDuration() { return estimatedDuration; }
    public Map<String, Object> getMetadata() { return metadata; }
    public List<String> getWarnings() { return warnings; }
    public List<String> getSuggestions() { return suggestions; }
    public double getConfidence() { return confidence; }
    
    // Setters
    public void setCommand(String command) { this.command = command; }
    public void setParsedCommand(ParsedCommand parsedCommand) { this.parsedCommand = parsedCommand; }
    public void setTestTypes(List<TestType> testTypes) { this.testTypes = testTypes; }
    public void setServices(List<String> services) { this.services = services; }
    public void setActions(List<ActionType> actions) { this.actions = actions; }
    public void setDependencies(DependencyGraph dependencies) { this.dependencies = dependencies; }
    public void setExecutionPlan(ExecutionPlan executionPlan) { this.executionPlan = executionPlan; }
    public void setRiskAssessment(RiskAssessment riskAssessment) { this.riskAssessment = riskAssessment; }
    public void setResourceRequirements(ResourceRequirements resourceRequirements) { this.resourceRequirements = resourceRequirements; }
    public void setEstimatedDuration(String estimatedDuration) { this.estimatedDuration = estimatedDuration; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    public void setWarnings(List<String> warnings) { this.warnings = warnings; }
    public void setSuggestions(List<String> suggestions) { this.suggestions = suggestions; }
    public void setConfidence(double confidence) { this.confidence = confidence; }
    
    // Builder class
    public static class ComprehensiveContextBuilder {
        private String command;
        private ParsedCommand parsedCommand;
        private List<TestType> testTypes;
        private List<String> services;
        private List<ActionType> actions;
        private DependencyGraph dependencies;
        private ExecutionPlan executionPlan;
        private RiskAssessment riskAssessment;
        private ResourceRequirements resourceRequirements;
        private String estimatedDuration;
        private Map<String, Object> metadata;
        private List<String> warnings;
        private List<String> suggestions;
        private double confidence;
        
        public ComprehensiveContextBuilder command(String command) { this.command = command; return this; }
        public ComprehensiveContextBuilder parsedCommand(ParsedCommand parsedCommand) { this.parsedCommand = parsedCommand; return this; }
        public ComprehensiveContextBuilder testTypes(List<TestType> testTypes) { this.testTypes = testTypes; return this; }
        public ComprehensiveContextBuilder services(List<String> services) { this.services = services; return this; }
        public ComprehensiveContextBuilder actions(List<ActionType> actions) { this.actions = actions; return this; }
        public ComprehensiveContextBuilder dependencies(DependencyGraph dependencies) { this.dependencies = dependencies; return this; }
        public ComprehensiveContextBuilder executionPlan(ExecutionPlan executionPlan) { this.executionPlan = executionPlan; return this; }
        public ComprehensiveContextBuilder riskAssessment(RiskAssessment riskAssessment) { this.riskAssessment = riskAssessment; return this; }
        public ComprehensiveContextBuilder resourceRequirements(ResourceRequirements resourceRequirements) { this.resourceRequirements = resourceRequirements; return this; }
        public ComprehensiveContextBuilder estimatedDuration(String estimatedDuration) { this.estimatedDuration = estimatedDuration; return this; }
        public ComprehensiveContextBuilder metadata(Map<String, Object> metadata) { this.metadata = metadata; return this; }
        public ComprehensiveContextBuilder warnings(List<String> warnings) { this.warnings = warnings; return this; }
        public ComprehensiveContextBuilder suggestions(List<String> suggestions) { this.suggestions = suggestions; return this; }
        public ComprehensiveContextBuilder confidence(double confidence) { this.confidence = confidence; return this; }
        
        public ComprehensiveContext build() {
            ComprehensiveContext context = new ComprehensiveContext();
            context.setCommand(command);
            context.setParsedCommand(parsedCommand);
            context.setTestTypes(testTypes);
            context.setServices(services);
            context.setActions(actions);
            context.setDependencies(dependencies);
            context.setExecutionPlan(executionPlan);
            context.setRiskAssessment(riskAssessment);
            context.setResourceRequirements(resourceRequirements);
            context.setEstimatedDuration(estimatedDuration);
            context.setMetadata(metadata);
            context.setWarnings(warnings);
            context.setSuggestions(suggestions);
            context.setConfidence(confidence);
            return context;
        }
    }
}
