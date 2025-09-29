package com.kb.jarvis.core.model;

import java.util.List;
import java.util.Map;

/**
 * Context information for a specific test type
 * Contains metadata about test execution characteristics, requirements, and capabilities
 */
public class TestTypeContext {
    
    private TestType testType;
    private String description;
    private List<String> tools;
    private String executionTime;
    private String resourceUsage;
    private List<String> dependencies;
    private String riskLevel;
    private boolean parallelizable;
    private Map<String, Object> parameters;
    private List<String> prerequisites;
    private String criticality;
    private List<String> supportedServices;
    private Map<String, Object> configuration;
    
    // Default constructor
    public TestTypeContext() {}
    
    // Builder pattern
    public static TestTypeContextBuilder builder() {
        return new TestTypeContextBuilder();
    }
    
    // Getters
    public TestType getTestType() { return testType; }
    public String getDescription() { return description; }
    public List<String> getTools() { return tools; }
    public String getExecutionTime() { return executionTime; }
    public String getResourceUsage() { return resourceUsage; }
    public List<String> getDependencies() { return dependencies; }
    public String getRiskLevel() { return riskLevel; }
    public boolean isParallelizable() { return parallelizable; }
    public Map<String, Object> getParameters() { return parameters; }
    public List<String> getPrerequisites() { return prerequisites; }
    public String getCriticality() { return criticality; }
    public List<String> getSupportedServices() { return supportedServices; }
    public Map<String, Object> getConfiguration() { return configuration; }
    
    // Setters
    public void setTestType(TestType testType) { this.testType = testType; }
    public void setDescription(String description) { this.description = description; }
    public void setTools(List<String> tools) { this.tools = tools; }
    public void setExecutionTime(String executionTime) { this.executionTime = executionTime; }
    public void setResourceUsage(String resourceUsage) { this.resourceUsage = resourceUsage; }
    public void setDependencies(List<String> dependencies) { this.dependencies = dependencies; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public void setParallelizable(boolean parallelizable) { this.parallelizable = parallelizable; }
    public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
    public void setPrerequisites(List<String> prerequisites) { this.prerequisites = prerequisites; }
    public void setCriticality(String criticality) { this.criticality = criticality; }
    public void setSupportedServices(List<String> supportedServices) { this.supportedServices = supportedServices; }
    public void setConfiguration(Map<String, Object> configuration) { this.configuration = configuration; }
    
    // Builder class
    public static class TestTypeContextBuilder {
        private TestType testType;
        private String description;
        private List<String> tools;
        private String executionTime;
        private String resourceUsage;
        private List<String> dependencies;
        private String riskLevel;
        private boolean parallelizable;
        private Map<String, Object> parameters;
        private List<String> prerequisites;
        private String criticality;
        private List<String> supportedServices;
        private Map<String, Object> configuration;
        
        public TestTypeContextBuilder testType(TestType testType) { this.testType = testType; return this; }
        public TestTypeContextBuilder description(String description) { this.description = description; return this; }
        public TestTypeContextBuilder tools(List<String> tools) { this.tools = tools; return this; }
        public TestTypeContextBuilder executionTime(String executionTime) { this.executionTime = executionTime; return this; }
        public TestTypeContextBuilder resourceUsage(String resourceUsage) { this.resourceUsage = resourceUsage; return this; }
        public TestTypeContextBuilder dependencies(List<String> dependencies) { this.dependencies = dependencies; return this; }
        public TestTypeContextBuilder riskLevel(String riskLevel) { this.riskLevel = riskLevel; return this; }
        public TestTypeContextBuilder parallelizable(boolean parallelizable) { this.parallelizable = parallelizable; return this; }
        public TestTypeContextBuilder parameters(Map<String, Object> parameters) { this.parameters = parameters; return this; }
        public TestTypeContextBuilder prerequisites(List<String> prerequisites) { this.prerequisites = prerequisites; return this; }
        public TestTypeContextBuilder criticality(String criticality) { this.criticality = criticality; return this; }
        public TestTypeContextBuilder supportedServices(List<String> supportedServices) { this.supportedServices = supportedServices; return this; }
        public TestTypeContextBuilder configuration(Map<String, Object> configuration) { this.configuration = configuration; return this; }
        
        public TestTypeContext build() {
            TestTypeContext context = new TestTypeContext();
            context.setTestType(testType);
            context.setDescription(description);
            context.setTools(tools);
            context.setExecutionTime(executionTime);
            context.setResourceUsage(resourceUsage);
            context.setDependencies(dependencies);
            context.setRiskLevel(riskLevel);
            context.setParallelizable(parallelizable);
            context.setParameters(parameters);
            context.setPrerequisites(prerequisites);
            context.setCriticality(criticality);
            context.setSupportedServices(supportedServices);
            context.setConfiguration(configuration);
            return context;
        }
    }
}
