package com.kb.jarvis.core.model;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a test recommendation based on PR analysis
 * Contains information about what tests should be run and why
 */
public class TestRecommendation {
    
    private String recommendationId;
    private TestType testType;
    private String serviceName;
    private String componentName;
    private String description;
    private String rationale;
    private double priority;
    private PriorityLevel priorityLevel;
    private List<String> testScenarios;
    private List<String> testCases;
    private List<String> affectedFiles;
    private List<String> affectedMethods;
    private List<String> affectedClasses;
    private String estimatedDuration;
    private String complexity;
    private List<String> prerequisites;
    private List<String> dependencies;
    private Map<String, Object> parameters;
    private boolean automated;
    private boolean critical;
    private String category;
    private List<String> tags;
    private Map<String, Object> metadata;
    
    // Default constructor
    public TestRecommendation() {}
    
    // Constructor with essential fields
    public TestRecommendation(String recommendationId, TestType testType, String serviceName, 
                             String description, double priority) {
        this.recommendationId = recommendationId;
        this.testType = testType;
        this.serviceName = serviceName;
        this.description = description;
        this.priority = priority;
        this.priorityLevel = calculatePriorityLevel(priority);
    }
    
    // Builder pattern
    public static TestRecommendationBuilder builder() {
        return new TestRecommendationBuilder();
    }
    
    // Getters
    public String getRecommendationId() { return recommendationId; }
    public TestType getTestType() { return testType; }
    public String getServiceName() { return serviceName; }
    public String getComponentName() { return componentName; }
    public String getDescription() { return description; }
    public String getRationale() { return rationale; }
    public double getPriority() { return priority; }
    public PriorityLevel getPriorityLevel() { return priorityLevel; }
    public List<String> getTestScenarios() { return testScenarios; }
    public List<String> getTestCases() { return testCases; }
    public List<String> getAffectedFiles() { return affectedFiles; }
    public List<String> getAffectedMethods() { return affectedMethods; }
    public List<String> getAffectedClasses() { return affectedClasses; }
    public String getEstimatedDuration() { return estimatedDuration; }
    public String getComplexity() { return complexity; }
    public List<String> getPrerequisites() { return prerequisites; }
    public List<String> getDependencies() { return dependencies; }
    public Map<String, Object> getParameters() { return parameters; }
    public boolean isAutomated() { return automated; }
    public boolean isCritical() { return critical; }
    public String getCategory() { return category; }
    public List<String> getTags() { return tags; }
    public Map<String, Object> getMetadata() { return metadata; }
    
