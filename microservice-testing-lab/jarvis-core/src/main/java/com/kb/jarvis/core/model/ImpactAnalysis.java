package com.kb.jarvis.core.model;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents the impact analysis of changes in a Pull Request
 * Contains information about affected areas, dependencies, and potential impacts
 */
public class ImpactAnalysis {
    
    private ImpactLevel overallImpact;
    private List<String> affectedServices;
    private List<String> affectedComponents;
    private List<String> affectedModules;
    private List<String> affectedEndpoints;
    private List<String> affectedDatabases;
    private List<String> affectedConfigurations;
    private List<String> dependencies;
    private List<String> downstreamServices;
    private List<String> upstreamServices;
    private Map<String, ImpactLevel> serviceImpacts;
    private Map<String, ImpactLevel> componentImpacts;
    private List<String> breakingChanges;
    private List<String> newFeatures;
    private List<String> bugFixes;
    private List<String> refactoring;
    private List<String> performanceChanges;
    private List<String> securityChanges;
    private Map<String, Object> metrics;
    private String summary;
    private List<String> recommendations;
    
    // Default constructor
    public ImpactAnalysis() {}
    
    // Constructor with essential fields
    public ImpactAnalysis(ImpactLevel overallImpact, List<String> affectedServices) {
        this.overallImpact = overallImpact;
        this.affectedServices = affectedServices;
    }
    
    // Builder pattern
    public static ImpactAnalysisBuilder builder() {
        return new ImpactAnalysisBuilder();
    }
    
    // Getters
    public ImpactLevel getOverallImpact() { return overallImpact; }
    public List<String> getAffectedServices() { return affectedServices; }
    public List<String> getAffectedComponents() { return affectedComponents; }
    public List<String> getAffectedModules() { return affectedModules; }
    public List<String> getAffectedEndpoints() { return affectedEndpoints; }
    public List<String> getAffectedDatabases() { return affectedDatabases; }
    public List<String> getAffectedConfigurations() { return affectedConfigurations; }
    public List<String> getDependencies() { return dependencies; }
    public List<String> getDownstreamServices() { return downstreamServices; }
    public List<String> getUpstreamServices() { return upstreamServices; }
    public Map<String, ImpactLevel> getServiceImpacts() { return serviceImpacts; }
    public Map<String, ImpactLevel> getComponentImpacts() { return componentImpacts; }
    public List<String> getBreakingChanges() { return breakingChanges; }
    public List<String> getNewFeatures() { return newFeatures; }
    public List<String> getBugFixes() { return bugFixes; }
    public List<String> getRefactoring() { return refactoring; }
    public List<String> getPerformanceChanges() { return performanceChanges; }
    public List<String> getSecurityChanges() { return securityChanges; }
    public Map<String, Object> getMetrics() { return metrics; }
    public String getSummary() { return summary; }
    public List<String> getRecommendations() { return recommendations; }
    
