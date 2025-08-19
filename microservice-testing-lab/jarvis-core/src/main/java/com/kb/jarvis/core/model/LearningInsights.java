package com.kb.jarvis.core.model;

// TEMPORARILY COMMENTED OUT FOR QUICK AI INTEGRATION TESTING
// TODO: Convert to manual implementation after AI integration testing
/*
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class LearningInsights {
    private String insightType;
    private String description;
    private double confidence;
    private List<String> patterns;
    private Map<String, Object> data;
    private LocalDateTime discoveredAt;
    private String modelVersion;
    private List<String> recommendations;
}
*/

// Temporary placeholder for compilation
public class LearningInsights {
    private String insightType;
    private String description;
    private double confidence;
    private java.util.List<String> patterns;
    private java.util.Map<String, Object> data;
    private java.time.LocalDateTime discoveredAt;
    private String modelVersion;
    private java.util.List<String> recommendations;
    
    public LearningInsights() {}
    
    public String getInsightType() { return insightType; }
    public void setInsightType(String insightType) { this.insightType = insightType; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getConfidence() { return confidence; }
    public void setConfidence(double confidence) { this.confidence = confidence; }
    public java.util.List<String> getPatterns() { return patterns; }
    public void setPatterns(java.util.List<String> patterns) { this.patterns = patterns; }
    public java.util.Map<String, Object> getData() { return data; }
    public void setData(java.util.Map<String, Object> data) { this.data = data; }
    public java.time.LocalDateTime getDiscoveredAt() { return discoveredAt; }
    public void setDiscoveredAt(java.time.LocalDateTime discoveredAt) { this.discoveredAt = discoveredAt; }
    public String getModelVersion() { return modelVersion; }
    public void setModelVersion(String modelVersion) { this.modelVersion = modelVersion; }
    public java.util.List<String> getRecommendations() { return recommendations; }
    public void setRecommendations(java.util.List<String> recommendations) { this.recommendations = recommendations; }
    
    // Builder pattern
    public static LearningInsightsBuilder builder() {
        return new LearningInsightsBuilder();
    }
    
    // Builder class
    public static class LearningInsightsBuilder {
        private String insightType;
        private String description;
        private double confidence;
        private java.util.List<String> patterns;
        private java.util.Map<String, Object> data;
        private java.time.LocalDateTime discoveredAt;
        private String modelVersion;
        private java.util.List<String> recommendations;
        
        public LearningInsightsBuilder insightType(String insightType) { this.insightType = insightType; return this; }
        public LearningInsightsBuilder description(String description) { this.description = description; return this; }
        public LearningInsightsBuilder confidence(double confidence) { this.confidence = confidence; return this; }
        public LearningInsightsBuilder patterns(java.util.List<String> patterns) { this.patterns = patterns; return this; }
        public LearningInsightsBuilder data(java.util.Map<String, Object> data) { this.data = data; return this; }
        public LearningInsightsBuilder discoveredAt(java.time.LocalDateTime discoveredAt) { this.discoveredAt = discoveredAt; return this; }
        public LearningInsightsBuilder modelVersion(String modelVersion) { this.modelVersion = modelVersion; return this; }
        public LearningInsightsBuilder recommendations(java.util.List<String> recommendations) { this.recommendations = recommendations; return this; }
        public LearningInsightsBuilder insights(java.util.List<String> insights) { this.recommendations = insights; return this; } // Alias for recommendations
        public LearningInsightsBuilder timestamp(java.time.LocalDateTime timestamp) { this.discoveredAt = timestamp; return this; } // Alias for discoveredAt
        
        public LearningInsights build() {
            LearningInsights insights = new LearningInsights();
            insights.setInsightType(insightType);
            insights.setDescription(description);
            insights.setConfidence(confidence);
            insights.setPatterns(patterns);
            insights.setData(data);
            insights.setDiscoveredAt(discoveredAt);
            insights.setModelVersion(modelVersion);
            insights.setRecommendations(recommendations);
            return insights;
        }
    }
} 