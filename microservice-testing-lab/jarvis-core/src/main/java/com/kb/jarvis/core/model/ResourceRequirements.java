package com.kb.jarvis.core.model;

import java.util.List;
import java.util.Map;

/**
 * Represents resource requirements for command execution
 * Contains CPU, memory, storage, and other resource specifications
 */
public class ResourceRequirements {
    
    private String cpuRequirements;
    private String memoryRequirements;
    private String storageRequirements;
    private String networkRequirements;
    private List<String> externalDependencies;
    private Map<String, Object> resourceLimits;
    private Map<String, Object> resourceAllocation;
    private String priority;
    private List<String> constraints;
    
    // Default constructor
    public ResourceRequirements() {}
    
    // Builder pattern
    public static ResourceRequirementsBuilder builder() {
        return new ResourceRequirementsBuilder();
    }
    
    // Getters
    public String getCpuRequirements() { return cpuRequirements; }
    public String getMemoryRequirements() { return memoryRequirements; }
    public String getStorageRequirements() { return storageRequirements; }
    public String getNetworkRequirements() { return networkRequirements; }
    public List<String> getExternalDependencies() { return externalDependencies; }
    public Map<String, Object> getResourceLimits() { return resourceLimits; }
    public Map<String, Object> getResourceAllocation() { return resourceAllocation; }
    public String getPriority() { return priority; }
    public List<String> getConstraints() { return constraints; }
    
    // Setters
    public void setCpuRequirements(String cpuRequirements) { this.cpuRequirements = cpuRequirements; }
    public void setMemoryRequirements(String memoryRequirements) { this.memoryRequirements = memoryRequirements; }
    public void setStorageRequirements(String storageRequirements) { this.storageRequirements = storageRequirements; }
    public void setNetworkRequirements(String networkRequirements) { this.networkRequirements = networkRequirements; }
    public void setExternalDependencies(List<String> externalDependencies) { this.externalDependencies = externalDependencies; }
    public void setResourceLimits(Map<String, Object> resourceLimits) { this.resourceLimits = resourceLimits; }
    public void setResourceAllocation(Map<String, Object> resourceAllocation) { this.resourceAllocation = resourceAllocation; }
    public void setPriority(String priority) { this.priority = priority; }
    public void setConstraints(List<String> constraints) { this.constraints = constraints; }
    
    // Builder class
    public static class ResourceRequirementsBuilder {
        private String cpuRequirements;
        private String memoryRequirements;
        private String storageRequirements;
        private String networkRequirements;
        private List<String> externalDependencies;
        private Map<String, Object> resourceLimits;
        private Map<String, Object> resourceAllocation;
        private String priority;
        private List<String> constraints;
        
        public ResourceRequirementsBuilder cpuRequirements(String cpuRequirements) { this.cpuRequirements = cpuRequirements; return this; }
        public ResourceRequirementsBuilder memoryRequirements(String memoryRequirements) { this.memoryRequirements = memoryRequirements; return this; }
        public ResourceRequirementsBuilder storageRequirements(String storageRequirements) { this.storageRequirements = storageRequirements; return this; }
        public ResourceRequirementsBuilder networkRequirements(String networkRequirements) { this.networkRequirements = networkRequirements; return this; }
        public ResourceRequirementsBuilder externalDependencies(List<String> externalDependencies) { this.externalDependencies = externalDependencies; return this; }
        public ResourceRequirementsBuilder resourceLimits(Map<String, Object> resourceLimits) { this.resourceLimits = resourceLimits; return this; }
        public ResourceRequirementsBuilder resourceAllocation(Map<String, Object> resourceAllocation) { this.resourceAllocation = resourceAllocation; return this; }
        public ResourceRequirementsBuilder priority(String priority) { this.priority = priority; return this; }
        public ResourceRequirementsBuilder constraints(List<String> constraints) { this.constraints = constraints; return this; }
        
        public ResourceRequirements build() {
            ResourceRequirements requirements = new ResourceRequirements();
            requirements.setCpuRequirements(cpuRequirements);
            requirements.setMemoryRequirements(memoryRequirements);
            requirements.setStorageRequirements(storageRequirements);
            requirements.setNetworkRequirements(networkRequirements);
            requirements.setExternalDependencies(externalDependencies);
            requirements.setResourceLimits(resourceLimits);
            requirements.setResourceAllocation(resourceAllocation);
            requirements.setPriority(priority);
            requirements.setConstraints(constraints);
            return requirements;
        }
    }
}
