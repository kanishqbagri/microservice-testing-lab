package com.kb.jarvis.core.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Result of LLM-powered PR analysis
 */
@Data
@Builder
public class LLMAnalysisResult {
    
    private CodePatternAnalysis patternAnalysis;
    private ImpactAnalysis impactAnalysis;
    private RiskAssessment riskAssessment;
    private List<TestRecommendation> testRecommendations;
    private List<String> insights;
    private List<String> warnings;
    private double confidence;
    private long analysisTimestamp;
}

