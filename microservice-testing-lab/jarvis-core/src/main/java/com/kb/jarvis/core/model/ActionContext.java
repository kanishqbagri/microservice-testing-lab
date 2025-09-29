package com.kb.jarvis.core.model;

import java.util.List;
import java.util.Map;

/**
 * Context information for a specific action type
 * Contains metadata about action execution characteristics, requirements, and capabilities
 */
public class ActionContext {
    
    private ActionType actionType;
    private String description;
    private List<String> prerequisites;
    private List<String> supportedServices;
    private List<TestType> supportedTestTypes;
    private String executionTime;
    private String resourceUsage;
    private String riskLevel;
    private boolean parallelizable;
    private Map<String, Object> parameters;
    private List<String> dependencies;
    private String criticality;
    private Map<String, Object> configuration;
    private List<String> outputTypes;
    private Map<String, Object> sideEffects;
    
    // Default constructor
    public ActionContext() {}
    
    // Builder pattern
    public static ActionContextBuilder builder() {
        return new ActionContextBuilder();
    }
    
    // Getters
    public ActionType getActionType() { return actionType; }
    public String getDescription() { return description; }
    public List<String> getPrerequisites() { return prerequisites; }
    public List<String> getSupportedServices() { return supportedServices; }
    public List<TestType> getSupportedTestTypes() { return supportedTestTypes; }
    public String getExecutionTime() { return executionTime; }
    public String getResourceUsage() { return resourceUsage; }
    public String getRiskLevel() { return riskLevel; }
    public boolean isParallelizable() { return parallelizable; }
    public Map<String, Object> getParameters() { return parameters; }
    public List<String> getDependencies() { return dependencies; }
    public String getCriticality() { return criticality; }
    public Map<String, Object> getConfiguration() { return configuration; }
    public List<String> getOutputTypes() { return outputTypes; }
    public Map<String, Object> getSideEffects() { return sideEffects; }
    
    // Setters
    public void setActionType(ActionType actionType) { this.actionType = actionType; }
    public void setDescription(String description) { this.description = description; }
    public void setPrerequisites(List<String> prerequisites) { this.prerequisites = prerequisites; }
    public void setSupportedServices(List<String> supportedServices) { this.supportedServices = supportedServices; }
    public void setSupportedTestTypes(List<TestType> supportedTestTypes) { this.supportedTestTypes = supportedTestTypes; }
    public void setExecutionTime(String executionTime) { this.executionTime = executionTime; }
    public void setResourceUsage(String resourceUsage) { this.resourceUsage = resourceUsage; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public void setParallelizable(boolean parallelizable) { this.parallelizable = parallelizable; }
    public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
    public void setDependencies(List<String> dependencies) { this.dependencies = dependencies; }
    public void setCriticality(String criticality) { this.criticality = criticality; }
    public void setConfiguration(Map<String, Object> configuration) { this.configuration = configuration; }
    public void setOutputTypes(List<String> outputTypes) { this.outputTypes = outputTypes; }
    public void setSideEffects(Map<String, Object> sideEffects) { this.sideEffects = sideEffects; }
    
    // Builder class
    public static class ActionContextBuilder {
        private ActionType actionType;
        private String description;
        private List<String> prerequisites;
        private List<String> supportedServices;
        private List<TestType> supportedTestTypes;
        private String executionTime;
        private String resourceUsage;
        private String riskLevel;
        private boolean parallelizable;
        private Map<String, Object> parameters;
        private List<String> dependencies;
        private String criticality;
        private Map<String, Object> configuration;
        private List<String> outputTypes;
        private Map<String, Object> sideEffects;
        
        public ActionContextBuilder actionType(ActionType actionType) { this.actionType = actionType; return this; }
        public ActionContextBuilder description(String description) { this.description = description; return this; }
        public ActionContextBuilder prerequisites(List<String> prerequisites) { this.prerequisites = prerequisites; return this; }
        public ActionContextBuilder supportedServices(List<String> supportedServices) { this.supportedServices = supportedServices; return this; }
        public ActionContextBuilder supportedTestTypes(List<TestType> supportedTestTypes) { this.supportedTestTypes = supportedTestTypes; return this; }
        public ActionContextBuilder executionTime(String executionTime) { this.executionTime = executionTime; return this; }
        public ActionContextBuilder resourceUsage(String resourceUsage) { this.resourceUsage = resourceUsage; return this; }
        public ActionContextBuilder riskLevel(String riskLevel) { this.riskLevel = riskLevel; return this; }
        public ActionContextBuilder parallelizable(boolean parallelizable) { this.parallelizable = parallelizable; return this; }
        public ActionContextBuilder parameters(Map<String, Object> parameters) { this.parameters = parameters; return this; }
        public ActionContextBuilder dependencies(List<String> dependencies) { this.dependencies = dependencies; return this; }
        public ActionContextBuilder criticality(String criticality) { this.criticality = criticality; return this; }
        public ActionContextBuilder configuration(Map<String, Object> configuration) { this.configuration = configuration; return this; }
        public ActionContextBuilder outputTypes(List<String> outputTypes) { this.outputTypes = outputTypes; return this; }
        public ActionContextBuilder sideEffects(Map<String, Object> sideEffects) { this.sideEffects = sideEffects; return this; }
        
        public ActionContext build() {
            ActionContext context = new ActionContext();
            context.setActionType(actionType);
            context.setDescription(description);
            context.setPrerequisites(prerequisites);
            context.setSupportedServices(supportedServices);
            context.setSupportedTestTypes(supportedTestTypes);
            context.setExecutionTime(executionTime);
            context.setResourceUsage(resourceUsage);
            context.setRiskLevel(riskLevel);
            context.setParallelizable(parallelizable);
            context.setParameters(parameters);
            context.setDependencies(dependencies);
            context.setCriticality(criticality);
            context.setConfiguration(configuration);
            context.setOutputTypes(outputTypes);
            context.setSideEffects(sideEffects);
            return context;
        }
    }
}
