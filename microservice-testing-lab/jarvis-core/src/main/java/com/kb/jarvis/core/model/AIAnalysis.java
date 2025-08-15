package com.kb.jarvis.core.model;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
public class AIAnalysis {
    private UUID id;
    private UserIntent intent;
    private List<String> insights;
    private RiskAssessment riskAssessment;
    private PerformancePrediction performancePrediction;
    private List<Recommendation> recommendations;
    private double confidence;
    
    // Enhanced AI Analysis fields
    private List<Pattern> patterns;
    private List<Trend> identifiedTrends;
    private Map<String, Object> featureAnalysis;
    private List<String> anomalyDetections;
    private Map<String, Double> predictionScores;
    private List<String> optimizationSuggestions;
    private Map<String, Object> contextualData;
    private String analysisModel;
    private String analysisVersion;
    private LocalDateTime timestamp;
    private Long processingTimeMs;
    private Map<String, Object> metadata;
} 