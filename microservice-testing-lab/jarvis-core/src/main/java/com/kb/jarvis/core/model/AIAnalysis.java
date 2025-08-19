package com.kb.jarvis.core.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

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

    // Default constructor
    public AIAnalysis() {}

    // Constructor with all fields
    public AIAnalysis(UUID id, UserIntent intent, List<String> insights, RiskAssessment riskAssessment,
                     PerformancePrediction performancePrediction, List<Recommendation> recommendations,
                     double confidence, List<Pattern> patterns, List<Trend> identifiedTrends,
                     Map<String, Object> featureAnalysis, List<String> anomalyDetections,
                     Map<String, Double> predictionScores, List<String> optimizationSuggestions,
                     Map<String, Object> contextualData, String analysisModel, String analysisVersion,
                     LocalDateTime timestamp, Long processingTimeMs, Map<String, Object> metadata) {
        this.id = id;
        this.intent = intent;
        this.insights = insights;
        this.riskAssessment = riskAssessment;
        this.performancePrediction = performancePrediction;
        this.recommendations = recommendations;
        this.confidence = confidence;
        this.patterns = patterns;
        this.identifiedTrends = identifiedTrends;
        this.featureAnalysis = featureAnalysis;
        this.anomalyDetections = anomalyDetections;
        this.predictionScores = predictionScores;
        this.optimizationSuggestions = optimizationSuggestions;
        this.contextualData = contextualData;
        this.analysisModel = analysisModel;
        this.analysisVersion = analysisVersion;
        this.timestamp = timestamp;
        this.processingTimeMs = processingTimeMs;
        this.metadata = metadata;
    }

    // Builder pattern
    public static AIAnalysisBuilder builder() {
        return new AIAnalysisBuilder();
    }

    // Getter methods
    public UUID getId() { return id; }
    public UserIntent getIntent() { return intent; }
    public List<String> getInsights() { return insights; }
    public RiskAssessment getRiskAssessment() { return riskAssessment; }
    public PerformancePrediction getPerformancePrediction() { return performancePrediction; }
    public List<Recommendation> getRecommendations() { return recommendations; }
    public double getConfidence() { return confidence; }
    public List<Pattern> getPatterns() { return patterns; }
    public List<Trend> getIdentifiedTrends() { return identifiedTrends; }
    public Map<String, Object> getFeatureAnalysis() { return featureAnalysis; }
    public List<String> getAnomalyDetections() { return anomalyDetections; }
    public Map<String, Double> getPredictionScores() { return predictionScores; }
    public List<String> getOptimizationSuggestions() { return optimizationSuggestions; }
    public Map<String, Object> getContextualData() { return contextualData; }
    public String getAnalysisModel() { return analysisModel; }
    public String getAnalysisVersion() { return analysisVersion; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public Long getProcessingTimeMs() { return processingTimeMs; }
    public Map<String, Object> getMetadata() { return metadata; }

    // Setter methods
    public void setId(UUID id) { this.id = id; }
    public void setIntent(UserIntent intent) { this.intent = intent; }
    public void setInsights(List<String> insights) { this.insights = insights; }
    public void setRiskAssessment(RiskAssessment riskAssessment) { this.riskAssessment = riskAssessment; }
    public void setPerformancePrediction(PerformancePrediction performancePrediction) { this.performancePrediction = performancePrediction; }
    public void setRecommendations(List<Recommendation> recommendations) { this.recommendations = recommendations; }
    public void setConfidence(double confidence) { this.confidence = confidence; }
    public void setPatterns(List<Pattern> patterns) { this.patterns = patterns; }
    public void setIdentifiedTrends(List<Trend> identifiedTrends) { this.identifiedTrends = identifiedTrends; }
    public void setFeatureAnalysis(Map<String, Object> featureAnalysis) { this.featureAnalysis = featureAnalysis; }
    public void setAnomalyDetections(List<String> anomalyDetections) { this.anomalyDetections = anomalyDetections; }
    public void setPredictionScores(Map<String, Double> predictionScores) { this.predictionScores = predictionScores; }
    public void setOptimizationSuggestions(List<String> optimizationSuggestions) { this.optimizationSuggestions = optimizationSuggestions; }
    public void setContextualData(Map<String, Object> contextualData) { this.contextualData = contextualData; }
    public void setAnalysisModel(String analysisModel) { this.analysisModel = analysisModel; }
    public void setAnalysisVersion(String analysisVersion) { this.analysisVersion = analysisVersion; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public void setProcessingTimeMs(Long processingTimeMs) { this.processingTimeMs = processingTimeMs; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }

    // equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AIAnalysis that = (AIAnalysis) o;
        return Double.compare(that.confidence, confidence) == 0 &&
                Objects.equals(id, that.id) &&
                Objects.equals(intent, that.intent) &&
                Objects.equals(insights, that.insights) &&
                Objects.equals(riskAssessment, that.riskAssessment) &&
                Objects.equals(performancePrediction, that.performancePrediction) &&
                Objects.equals(recommendations, that.recommendations) &&
                Objects.equals(patterns, that.patterns) &&
                Objects.equals(identifiedTrends, that.identifiedTrends) &&
                Objects.equals(featureAnalysis, that.featureAnalysis) &&
                Objects.equals(anomalyDetections, that.anomalyDetections) &&
                Objects.equals(predictionScores, that.predictionScores) &&
                Objects.equals(optimizationSuggestions, that.optimizationSuggestions) &&
                Objects.equals(contextualData, that.contextualData) &&
                Objects.equals(analysisModel, that.analysisModel) &&
                Objects.equals(analysisVersion, that.analysisVersion) &&
                Objects.equals(timestamp, that.timestamp) &&
                Objects.equals(processingTimeMs, that.processingTimeMs) &&
                Objects.equals(metadata, that.metadata);
    }

    // hashCode method
    @Override
    public int hashCode() {
        return Objects.hash(id, intent, insights, riskAssessment, performancePrediction, recommendations,
                confidence, patterns, identifiedTrends, featureAnalysis, anomalyDetections, predictionScores,
                optimizationSuggestions, contextualData, analysisModel, analysisVersion, timestamp,
                processingTimeMs, metadata);
    }

    // toString method
    @Override
    public String toString() {
        return "AIAnalysis{" +
                "id=" + id +
                ", intent=" + intent +
                ", insights=" + insights +
                ", riskAssessment=" + riskAssessment +
                ", performancePrediction=" + performancePrediction +
                ", recommendations=" + recommendations +
                ", confidence=" + confidence +
                ", patterns=" + patterns +
                ", identifiedTrends=" + identifiedTrends +
                ", featureAnalysis=" + featureAnalysis +
                ", anomalyDetections=" + anomalyDetections +
                ", predictionScores=" + predictionScores +
                ", optimizationSuggestions=" + optimizationSuggestions +
                ", contextualData=" + contextualData +
                ", analysisModel='" + analysisModel + '\'' +
                ", analysisVersion='" + analysisVersion + '\'' +
                ", timestamp=" + timestamp +
                ", processingTimeMs=" + processingTimeMs +
                ", metadata=" + metadata +
                '}';
    }

    // Builder class
    public static class AIAnalysisBuilder {
        private UUID id;
        private UserIntent intent;
        private List<String> insights;
        private RiskAssessment riskAssessment;
        private PerformancePrediction performancePrediction;
        private List<Recommendation> recommendations;
        private double confidence;
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

        public AIAnalysisBuilder id(UUID id) { this.id = id; return this; }
        public AIAnalysisBuilder intent(UserIntent intent) { this.intent = intent; return this; }
        public AIAnalysisBuilder insights(List<String> insights) { this.insights = insights; return this; }
        public AIAnalysisBuilder riskAssessment(RiskAssessment riskAssessment) { this.riskAssessment = riskAssessment; return this; }
        public AIAnalysisBuilder performancePrediction(PerformancePrediction performancePrediction) { this.performancePrediction = performancePrediction; return this; }
        public AIAnalysisBuilder recommendations(List<Recommendation> recommendations) { this.recommendations = recommendations; return this; }
        public AIAnalysisBuilder confidence(double confidence) { this.confidence = confidence; return this; }
        public AIAnalysisBuilder patterns(List<Pattern> patterns) { this.patterns = patterns; return this; }
        public AIAnalysisBuilder identifiedTrends(List<Trend> identifiedTrends) { this.identifiedTrends = identifiedTrends; return this; }
        public AIAnalysisBuilder featureAnalysis(Map<String, Object> featureAnalysis) { this.featureAnalysis = featureAnalysis; return this; }
        public AIAnalysisBuilder anomalyDetections(List<String> anomalyDetections) { this.anomalyDetections = anomalyDetections; return this; }
        public AIAnalysisBuilder predictionScores(Map<String, Double> predictionScores) { this.predictionScores = predictionScores; return this; }
        public AIAnalysisBuilder optimizationSuggestions(List<String> optimizationSuggestions) { this.optimizationSuggestions = optimizationSuggestions; return this; }
        public AIAnalysisBuilder contextualData(Map<String, Object> contextualData) { this.contextualData = contextualData; return this; }
        public AIAnalysisBuilder analysisModel(String analysisModel) { this.analysisModel = analysisModel; return this; }
        public AIAnalysisBuilder analysisVersion(String analysisVersion) { this.analysisVersion = analysisVersion; return this; }
        public AIAnalysisBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }
        public AIAnalysisBuilder processingTimeMs(Long processingTimeMs) { this.processingTimeMs = processingTimeMs; return this; }
        public AIAnalysisBuilder metadata(Map<String, Object> metadata) { this.metadata = metadata; return this; }

        public AIAnalysis build() {
            return new AIAnalysis(id, intent, insights, riskAssessment, performancePrediction, recommendations,
                    confidence, patterns, identifiedTrends, featureAnalysis, anomalyDetections, predictionScores,
                    optimizationSuggestions, contextualData, analysisModel, analysisVersion, timestamp,
                    processingTimeMs, metadata);
        }
    }
} 