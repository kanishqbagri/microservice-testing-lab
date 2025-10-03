package com.kb.jarvis.core.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents the complete analysis of a Pull Request
 * Contains impact analysis, risk assessment, and testing recommendations
 */
public class PRAnalysis {
    
    private String analysisId;
    private PRInfo prInfo;
    private ImpactAnalysis impactAnalysis;
    private RiskAssessment riskAssessment;
    private List<TestRecommendation> testRecommendations;
    private List<String> affectedServices;
    private List<String> affectedComponents;
    private List<String> criticalPaths;
    private List<String> potentialIssues;
    private List<String> securityConcerns;
    private List<String> performanceImplications;
    private Map<String, Object> metrics;
    private double confidenceScore;
    private String analysisVersion;
    private LocalDateTime analyzedAt;
    private String analyzedBy;
    private List<String> insights;
    private Map<String, Object> metadata;
    
    // Default constructor
    public PRAnalysis() {}
    
    // Constructor with essential fields
    public PRAnalysis(String analysisId, PRInfo prInfo, ImpactAnalysis impactAnalysis, 
                      RiskAssessment riskAssessment) {
        this.analysisId = analysisId;
        this.prInfo = prInfo;
        this.impactAnalysis = impactAnalysis;
        this.riskAssessment = riskAssessment;
        this.analyzedAt = LocalDateTime.now();
    }
    
    // Builder pattern
    public static PRAnalysisBuilder builder() {
        return new PRAnalysisBuilder();
    }
    
    // Getters
    public String getAnalysisId() { return analysisId; }
    public PRInfo getPrInfo() { return prInfo; }
    public ImpactAnalysis getImpactAnalysis() { return impactAnalysis; }
    public RiskAssessment getRiskAssessment() { return riskAssessment; }
    public List<TestRecommendation> getTestRecommendations() { return testRecommendations; }
    public List<String> getAffectedServices() { return affectedServices; }
    public List<String> getAffectedComponents() { return affectedComponents; }
    public List<String> getCriticalPaths() { return criticalPaths; }
    public List<String> getPotentialIssues() { return potentialIssues; }
    public List<String> getSecurityConcerns() { return securityConcerns; }
    public List<String> getPerformanceImplications() { return performanceImplications; }
    public Map<String, Object> getMetrics() { return metrics; }
    public double getConfidenceScore() { return confidenceScore; }
    public String getAnalysisVersion() { return analysisVersion; }
    public LocalDateTime getAnalyzedAt() { return analyzedAt; }
    public String getAnalyzedBy() { return analyzedBy; }
    public List<String> getInsights() { return insights; }
    public Map<String, Object> getMetadata() { return metadata; }
    
    // Setters
    public void setAnalysisId(String analysisId) { this.analysisId = analysisId; }
    public void setPrInfo(PRInfo prInfo) { this.prInfo = prInfo; }
    public void setImpactAnalysis(ImpactAnalysis impactAnalysis) { this.impactAnalysis = impactAnalysis; }
    public void setRiskAssessment(RiskAssessment riskAssessment) { this.riskAssessment = riskAssessment; }
    public void setTestRecommendations(List<TestRecommendation> testRecommendations) { this.testRecommendations = testRecommendations; }
    public void setAffectedServices(List<String> affectedServices) { this.affectedServices = affectedServices; }
    public void setAffectedComponents(List<String> affectedComponents) { this.affectedComponents = affectedComponents; }
    public void setCriticalPaths(List<String> criticalPaths) { this.criticalPaths = criticalPaths; }
    public void setPotentialIssues(List<String> potentialIssues) { this.potentialIssues = potentialIssues; }
    public void setSecurityConcerns(List<String> securityConcerns) { this.securityConcerns = securityConcerns; }
    public void setPerformanceImplications(List<String> performanceImplications) { this.performanceImplications = performanceImplications; }
    public void setMetrics(Map<String, Object> metrics) { this.metrics = metrics; }
    public void setConfidenceScore(double confidenceScore) { this.confidenceScore = confidenceScore; }
    public void setAnalysisVersion(String analysisVersion) { this.analysisVersion = analysisVersion; }
    public void setAnalyzedAt(LocalDateTime analyzedAt) { this.analyzedAt = analyzedAt; }
    public void setAnalyzedBy(String analyzedBy) { this.analyzedBy = analyzedBy; }
    public void setInsights(List<String> insights) { this.insights = insights; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    
    // Helper methods
    public boolean isHighRisk() {
        return riskAssessment != null && 
               riskAssessment.getOverallRiskLevel() == RiskLevel.HIGH;
    }
    
    public boolean requiresSecurityTesting() {
        return securityConcerns != null && !securityConcerns.isEmpty();
    }
    
    public boolean requiresPerformanceTesting() {
        return performanceImplications != null && !performanceImplications.isEmpty();
    }
    
    public int getTotalTestRecommendations() {
        return testRecommendations != null ? testRecommendations.size() : 0;
    }
    
    public List<TestRecommendation> getHighPriorityRecommendations() {
        if (testRecommendations == null) return List.of();
        return testRecommendations.stream()
                .filter(rec -> rec.getPriority() >= 0.8)
                .toList();
    }
    
    public List<TestRecommendation> getRecommendationsByType(TestType testType) {
        if (testRecommendations == null) return List.of();
        return testRecommendations.stream()
                .filter(rec -> rec.getTestType() == testType)
                .toList();
    }
    
    // equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PRAnalysis that = (PRAnalysis) o;
        return Objects.equals(analysisId, that.analysisId);
    }
    
