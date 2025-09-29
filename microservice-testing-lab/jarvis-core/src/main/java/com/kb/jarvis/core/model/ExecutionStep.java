package com.kb.jarvis.core.model;

import java.util.List;
import java.util.Map;

/**
 * Represents a single step in an execution plan
 * Contains step details, dependencies, and execution information
 */
public class ExecutionStep {
    
    private String stepId;
    private String stepName;
    private ActionType actionType;
    private String serviceName;
    private TestType testType;
    private Map<String, Object> parameters;
    private List<String> dependencies;
    private String estimatedDuration;
    private String status;
    private Map<String, Object> output;
    private List<String> prerequisites;
    
    // Default constructor
    public ExecutionStep() {}
    
    // Builder pattern
    public static ExecutionStepBuilder builder() {
        return new ExecutionStepBuilder();
    }
    
    // Getters
    public String getStepId() { return stepId; }
    public String getStepName() { return stepName; }
    public ActionType getActionType() { return actionType; }
    public String getServiceName() { return serviceName; }
    public TestType getTestType() { return testType; }
    public Map<String, Object> getParameters() { return parameters; }
    public List<String> getDependencies() { return dependencies; }
    public String getEstimatedDuration() { return estimatedDuration; }
    public String getStatus() { return status; }
    public Map<String, Object> getOutput() { return output; }
    public List<String> getPrerequisites() { return prerequisites; }
    
    // Setters
    public void setStepId(String stepId) { this.stepId = stepId; }
    public void setStepName(String stepName) { this.stepName = stepName; }
    public void setActionType(ActionType actionType) { this.actionType = actionType; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public void setTestType(TestType testType) { this.testType = testType; }
    public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
    public void setDependencies(List<String> dependencies) { this.dependencies = dependencies; }
    public void setEstimatedDuration(String estimatedDuration) { this.estimatedDuration = estimatedDuration; }
    public void setStatus(String status) { this.status = status; }
    public void setOutput(Map<String, Object> output) { this.output = output; }
    public void setPrerequisites(List<String> prerequisites) { this.prerequisites = prerequisites; }
    
    // Builder class
    public static class ExecutionStepBuilder {
        private String stepId;
        private String stepName;
        private ActionType actionType;
        private String serviceName;
        private TestType testType;
        private Map<String, Object> parameters;
        private List<String> dependencies;
        private String estimatedDuration;
        private String status;
        private Map<String, Object> output;
        private List<String> prerequisites;
        
        public ExecutionStepBuilder stepId(String stepId) { this.stepId = stepId; return this; }
        public ExecutionStepBuilder stepName(String stepName) { this.stepName = stepName; return this; }
        public ExecutionStepBuilder actionType(ActionType actionType) { this.actionType = actionType; return this; }
        public ExecutionStepBuilder serviceName(String serviceName) { this.serviceName = serviceName; return this; }
        public ExecutionStepBuilder testType(TestType testType) { this.testType = testType; return this; }
        public ExecutionStepBuilder parameters(Map<String, Object> parameters) { this.parameters = parameters; return this; }
        public ExecutionStepBuilder dependencies(List<String> dependencies) { this.dependencies = dependencies; return this; }
        public ExecutionStepBuilder estimatedDuration(String estimatedDuration) { this.estimatedDuration = estimatedDuration; return this; }
        public ExecutionStepBuilder status(String status) { this.status = status; return this; }
        public ExecutionStepBuilder output(Map<String, Object> output) { this.output = output; return this; }
        public ExecutionStepBuilder prerequisites(List<String> prerequisites) { this.prerequisites = prerequisites; return this; }
        
        public ExecutionStep build() {
            ExecutionStep step = new ExecutionStep();
            step.setStepId(stepId);
            step.setStepName(stepName);
            step.setActionType(actionType);
            step.setServiceName(serviceName);
            step.setTestType(testType);
            step.setParameters(parameters);
            step.setDependencies(dependencies);
            step.setEstimatedDuration(estimatedDuration);
            step.setStatus(status);
            step.setOutput(output);
            step.setPrerequisites(prerequisites);
            return step;
        }
    }
}
