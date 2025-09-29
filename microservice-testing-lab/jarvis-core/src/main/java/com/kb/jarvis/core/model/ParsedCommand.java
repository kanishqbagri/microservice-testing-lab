package com.kb.jarvis.core.model;

import java.util.List;
import java.util.Map;

/**
 * Represents a parsed natural language command
 * Contains extracted intents, services, test types, parameters, and context
 */
public class ParsedCommand {
    
    private String originalCommand;
    private List<IntentType> intents;
    private List<String> services;
    private List<TestType> testTypes;
    private Map<String, Object> parameters;
    private CommandContext context;
    private double confidence;
    
    // Default constructor
    public ParsedCommand() {}
    
    // Builder pattern
    public static ParsedCommandBuilder builder() {
        return new ParsedCommandBuilder();
    }
    
    // Getters
    public String getOriginalCommand() { return originalCommand; }
    public List<IntentType> getIntents() { return intents; }
    public List<String> getServices() { return services; }
    public List<TestType> getTestTypes() { return testTypes; }
    public Map<String, Object> getParameters() { return parameters; }
    public CommandContext getContext() { return context; }
    public double getConfidence() { return confidence; }
    
    // Setters
    public void setOriginalCommand(String originalCommand) { this.originalCommand = originalCommand; }
    public void setIntents(List<IntentType> intents) { this.intents = intents; }
    public void setServices(List<String> services) { this.services = services; }
    public void setTestTypes(List<TestType> testTypes) { this.testTypes = testTypes; }
    public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
    public void setContext(CommandContext context) { this.context = context; }
    public void setConfidence(double confidence) { this.confidence = confidence; }
    
    // Builder class
    public static class ParsedCommandBuilder {
        private String originalCommand;
        private List<IntentType> intents;
        private List<String> services;
        private List<TestType> testTypes;
        private Map<String, Object> parameters;
        private CommandContext context;
        private double confidence;
        
        public ParsedCommandBuilder originalCommand(String originalCommand) { this.originalCommand = originalCommand; return this; }
        public ParsedCommandBuilder intents(List<IntentType> intents) { this.intents = intents; return this; }
        public ParsedCommandBuilder services(List<String> services) { this.services = services; return this; }
        public ParsedCommandBuilder testTypes(List<TestType> testTypes) { this.testTypes = testTypes; return this; }
        public ParsedCommandBuilder parameters(Map<String, Object> parameters) { this.parameters = parameters; return this; }
        public ParsedCommandBuilder context(CommandContext context) { this.context = context; return this; }
        public ParsedCommandBuilder confidence(double confidence) { this.confidence = confidence; return this; }
        
        public ParsedCommand build() {
            ParsedCommand command = new ParsedCommand();
            command.setOriginalCommand(originalCommand);
            command.setIntents(intents);
            command.setServices(services);
            command.setTestTypes(testTypes);
            command.setParameters(parameters);
            command.setContext(context);
            command.setConfidence(confidence);
            return command;
        }
    }
}