    // hashCode method
    @Override
    public int hashCode() {
        return Objects.hash(analysisId);
    }
    
    // toString method
    @Override
    public String toString() {
        return "PRAnalysis{" +
                "analysisId='" + analysisId + '\'' +
                ", prId='" + (prInfo != null ? prInfo.getPrId() : "null") + '\'' +
                ", riskLevel=" + (riskAssessment != null ? riskAssessment.getOverallRiskLevel() : "null") +
                ", testRecommendations=" + getTotalTestRecommendations() +
                ", confidenceScore=" + confidenceScore +
                '}';
    }
    
    // Builder class
    public static class PRAnalysisBuilder {
        private String analysisId;
        private PRInfo prInfo;
        private ImpactAnalysis impactAnalysis;
        private RiskAssessment riskAssessment;
        private List<TestRecommendation> testRecommendations;
        private List<String> affectedServices;
        private List<String> affectedComponents;
        private List<String> criticalPaths;
        private List<String> potentialIssues;
        private List<String> securityConcerns;
        private List<String> performanceImplications;
        private Map<String, Object> metrics;
        private double confidenceScore;
        private String analysisVersion;
        private LocalDateTime analyzedAt;
        private String analyzedBy;
        private List<String> insights;
        private Map<String, Object> metadata;
        
        public PRAnalysisBuilder analysisId(String analysisId) { this.analysisId = analysisId; return this; }
        public PRAnalysisBuilder prInfo(PRInfo prInfo) { this.prInfo = prInfo; return this; }
        public PRAnalysisBuilder impactAnalysis(ImpactAnalysis impactAnalysis) { this.impactAnalysis = impactAnalysis; return this; }
        public PRAnalysisBuilder riskAssessment(RiskAssessment riskAssessment) { this.riskAssessment = riskAssessment; return this; }
        public PRAnalysisBuilder testRecommendations(List<TestRecommendation> testRecommendations) { this.testRecommendations = testRecommendations; return this; }
        public PRAnalysisBuilder affectedServices(List<String> affectedServices) { this.affectedServices = affectedServices; return this; }
        public PRAnalysisBuilder affectedComponents(List<String> affectedComponents) { this.affectedComponents = affectedComponents; return this; }
        public PRAnalysisBuilder criticalPaths(List<String> criticalPaths) { this.criticalPaths = criticalPaths; return this; }
        public PRAnalysisBuilder potentialIssues(List<String> potentialIssues) { this.potentialIssues = potentialIssues; return this; }
        public PRAnalysisBuilder securityConcerns(List<String> securityConcerns) { this.securityConcerns = securityConcerns; return this; }
        public PRAnalysisBuilder performanceImplications(List<String> performanceImplications) { this.performanceImplications = performanceImplications; return this; }
        public PRAnalysisBuilder metrics(Map<String, Object> metrics) { this.metrics = metrics; return this; }
        public PRAnalysisBuilder confidenceScore(double confidenceScore) { this.confidenceScore = confidenceScore; return this; }
        public PRAnalysisBuilder analysisVersion(String analysisVersion) { this.analysisVersion = analysisVersion; return this; }
        public PRAnalysisBuilder analyzedAt(LocalDateTime analyzedAt) { this.analyzedAt = analyzedAt; return this; }
        public PRAnalysisBuilder analyzedBy(String analyzedBy) { this.analyzedBy = analyzedBy; return this; }
        public PRAnalysisBuilder insights(List<String> insights) { this.insights = insights; return this; }
        public PRAnalysisBuilder metadata(Map<String, Object> metadata) { this.metadata = metadata; return this; }
        
        public PRAnalysis build() {
            PRAnalysis analysis = new PRAnalysis();
            analysis.setAnalysisId(analysisId);
            analysis.setPrInfo(prInfo);
            analysis.setImpactAnalysis(impactAnalysis);
            analysis.setRiskAssessment(riskAssessment);
            analysis.setTestRecommendations(testRecommendations);
            analysis.setAffectedServices(affectedServices);
            analysis.setAffectedComponents(affectedComponents);
            analysis.setCriticalPaths(criticalPaths);
            analysis.setPotentialIssues(potentialIssues);
            analysis.setSecurityConcerns(securityConcerns);
            analysis.setPerformanceImplications(performanceImplications);
            analysis.setMetrics(metrics);
            analysis.setConfidenceScore(confidenceScore);
            analysis.setAnalysisVersion(analysisVersion);
            analysis.setAnalyzedAt(analyzedAt);
            analysis.setAnalyzedBy(analyzedBy);
            analysis.setInsights(insights);
            analysis.setMetadata(metadata);
            return analysis;
        }
    }
}
