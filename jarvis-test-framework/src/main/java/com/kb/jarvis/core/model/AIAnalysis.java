package com.kb.jarvis.core.model;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class AIAnalysis {
    private UserIntent intent;
    private List<String> insights;
    private RiskAssessment riskAssessment;
    private PerformancePrediction performancePrediction;
    private List<Recommendation> recommendations;
    private double confidence;
} 