    // Setters
    public void setOverallImpact(ImpactLevel overallImpact) { this.overallImpact = overallImpact; }
    public void setAffectedServices(List<String> affectedServices) { this.affectedServices = affectedServices; }
    public void setAffectedComponents(List<String> affectedComponents) { this.affectedComponents = affectedComponents; }
    public void setAffectedModules(List<String> affectedModules) { this.affectedModules = affectedModules; }
    public void setAffectedEndpoints(List<String> affectedEndpoints) { this.affectedEndpoints = affectedEndpoints; }
    public void setAffectedDatabases(List<String> affectedDatabases) { this.affectedDatabases = affectedDatabases; }
    public void setAffectedConfigurations(List<String> affectedConfigurations) { this.affectedConfigurations = affectedConfigurations; }
    public void setDependencies(List<String> dependencies) { this.dependencies = dependencies; }
    public void setDownstreamServices(List<String> downstreamServices) { this.downstreamServices = downstreamServices; }
    public void setUpstreamServices(List<String> upstreamServices) { this.upstreamServices = upstreamServices; }
    public void setServiceImpacts(Map<String, ImpactLevel> serviceImpacts) { this.serviceImpacts = serviceImpacts; }
    public void setComponentImpacts(Map<String, ImpactLevel> componentImpacts) { this.componentImpacts = componentImpacts; }
    public void setBreakingChanges(List<String> breakingChanges) { this.breakingChanges = breakingChanges; }
    public void setNewFeatures(List<String> newFeatures) { this.newFeatures = newFeatures; }
    public void setBugFixes(List<String> bugFixes) { this.bugFixes = bugFixes; }
    public void setRefactoring(List<String> refactoring) { this.refactoring = refactoring; }
    public void setPerformanceChanges(List<String> performanceChanges) { this.performanceChanges = performanceChanges; }
    public void setSecurityChanges(List<String> securityChanges) { this.securityChanges = securityChanges; }
    public void setMetrics(Map<String, Object> metrics) { this.metrics = metrics; }
    public void setSummary(String summary) { this.summary = summary; }
    public void setRecommendations(List<String> recommendations) { this.recommendations = recommendations; }
    
    // Helper methods
    public boolean hasBreakingChanges() {
        return breakingChanges != null && !breakingChanges.isEmpty();
    }
    
    public boolean hasNewFeatures() {
        return newFeatures != null && !newFeatures.isEmpty();
    }
    
    public boolean hasSecurityChanges() {
        return securityChanges != null && !securityChanges.isEmpty();
    }
    
    public boolean hasPerformanceChanges() {
        return performanceChanges != null && !performanceChanges.isEmpty();
    }
    
    public boolean affectsService(String serviceName) {
        return affectedServices != null && affectedServices.contains(serviceName);
    }
    
    public ImpactLevel getServiceImpact(String serviceName) {
        return serviceImpacts != null ? serviceImpacts.get(serviceName) : ImpactLevel.NONE;
    }
    
    public int getTotalAffectedAreas() {
        int count = 0;
        if (affectedServices != null) count += affectedServices.size();
        if (affectedComponents != null) count += affectedComponents.size();
        if (affectedModules != null) count += affectedModules.size();
        if (affectedEndpoints != null) count += affectedEndpoints.size();
        return count;
    }
    
    // equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImpactAnalysis that = (ImpactAnalysis) o;
        return overallImpact == that.overallImpact &&
               Objects.equals(affectedServices, that.affectedServices);
    }
    
    // hashCode method
    @Override
    public int hashCode() {
        return Objects.hash(overallImpact, affectedServices);
    }
    
    // toString method
    @Override
    public String toString() {
        return "ImpactAnalysis{" +
                "overallImpact=" + overallImpact +
                ", affectedServices=" + (affectedServices != null ? affectedServices.size() : 0) +
                ", hasBreakingChanges=" + hasBreakingChanges() +
                ", hasNewFeatures=" + hasNewFeatures() +
                '}';
    }
    
    // Builder class
    public static class ImpactAnalysisBuilder {
        private ImpactLevel overallImpact;
        private List<String> affectedServices;
        private List<String> affectedComponents;
        private List<String> affectedModules;
        private List<String> affectedEndpoints;
        private List<String> affectedDatabases;
        private List<String> affectedConfigurations;
        private List<String> dependencies;
        private List<String> downstreamServices;
        private List<String> upstreamServices;
        private Map<String, ImpactLevel> serviceImpacts;
        private Map<String, ImpactLevel> componentImpacts;
        private List<String> breakingChanges;
        private List<String> newFeatures;
        private List<String> bugFixes;
        private List<String> refactoring;
        private List<String> performanceChanges;
        private List<String> securityChanges;
        private Map<String, Object> metrics;
        private String summary;
        private List<String> recommendations;
        
        public ImpactAnalysisBuilder overallImpact(ImpactLevel overallImpact) { this.overallImpact = overallImpact; return this; }
        public ImpactAnalysisBuilder affectedServices(List<String> affectedServices) { this.affectedServices = affectedServices; return this; }
        public ImpactAnalysisBuilder affectedComponents(List<String> affectedComponents) { this.affectedComponents = affectedComponents; return this; }
        public ImpactAnalysisBuilder affectedModules(List<String> affectedModules) { this.affectedModules = affectedModules; return this; }
        public ImpactAnalysisBuilder affectedEndpoints(List<String> affectedEndpoints) { this.affectedEndpoints = affectedEndpoints; return this; }
        public ImpactAnalysisBuilder affectedDatabases(List<String> affectedDatabases) { this.affectedDatabases = affectedDatabases; return this; }
        public ImpactAnalysisBuilder affectedConfigurations(List<String> affectedConfigurations) { this.affectedConfigurations = affectedConfigurations; return this; }
        public ImpactAnalysisBuilder dependencies(List<String> dependencies) { this.dependencies = dependencies; return this; }
        public ImpactAnalysisBuilder downstreamServices(List<String> downstreamServices) { this.downstreamServices = downstreamServices; return this; }
        public ImpactAnalysisBuilder upstreamServices(List<String> upstreamServices) { this.upstreamServices = upstreamServices; return this; }
        public ImpactAnalysisBuilder serviceImpacts(Map<String, ImpactLevel> serviceImpacts) { this.serviceImpacts = serviceImpacts; return this; }
        public ImpactAnalysisBuilder componentImpacts(Map<String, ImpactLevel> componentImpacts) { this.componentImpacts = componentImpacts; return this; }
        public ImpactAnalysisBuilder breakingChanges(List<String> breakingChanges) { this.breakingChanges = breakingChanges; return this; }
        public ImpactAnalysisBuilder newFeatures(List<String> newFeatures) { this.newFeatures = newFeatures; return this; }
        public ImpactAnalysisBuilder bugFixes(List<String> bugFixes) { this.bugFixes = bugFixes; return this; }
        public ImpactAnalysisBuilder refactoring(List<String> refactoring) { this.refactoring = refactoring; return this; }
        public ImpactAnalysisBuilder performanceChanges(List<String> performanceChanges) { this.performanceChanges = performanceChanges; return this; }
        public ImpactAnalysisBuilder securityChanges(List<String> securityChanges) { this.securityChanges = securityChanges; return this; }
        public ImpactAnalysisBuilder metrics(Map<String, Object> metrics) { this.metrics = metrics; return this; }
        public ImpactAnalysisBuilder summary(String summary) { this.summary = summary; return this; }
        public ImpactAnalysisBuilder recommendations(List<String> recommendations) { this.recommendations = recommendations; return this; }
        
        public ImpactAnalysis build() {
            ImpactAnalysis analysis = new ImpactAnalysis();
            analysis.setOverallImpact(overallImpact);
            analysis.setAffectedServices(affectedServices);
            analysis.setAffectedComponents(affectedComponents);
            analysis.setAffectedModules(affectedModules);
            analysis.setAffectedEndpoints(affectedEndpoints);
            analysis.setAffectedDatabases(affectedDatabases);
            analysis.setAffectedConfigurations(affectedConfigurations);
            analysis.setDependencies(dependencies);
            analysis.setDownstreamServices(downstreamServices);
            analysis.setUpstreamServices(upstreamServices);
            analysis.setServiceImpacts(serviceImpacts);
            analysis.setComponentImpacts(componentImpacts);
            analysis.setBreakingChanges(breakingChanges);
            analysis.setNewFeatures(newFeatures);
            analysis.setBugFixes(bugFixes);
            analysis.setRefactoring(refactoring);
            analysis.setPerformanceChanges(performanceChanges);
            analysis.setSecurityChanges(securityChanges);
            analysis.setMetrics(metrics);
            analysis.setSummary(summary);
            analysis.setRecommendations(recommendations);
            return analysis;
        }
    }
}
