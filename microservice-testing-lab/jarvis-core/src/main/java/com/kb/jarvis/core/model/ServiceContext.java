package com.kb.jarvis.core.model;

import java.util.List;
import java.util.Map;

/**
 * Context information for a specific microservice
 * Contains metadata about service capabilities, dependencies, and testing requirements
 */
public class ServiceContext {
    
    private String serviceName;
    private int port;
    private String description;
    private List<String> dependencies;
    private List<String> endpoints;
    private List<TestType> supportedTestTypes;
    private String criticality;
    private Map<String, Object> configuration;
    private List<String> healthCheckEndpoints;
    private Map<String, Object> metrics;
    private List<String> supportedActions;
    private String deploymentType;
    private Map<String, Object> resourceRequirements;
    private List<String> environmentVariables;
    
    // Default constructor
    public ServiceContext() {}
    
    // Builder pattern
    public static ServiceContextBuilder builder() {
        return new ServiceContextBuilder();
    }
    
    // Getters
    public String getServiceName() { return serviceName; }
    public int getPort() { return port; }
    public String getDescription() { return description; }
    public List<String> getDependencies() { return dependencies; }
    public List<String> getEndpoints() { return endpoints; }
    public List<TestType> getSupportedTestTypes() { return supportedTestTypes; }
    public String getCriticality() { return criticality; }
    public Map<String, Object> getConfiguration() { return configuration; }
    public List<String> getHealthCheckEndpoints() { return healthCheckEndpoints; }
    public Map<String, Object> getMetrics() { return metrics; }
    public List<String> getSupportedActions() { return supportedActions; }
    public String getDeploymentType() { return deploymentType; }
    public Map<String, Object> getResourceRequirements() { return resourceRequirements; }
    public List<String> getEnvironmentVariables() { return environmentVariables; }
    
    // Setters
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public void setPort(int port) { this.port = port; }
    public void setDescription(String description) { this.description = description; }
    public void setDependencies(List<String> dependencies) { this.dependencies = dependencies; }
    public void setEndpoints(List<String> endpoints) { this.endpoints = endpoints; }
    public void setSupportedTestTypes(List<TestType> supportedTestTypes) { this.supportedTestTypes = supportedTestTypes; }
    public void setCriticality(String criticality) { this.criticality = criticality; }
    public void setConfiguration(Map<String, Object> configuration) { this.configuration = configuration; }
    public void setHealthCheckEndpoints(List<String> healthCheckEndpoints) { this.healthCheckEndpoints = healthCheckEndpoints; }
    public void setMetrics(Map<String, Object> metrics) { this.metrics = metrics; }
    public void setSupportedActions(List<String> supportedActions) { this.supportedActions = supportedActions; }
    public void setDeploymentType(String deploymentType) { this.deploymentType = deploymentType; }
    public void setResourceRequirements(Map<String, Object> resourceRequirements) { this.resourceRequirements = resourceRequirements; }
    public void setEnvironmentVariables(List<String> environmentVariables) { this.environmentVariables = environmentVariables; }
    
    // Builder class
    public static class ServiceContextBuilder {
        private String serviceName;
        private int port;
        private String description;
        private List<String> dependencies;
        private List<String> endpoints;
        private List<TestType> supportedTestTypes;
        private String criticality;
        private Map<String, Object> configuration;
        private List<String> healthCheckEndpoints;
        private Map<String, Object> metrics;
        private List<String> supportedActions;
        private String deploymentType;
        private Map<String, Object> resourceRequirements;
        private List<String> environmentVariables;
        
        public ServiceContextBuilder serviceName(String serviceName) { this.serviceName = serviceName; return this; }
        public ServiceContextBuilder port(int port) { this.port = port; return this; }
        public ServiceContextBuilder description(String description) { this.description = description; return this; }
        public ServiceContextBuilder dependencies(List<String> dependencies) { this.dependencies = dependencies; return this; }
        public ServiceContextBuilder endpoints(List<String> endpoints) { this.endpoints = endpoints; return this; }
        public ServiceContextBuilder supportedTestTypes(List<TestType> supportedTestTypes) { this.supportedTestTypes = supportedTestTypes; return this; }
        public ServiceContextBuilder criticality(String criticality) { this.criticality = criticality; return this; }
        public ServiceContextBuilder configuration(Map<String, Object> configuration) { this.configuration = configuration; return this; }
        public ServiceContextBuilder healthCheckEndpoints(List<String> healthCheckEndpoints) { this.healthCheckEndpoints = healthCheckEndpoints; return this; }
        public ServiceContextBuilder metrics(Map<String, Object> metrics) { this.metrics = metrics; return this; }
        public ServiceContextBuilder supportedActions(List<String> supportedActions) { this.supportedActions = supportedActions; return this; }
        public ServiceContextBuilder deploymentType(String deploymentType) { this.deploymentType = deploymentType; return this; }
        public ServiceContextBuilder resourceRequirements(Map<String, Object> resourceRequirements) { this.resourceRequirements = resourceRequirements; return this; }
        public ServiceContextBuilder environmentVariables(List<String> environmentVariables) { this.environmentVariables = environmentVariables; return this; }
        
        public ServiceContext build() {
            ServiceContext context = new ServiceContext();
            context.setServiceName(serviceName);
            context.setPort(port);
            context.setDescription(description);
            context.setDependencies(dependencies);
            context.setEndpoints(endpoints);
            context.setSupportedTestTypes(supportedTestTypes);
            context.setCriticality(criticality);
            context.setConfiguration(configuration);
            context.setHealthCheckEndpoints(healthCheckEndpoints);
            context.setMetrics(metrics);
            context.setSupportedActions(supportedActions);
            context.setDeploymentType(deploymentType);
            context.setResourceRequirements(resourceRequirements);
            context.setEnvironmentVariables(environmentVariables);
            return context;
        }
    }
}