    // Setters
    public void setRecommendationId(String recommendationId) { this.recommendationId = recommendationId; }
    public void setTestType(TestType testType) { this.testType = testType; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public void setComponentName(String componentName) { this.componentName = componentName; }
    public void setDescription(String description) { this.description = description; }
    public void setRationale(String rationale) { this.rationale = rationale; }
    public void setPriority(double priority) { 
        this.priority = priority; 
        this.priorityLevel = calculatePriorityLevel(priority);
    }
    public void setPriorityLevel(PriorityLevel priorityLevel) { this.priorityLevel = priorityLevel; }
    public void setTestScenarios(List<String> testScenarios) { this.testScenarios = testScenarios; }
    public void setTestCases(List<String> testCases) { this.testCases = testCases; }
    public void setAffectedFiles(List<String> affectedFiles) { this.affectedFiles = affectedFiles; }
    public void setAffectedMethods(List<String> affectedMethods) { this.affectedMethods = affectedMethods; }
    public void setAffectedClasses(List<String> affectedClasses) { this.affectedClasses = affectedClasses; }
    public void setEstimatedDuration(String estimatedDuration) { this.estimatedDuration = estimatedDuration; }
    public void setComplexity(String complexity) { this.complexity = complexity; }
    public void setPrerequisites(List<String> prerequisites) { this.prerequisites = prerequisites; }
    public void setDependencies(List<String> dependencies) { this.dependencies = dependencies; }
    public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
    public void setAutomated(boolean automated) { this.automated = automated; }
    public void setCritical(boolean critical) { this.critical = critical; }
    public void setCategory(String category) { this.category = category; }
    public void setTags(List<String> tags) { this.tags = tags; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    
    // Helper methods
    public boolean isHighPriority() {
        return priority >= 0.8;
    }
    
    public boolean isMediumPriority() {
        return priority >= 0.5 && priority < 0.8;
    }
    
    public boolean isLowPriority() {
        return priority < 0.5;
    }
    
    public boolean affectsService(String serviceName) {
        return this.serviceName != null && this.serviceName.equals(serviceName);
    }
    
    public boolean hasTag(String tag) {
        return tags != null && tags.contains(tag);
    }
    
    public int getTotalTestCases() {
        return testCases != null ? testCases.size() : 0;
    }
    
    public int getTotalTestScenarios() {
        return testScenarios != null ? testScenarios.size() : 0;
    }
    
    private PriorityLevel calculatePriorityLevel(double priority) {
        if (priority >= 0.9) return PriorityLevel.CRITICAL;
        if (priority >= 0.7) return PriorityLevel.HIGH;
        if (priority >= 0.5) return PriorityLevel.MEDIUM;
        if (priority >= 0.3) return PriorityLevel.LOW;
        return PriorityLevel.MINIMAL;
    }
    
    // equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestRecommendation that = (TestRecommendation) o;
        return Objects.equals(recommendationId, that.recommendationId);
    }
    
    // hashCode method
    @Override
    public int hashCode() {
        return Objects.hash(recommendationId);
    }
    
    // toString method
    @Override
    public String toString() {
        return "TestRecommendation{" +
                "recommendationId='" + recommendationId + '\'' +
                ", testType=" + testType +
                ", serviceName='" + serviceName + '\'' +
                ", priority=" + priority +
                ", priorityLevel=" + priorityLevel +
                ", critical=" + critical +
                '}';
    }
    
    // Builder class
    public static class TestRecommendationBuilder {
        private String recommendationId;
        private TestType testType;
        private String serviceName;
        private String componentName;
        private String description;
        private String rationale;
        private double priority;
        private PriorityLevel priorityLevel;
        private List<String> testScenarios;
        private List<String> testCases;
        private List<String> affectedFiles;
        private List<String> affectedMethods;
        private List<String> affectedClasses;
        private String estimatedDuration;
        private String complexity;
        private List<String> prerequisites;
        private List<String> dependencies;
        private Map<String, Object> parameters;
        private boolean automated;
        private boolean critical;
        private String category;
        private List<String> tags;
        private Map<String, Object> metadata;
        
        public TestRecommendationBuilder recommendationId(String recommendationId) { this.recommendationId = recommendationId; return this; }
        public TestRecommendationBuilder testType(TestType testType) { this.testType = testType; return this; }
        public TestRecommendationBuilder serviceName(String serviceName) { this.serviceName = serviceName; return this; }
        public TestRecommendationBuilder componentName(String componentName) { this.componentName = componentName; return this; }
        public TestRecommendationBuilder description(String description) { this.description = description; return this; }
        public TestRecommendationBuilder rationale(String rationale) { this.rationale = rationale; return this; }
        public TestRecommendationBuilder priority(double priority) { this.priority = priority; return this; }
        public TestRecommendationBuilder priorityLevel(PriorityLevel priorityLevel) { this.priorityLevel = priorityLevel; return this; }
        public TestRecommendationBuilder testScenarios(List<String> testScenarios) { this.testScenarios = testScenarios; return this; }
        public TestRecommendationBuilder testCases(List<String> testCases) { this.testCases = testCases; return this; }
        public TestRecommendationBuilder affectedFiles(List<String> affectedFiles) { this.affectedFiles = affectedFiles; return this; }
        public TestRecommendationBuilder affectedMethods(List<String> affectedMethods) { this.affectedMethods = affectedMethods; return this; }
        public TestRecommendationBuilder affectedClasses(List<String> affectedClasses) { this.affectedClasses = affectedClasses; return this; }
        public TestRecommendationBuilder estimatedDuration(String estimatedDuration) { this.estimatedDuration = estimatedDuration; return this; }
        public TestRecommendationBuilder complexity(String complexity) { this.complexity = complexity; return this; }
        public TestRecommendationBuilder prerequisites(List<String> prerequisites) { this.prerequisites = prerequisites; return this; }
        public TestRecommendationBuilder dependencies(List<String> dependencies) { this.dependencies = dependencies; return this; }
        public TestRecommendationBuilder parameters(Map<String, Object> parameters) { this.parameters = parameters; return this; }
        public TestRecommendationBuilder automated(boolean automated) { this.automated = automated; return this; }
        public TestRecommendationBuilder critical(boolean critical) { this.critical = critical; return this; }
        public TestRecommendationBuilder category(String category) { this.category = category; return this; }
        public TestRecommendationBuilder tags(List<String> tags) { this.tags = tags; return this; }
        public TestRecommendationBuilder metadata(Map<String, Object> metadata) { this.metadata = metadata; return this; }
        
        public TestRecommendation build() {
            TestRecommendation recommendation = new TestRecommendation();
            recommendation.setRecommendationId(recommendationId);
            recommendation.setTestType(testType);
            recommendation.setServiceName(serviceName);
            recommendation.setComponentName(componentName);
            recommendation.setDescription(description);
            recommendation.setRationale(rationale);
            recommendation.setPriority(priority);
            recommendation.setPriorityLevel(priorityLevel);
            recommendation.setTestScenarios(testScenarios);
            recommendation.setTestCases(testCases);
            recommendation.setAffectedFiles(affectedFiles);
            recommendation.setAffectedMethods(affectedMethods);
            recommendation.setAffectedClasses(affectedClasses);
            recommendation.setEstimatedDuration(estimatedDuration);
            recommendation.setComplexity(complexity);
            recommendation.setPrerequisites(prerequisites);
            recommendation.setDependencies(dependencies);
            recommendation.setParameters(parameters);
            recommendation.setAutomated(automated);
            recommendation.setCritical(critical);
            recommendation.setCategory(category);
            recommendation.setTags(tags);
            recommendation.setMetadata(metadata);
            return recommendation;
        }
    }
}
