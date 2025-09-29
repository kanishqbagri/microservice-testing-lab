package com.kb.jarvis.core.model;

import java.util.List;
import java.util.Map;

/**
 * Represents an execution plan for a command
 * Contains execution steps, order, parameters, and scheduling information
 */
public class ExecutionPlan {
    
    private List<ExecutionStep> steps;
    private String executionOrder;
    private Map<String, Object> parameters;
    private String estimatedDuration;
    private List<String> prerequisites;
    private Map<String, Object> resourceAllocation;
    private List<String> checkpoints;
    private Map<String, Object> rollbackPlan;
    private String executionStrategy;
    
    // Default constructor
    public ExecutionPlan() {}
    
    // Builder pattern
    public static ExecutionPlanBuilder builder() {
        return new ExecutionPlanBuilder();
    }
    
    // Getters
    public List<ExecutionStep> getSteps() { return steps; }
    public String getExecutionOrder() { return executionOrder; }
    public Map<String, Object> getParameters() { return parameters; }
    public String getEstimatedDuration() { return estimatedDuration; }
    public List<String> getPrerequisites() { return prerequisites; }
    public Map<String, Object> getResourceAllocation() { return resourceAllocation; }
    public List<String> getCheckpoints() { return checkpoints; }
    public Map<String, Object> getRollbackPlan() { return rollbackPlan; }
    public String getExecutionStrategy() { return executionStrategy; }
    
    // Setters
    public void setSteps(List<ExecutionStep> steps) { this.steps = steps; }
    public void setExecutionOrder(String executionOrder) { this.executionOrder = executionOrder; }
    public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
    public void setEstimatedDuration(String estimatedDuration) { this.estimatedDuration = estimatedDuration; }
    public void setPrerequisites(List<String> prerequisites) { this.prerequisites = prerequisites; }
    public void setResourceAllocation(Map<String, Object> resourceAllocation) { this.resourceAllocation = resourceAllocation; }
    public void setCheckpoints(List<String> checkpoints) { this.checkpoints = checkpoints; }
    public void setRollbackPlan(Map<String, Object> rollbackPlan) { this.rollbackPlan = rollbackPlan; }
    public void setExecutionStrategy(String executionStrategy) { this.executionStrategy = executionStrategy; }
    
    // Builder class
    public static class ExecutionPlanBuilder {
        private List<ExecutionStep> steps;
        private String executionOrder;
        private Map<String, Object> parameters;
        private String estimatedDuration;
        private List<String> prerequisites;
        private Map<String, Object> resourceAllocation;
        private List<String> checkpoints;
        private Map<String, Object> rollbackPlan;
        private String executionStrategy;
        
        public ExecutionPlanBuilder steps(List<ExecutionStep> steps) { this.steps = steps; return this; }
        public ExecutionPlanBuilder executionOrder(String executionOrder) { this.executionOrder = executionOrder; return this; }
        public ExecutionPlanBuilder parameters(Map<String, Object> parameters) { this.parameters = parameters; return this; }
        public ExecutionPlanBuilder estimatedDuration(String estimatedDuration) { this.estimatedDuration = estimatedDuration; return this; }
        public ExecutionPlanBuilder prerequisites(List<String> prerequisites) { this.prerequisites = prerequisites; return this; }
        public ExecutionPlanBuilder resourceAllocation(Map<String, Object> resourceAllocation) { this.resourceAllocation = resourceAllocation; return this; }
        public ExecutionPlanBuilder checkpoints(List<String> checkpoints) { this.checkpoints = checkpoints; return this; }
        public ExecutionPlanBuilder rollbackPlan(Map<String, Object> rollbackPlan) { this.rollbackPlan = rollbackPlan; return this; }
        public ExecutionPlanBuilder executionStrategy(String executionStrategy) { this.executionStrategy = executionStrategy; return this; }
        
        public ExecutionPlan build() {
            ExecutionPlan plan = new ExecutionPlan();
            plan.setSteps(steps);
            plan.setExecutionOrder(executionOrder);
            plan.setParameters(parameters);
            plan.setEstimatedDuration(estimatedDuration);
            plan.setPrerequisites(prerequisites);
            plan.setResourceAllocation(resourceAllocation);
            plan.setCheckpoints(checkpoints);
            plan.setRollbackPlan(rollbackPlan);
            plan.setExecutionStrategy(executionStrategy);
            return plan;
        }
    }
}
