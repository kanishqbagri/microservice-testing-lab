package com.kb.jarvis.core.model;

import java.util.List;
import java.util.Map;

/**
 * Represents service dependencies and relationships
 * Contains dependency information, affected services, and blast radius analysis
 */
public class DependencyGraph {
    
    private List<String> affectedServices;
    private Map<String, List<String>> dependencies;
    private int blastRadius;
    private String severityLevel;
    private List<String> criticalPath;
    private Map<String, Object> impactAnalysis;
    private List<String> isolationPoints;
    private Map<String, Object> riskFactors;
    
    // Default constructor
    public DependencyGraph() {}
    
    // Builder pattern
    public static DependencyGraphBuilder builder() {
        return new DependencyGraphBuilder();
    }
    
    // Getters
    public List<String> getAffectedServices() { return affectedServices; }
    public Map<String, List<String>> getDependencies() { return dependencies; }
    public int getBlastRadius() { return blastRadius; }
    public String getSeverityLevel() { return severityLevel; }
    public List<String> getCriticalPath() { return criticalPath; }
    public Map<String, Object> getImpactAnalysis() { return impactAnalysis; }
    public List<String> getIsolationPoints() { return isolationPoints; }
    public Map<String, Object> getRiskFactors() { return riskFactors; }
    
    // Setters
    public void setAffectedServices(List<String> affectedServices) { this.affectedServices = affectedServices; }
    public void setDependencies(Map<String, List<String>> dependencies) { this.dependencies = dependencies; }
    public void setBlastRadius(int blastRadius) { this.blastRadius = blastRadius; }
    public void setSeverityLevel(String severityLevel) { this.severityLevel = severityLevel; }
    public void setCriticalPath(List<String> criticalPath) { this.criticalPath = criticalPath; }
    public void setImpactAnalysis(Map<String, Object> impactAnalysis) { this.impactAnalysis = impactAnalysis; }
    public void setIsolationPoints(List<String> isolationPoints) { this.isolationPoints = isolationPoints; }
    public void setRiskFactors(Map<String, Object> riskFactors) { this.riskFactors = riskFactors; }
    
    // Builder class
    public static class DependencyGraphBuilder {
        private List<String> affectedServices;
        private Map<String, List<String>> dependencies;
        private int blastRadius;
        private String severityLevel;
        private List<String> criticalPath;
        private Map<String, Object> impactAnalysis;
        private List<String> isolationPoints;
        private Map<String, Object> riskFactors;
        
        public DependencyGraphBuilder affectedServices(List<String> affectedServices) { this.affectedServices = affectedServices; return this; }
        public DependencyGraphBuilder dependencies(Map<String, List<String>> dependencies) { this.dependencies = dependencies; return this; }
        public DependencyGraphBuilder blastRadius(int blastRadius) { this.blastRadius = blastRadius; return this; }
        public DependencyGraphBuilder severityLevel(String severityLevel) { this.severityLevel = severityLevel; return this; }
        public DependencyGraphBuilder criticalPath(List<String> criticalPath) { this.criticalPath = criticalPath; return this; }
        public DependencyGraphBuilder impactAnalysis(Map<String, Object> impactAnalysis) { this.impactAnalysis = impactAnalysis; return this; }
        public DependencyGraphBuilder isolationPoints(List<String> isolationPoints) { this.isolationPoints = isolationPoints; return this; }
        public DependencyGraphBuilder riskFactors(Map<String, Object> riskFactors) { this.riskFactors = riskFactors; return this; }
        
        public DependencyGraph build() {
            DependencyGraph graph = new DependencyGraph();
            graph.setAffectedServices(affectedServices);
            graph.setDependencies(dependencies);
            graph.setBlastRadius(blastRadius);
            graph.setSeverityLevel(severityLevel);
            graph.setCriticalPath(criticalPath);
            graph.setImpactAnalysis(impactAnalysis);
            graph.setIsolationPoints(isolationPoints);
            graph.setRiskFactors(riskFactors);
            return graph;
        }
    }
}
