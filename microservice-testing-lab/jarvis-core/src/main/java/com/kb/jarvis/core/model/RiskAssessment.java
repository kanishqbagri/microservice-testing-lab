package com.kb.jarvis.core.model;

import java.util.List;
import java.util.Map;

/**
 * Represents risk assessment for a command execution
 * Contains risk levels, factors, mitigation strategies, and impact analysis
 */
public class RiskAssessment {
    
    private RiskLevel overallRiskLevel;
    private List<String> riskFactors;
    private Map<String, RiskLevel> riskLevels;
    private List<String> mitigationStrategies;
    private Map<String, Object> impactAnalysis;
    private List<String> warnings;
    private String confidence;
    private Map<String, Object> riskMetrics;
    
    // Default constructor
    public RiskAssessment() {}
    
    // Builder pattern
    public static RiskAssessmentBuilder builder() {
        return new RiskAssessmentBuilder();
    }
    
    // Getters
    public RiskLevel getOverallRiskLevel() { return overallRiskLevel; }
    public RiskLevel getRiskLevel() { return overallRiskLevel; } // Alias for compatibility
    public List<String> getRiskFactors() { return riskFactors; }
    public Map<String, RiskLevel> getRiskLevels() { return riskLevels; }
    public List<String> getMitigationStrategies() { return mitigationStrategies; }
    public Map<String, Object> getImpactAnalysis() { return impactAnalysis; }
    public List<String> getWarnings() { return warnings; }
    public String getConfidence() { return confidence; }
    public Map<String, Object> getRiskMetrics() { return riskMetrics; }
    
    // Setters
    public void setOverallRiskLevel(RiskLevel overallRiskLevel) { this.overallRiskLevel = overallRiskLevel; }
    public void setRiskFactors(List<String> riskFactors) { this.riskFactors = riskFactors; }
    public void setRiskLevels(Map<String, RiskLevel> riskLevels) { this.riskLevels = riskLevels; }
    public void setMitigationStrategies(List<String> mitigationStrategies) { this.mitigationStrategies = mitigationStrategies; }
    public void setImpactAnalysis(Map<String, Object> impactAnalysis) { this.impactAnalysis = impactAnalysis; }
    public void setWarnings(List<String> warnings) { this.warnings = warnings; }
    public void setConfidence(String confidence) { this.confidence = confidence; }
    public void setRiskMetrics(Map<String, Object> riskMetrics) { this.riskMetrics = riskMetrics; }
    
    // Builder class
    public static class RiskAssessmentBuilder {
        private RiskLevel overallRiskLevel;
        private List<String> riskFactors;
        private Map<String, RiskLevel> riskLevels;
        private List<String> mitigationStrategies;
        private Map<String, Object> impactAnalysis;
        private List<String> warnings;
        private String confidence;
        private Map<String, Object> riskMetrics;
        
        public RiskAssessmentBuilder overallRiskLevel(RiskLevel overallRiskLevel) { this.overallRiskLevel = overallRiskLevel; return this; }
        public RiskAssessmentBuilder riskLevel(RiskLevel riskLevel) { this.overallRiskLevel = riskLevel; return this; }
        public RiskAssessmentBuilder riskScore(double riskScore) { 
            // Convert risk score to risk level
            if (riskScore >= 0.7) this.overallRiskLevel = RiskLevel.HIGH;
            else if (riskScore >= 0.4) this.overallRiskLevel = RiskLevel.MEDIUM;
            else this.overallRiskLevel = RiskLevel.LOW;
            return this; 
        }
        public RiskAssessmentBuilder riskFactors(List<String> riskFactors) { this.riskFactors = riskFactors; return this; }
        public RiskAssessmentBuilder riskLevels(Map<String, RiskLevel> riskLevels) { this.riskLevels = riskLevels; return this; }
        public RiskAssessmentBuilder mitigationStrategies(List<String> mitigationStrategies) { this.mitigationStrategies = mitigationStrategies; return this; }
        public RiskAssessmentBuilder impactAnalysis(Map<String, Object> impactAnalysis) { this.impactAnalysis = impactAnalysis; return this; }
        public RiskAssessmentBuilder warnings(List<String> warnings) { this.warnings = warnings; return this; }
        public RiskAssessmentBuilder confidence(String confidence) { this.confidence = confidence; return this; }
        public RiskAssessmentBuilder riskMetrics(Map<String, Object> riskMetrics) { this.riskMetrics = riskMetrics; return this; }
        public RiskAssessmentBuilder timestamp(java.time.LocalDateTime timestamp) { return this; } // Stub method for compatibility
        
        public RiskAssessment build() {
            RiskAssessment assessment = new RiskAssessment();
            assessment.setOverallRiskLevel(overallRiskLevel);
            assessment.setRiskFactors(riskFactors);
            assessment.setRiskLevels(riskLevels);
            assessment.setMitigationStrategies(mitigationStrategies);
            assessment.setImpactAnalysis(impactAnalysis);
            assessment.setWarnings(warnings);
            assessment.setConfidence(confidence);
            assessment.setRiskMetrics(riskMetrics);
            return assessment;
        }
    }
}