package com.kb.jarvis.core.ai;

import com.kb.jarvis.core.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * AI-powered PR Analysis component
 * Provides intelligent analysis and recommendations using pattern recognition and heuristics
 */
@Component
public class PRAnalysisAI {
    
    private static final Logger log = LoggerFactory.getLogger(PRAnalysisAI.class);
    
    // AI patterns for intelligent analysis
    private static final Map<String, List<String>> SECURITY_PATTERNS = Map.of(
        "authentication", List.of("auth", "login", "jwt", "token", "password", "credential"),
        "authorization", List.of("role", "permission", "access", "authorize", "rbac"),
        "encryption", List.of("encrypt", "decrypt", "cipher", "hash", "bcrypt"),
        "vulnerability", List.of("sql injection", "xss", "csrf", "injection", "vulnerability")
    );
    
    private static final Map<String, List<String>> PERFORMANCE_PATTERNS = Map.of(
        "caching", List.of("cache", "redis", "memcached", "@cacheable"),
        "optimization", List.of("optimize", "performance", "async", "parallel", "thread"),
        "database", List.of("query", "index", "join", "n+1", "lazy", "eager"),
        "memory", List.of("memory", "heap", "gc", "leak", "allocation")
    );
    
    private static final Map<String, List<String>> BREAKING_CHANGE_PATTERNS = Map.of(
        "api_changes", List.of("@deprecated", "removed", "deleted", "renamed"),
        "method_signature", List.of("public.*\\(", "private.*\\(", "protected.*\\("),
        "data_model", List.of("field", "property", "column", "attribute"),
        "configuration", List.of("config", "property", "setting", "parameter")
    );
    
    /**
     * Enhanced AI analysis of PR changes
     */
    public AIInsights analyzeWithAI(PRInfo prInfo, List<FileChange> fileChanges) {
        log.info("Performing AI analysis for PR: {}", prInfo.getPrId());
        
        // Analyze code patterns
        CodePatternAnalysis patternAnalysis = analyzeCodePatterns(fileChanges);
        
        // Generate intelligent insights
        List<String> insights = generateIntelligentInsights(prInfo, fileChanges, patternAnalysis);
        
        // Predict potential issues
        List<String> predictedIssues = predictPotentialIssues(fileChanges, patternAnalysis);
        
        // Generate smart recommendations
        List<SmartRecommendation> smartRecommendations = generateSmartRecommendations(prInfo, fileChanges, patternAnalysis);
        
        // Calculate AI confidence score
        double aiConfidence = calculateAIConfidence(fileChanges, patternAnalysis);
        
        return AIInsights.builder()
                .patternAnalysis(patternAnalysis)
                .insights(insights)
                .predictedIssues(predictedIssues)
                .smartRecommendations(smartRecommendations)
                .aiConfidence(aiConfidence)
                .analysisTimestamp(System.currentTimeMillis())
                .build();
    }
    
    /**
     * Analyze code patterns using AI techniques
     */
    private CodePatternAnalysis analyzeCodePatterns(List<FileChange> fileChanges) {
        log.debug("Analyzing code patterns for {} file changes", fileChanges.size());
        
        Map<String, Integer> securityPatterns = new HashMap<>();
        Map<String, Integer> performancePatterns = new HashMap<>();
        Map<String, Integer> breakingChangePatterns = new HashMap<>();
        List<String> detectedPatterns = new ArrayList<>();
        
        for (FileChange fileChange : fileChanges) {
            // Analyze added lines for patterns
            if (fileChange.getAddedLines() != null) {
                for (String line : fileChange.getAddedLines()) {
                    String lowerLine = line.toLowerCase();
                    
                    // Check security patterns
                    for (Map.Entry<String, List<String>> entry : SECURITY_PATTERNS.entrySet()) {
                        for (String pattern : entry.getValue()) {
                            if (lowerLine.contains(pattern)) {
                                securityPatterns.merge(entry.getKey(), 1, Integer::sum);
                                detectedPatterns.add("security:" + entry.getKey());
                            }
                        }
                    }
                    
                    // Check performance patterns
                    for (Map.Entry<String, List<String>> entry : PERFORMANCE_PATTERNS.entrySet()) {
                        for (String pattern : entry.getValue()) {
                            if (lowerLine.contains(pattern)) {
                                performancePatterns.merge(entry.getKey(), 1, Integer::sum);
                                detectedPatterns.add("performance:" + entry.getKey());
                            }
                        }
                    }
                    
                    // Check breaking change patterns
                    for (Map.Entry<String, List<String>> entry : BREAKING_CHANGE_PATTERNS.entrySet()) {
                        for (String pattern : entry.getValue()) {
                            if (lowerLine.contains(pattern)) {
                                breakingChangePatterns.merge(entry.getKey(), 1, Integer::sum);
                                detectedPatterns.add("breaking:" + entry.getKey());
                            }
                        }
                    }
                }
            }
        }
        
        return CodePatternAnalysis.builder()
                .securityPatterns(securityPatterns)
                .performancePatterns(performancePatterns)
                .breakingChangePatterns(breakingChangePatterns)
                .detectedPatterns(detectedPatterns)
                .totalPatterns(detectedPatterns.size())
                .build();
    }
    
    /**
     * Generate intelligent insights based on AI analysis
     */
    private List<String> generateIntelligentInsights(PRInfo prInfo, List<FileChange> fileChanges, CodePatternAnalysis patternAnalysis) {
        List<String> insights = new ArrayList<>();
        
        // Security insights
        if (!patternAnalysis.getSecurityPatterns().isEmpty()) {
            insights.add("🔒 Security patterns detected: " + String.join(", ", patternAnalysis.getSecurityPatterns().keySet()));
            if (patternAnalysis.getSecurityPatterns().containsKey("authentication")) {
                insights.add("🔐 Authentication changes detected - ensure proper token validation and session management");
            }
            if (patternAnalysis.getSecurityPatterns().containsKey("authorization")) {
                insights.add("🛡️ Authorization changes detected - verify role-based access control implementation");
            }
        }
        
        // Performance insights
        if (!patternAnalysis.getPerformancePatterns().isEmpty()) {
            insights.add("⚡ Performance patterns detected: " + String.join(", ", patternAnalysis.getPerformancePatterns().keySet()));
            if (patternAnalysis.getPerformancePatterns().containsKey("caching")) {
                insights.add("💾 Caching implementation detected - test cache invalidation and hit rates");
            }
            if (patternAnalysis.getPerformancePatterns().containsKey("database")) {
                insights.add("🗄️ Database changes detected - monitor query performance and execution plans");
            }
        }
        
        // Breaking change insights
        if (!patternAnalysis.getBreakingChangePatterns().isEmpty()) {
            insights.add("⚠️ Potential breaking changes detected: " + String.join(", ", patternAnalysis.getBreakingChangePatterns().keySet()));
            insights.add("🔄 Breaking changes require comprehensive integration testing and API versioning strategy");
        }
        
        // Size-based insights
        if (prInfo.isLargePR()) {
            insights.add("📏 Large PR detected - consider breaking into smaller, focused changes for better reviewability");
        }
        
        // Service impact insights
        Set<String> affectedServices = fileChanges.stream()
                .map(FileChange::getServiceName)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        
        if (affectedServices.size() > 1) {
            insights.add("🔗 Multi-service changes detected - test cross-service interactions and data flow");
        }
        
        return insights;
    }
    
    /**
     * Predict potential issues using AI
     */
    private List<String> predictPotentialIssues(List<FileChange> fileChanges, CodePatternAnalysis patternAnalysis) {
        List<String> predictedIssues = new ArrayList<>();
        
        // Security issues
        if (patternAnalysis.getSecurityPatterns().containsKey("vulnerability")) {
            predictedIssues.add("🚨 Potential security vulnerabilities - conduct security audit");
        }
        
        // Performance issues
        if (patternAnalysis.getPerformancePatterns().containsKey("database")) {
            predictedIssues.add("🐌 Database performance may be impacted - monitor query execution times");
        }
        
        // Breaking change issues
        if (!patternAnalysis.getBreakingChangePatterns().isEmpty()) {
            predictedIssues.add("💥 Breaking changes may affect dependent services - update API documentation");
        }
        
        // Configuration issues
        long configChanges = fileChanges.stream()
                .filter(FileChange::isConfigFile)
                .count();
        
        if (configChanges > 0) {
            predictedIssues.add("⚙️ Configuration changes detected - verify environment-specific settings");
        }
        
        // Test coverage issues
        long testChanges = fileChanges.stream()
                .filter(FileChange::isTestFile)
                .count();
        
        if (testChanges == 0 && fileChanges.size() > 2) {
            predictedIssues.add("🧪 No test changes detected - ensure adequate test coverage for new functionality");
        }
        
        return predictedIssues;
    }
    
    /**
     * Generate smart recommendations using AI
     */
    private List<SmartRecommendation> generateSmartRecommendations(PRInfo prInfo, List<FileChange> fileChanges, CodePatternAnalysis patternAnalysis) {
        List<SmartRecommendation> recommendations = new ArrayList<>();
        
        // Security recommendations
        if (patternAnalysis.getSecurityPatterns().containsKey("authentication")) {
            recommendations.add(SmartRecommendation.builder()
                    .category("Security")
                    .priority(0.95)
                    .title("Enhanced Authentication Testing")
                    .description("Implement comprehensive authentication testing including token validation, session management, and edge cases")
                    .rationale("Authentication changes detected - critical security component")
                    .suggestedTests(List.of("JWT token validation", "Session timeout testing", "Invalid credential handling"))
                    .estimatedEffort("High")
                    .automationLevel("Semi-automated")
                    .build());
        }
        
        // Performance recommendations
        if (patternAnalysis.getPerformancePatterns().containsKey("caching")) {
            recommendations.add(SmartRecommendation.builder()
                    .category("Performance")
                    .priority(0.8)
                    .title("Cache Performance Testing")
                    .description("Test cache hit rates, invalidation strategies, and performance under load")
                    .rationale("Caching implementation detected - performance critical")
                    .suggestedTests(List.of("Cache hit rate analysis", "Cache invalidation testing", "Load testing with cache"))
                    .estimatedEffort("Medium")
                    .automationLevel("Automated")
                    .build());
        }
        
        // Breaking change recommendations
        if (!patternAnalysis.getBreakingChangePatterns().isEmpty()) {
            recommendations.add(SmartRecommendation.builder()
                    .category("Compatibility")
                    .priority(0.9)
                    .title("Breaking Change Validation")
                    .description("Comprehensive testing to validate backward compatibility and migration strategies")
                    .rationale("Breaking changes detected - requires careful validation")
                    .suggestedTests(List.of("API contract testing", "Backward compatibility testing", "Migration testing"))
                    .estimatedEffort("High")
                    .automationLevel("Semi-automated")
                    .build());
        }
        
        // Multi-service recommendations
        Set<String> affectedServices = fileChanges.stream()
                .map(FileChange::getServiceName)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        
        if (affectedServices.size() > 1) {
            recommendations.add(SmartRecommendation.builder()
                    .category("Integration")
                    .priority(0.85)
                    .title("Cross-Service Integration Testing")
                    .description("Test interactions between affected services and data flow")
                    .rationale("Multiple services affected - integration testing required")
                    .suggestedTests(List.of("Service-to-service communication", "Data consistency testing", "End-to-end workflow testing"))
                    .estimatedEffort("High")
                    .automationLevel("Automated")
                    .build());
        }
        
        return recommendations;
    }
    
    /**
     * Calculate AI confidence score
     */
    private double calculateAIConfidence(List<FileChange> fileChanges, CodePatternAnalysis patternAnalysis) {
        double confidence = 0.7; // Base confidence
        
        // Increase confidence based on pattern detection
        if (patternAnalysis.getTotalPatterns() > 0) {
            confidence += 0.1;
        }
        
        // Increase confidence based on file change analysis
        if (fileChanges != null && !fileChanges.isEmpty()) {
            confidence += 0.1;
        }
        
        // Increase confidence if we have detailed analysis
        if (patternAnalysis.getSecurityPatterns().size() + 
            patternAnalysis.getPerformancePatterns().size() + 
            patternAnalysis.getBreakingChangePatterns().size() > 0) {
            confidence += 0.1;
        }
        
        return Math.min(confidence, 1.0);
    }
    
    /**
     * AI Insights data class
     */
    public static class AIInsights {
        private CodePatternAnalysis patternAnalysis;
        private List<String> insights;
        private List<String> predictedIssues;
        private List<SmartRecommendation> smartRecommendations;
        private double aiConfidence;
        private long analysisTimestamp;
        
        // Builder pattern
        public static AIInsightsBuilder builder() {
            return new AIInsightsBuilder();
        }
        
        // Getters
        public CodePatternAnalysis getPatternAnalysis() { return patternAnalysis; }
        public List<String> getInsights() { return insights; }
        public List<String> getPredictedIssues() { return predictedIssues; }
        public List<SmartRecommendation> getSmartRecommendations() { return smartRecommendations; }
        public double getAiConfidence() { return aiConfidence; }
        public long getAnalysisTimestamp() { return analysisTimestamp; }
        
        // Setters
        public void setPatternAnalysis(CodePatternAnalysis patternAnalysis) { this.patternAnalysis = patternAnalysis; }
        public void setInsights(List<String> insights) { this.insights = insights; }
        public void setPredictedIssues(List<String> predictedIssues) { this.predictedIssues = predictedIssues; }
        public void setSmartRecommendations(List<SmartRecommendation> smartRecommendations) { this.smartRecommendations = smartRecommendations; }
        public void setAiConfidence(double aiConfidence) { this.aiConfidence = aiConfidence; }
        public void setAnalysisTimestamp(long analysisTimestamp) { this.analysisTimestamp = analysisTimestamp; }
        
        // Builder class
        public static class AIInsightsBuilder {
            private CodePatternAnalysis patternAnalysis;
            private List<String> insights;
            private List<String> predictedIssues;
            private List<SmartRecommendation> smartRecommendations;
            private double aiConfidence;
            private long analysisTimestamp;
            
            public AIInsightsBuilder patternAnalysis(CodePatternAnalysis patternAnalysis) { this.patternAnalysis = patternAnalysis; return this; }
            public AIInsightsBuilder insights(List<String> insights) { this.insights = insights; return this; }
            public AIInsightsBuilder predictedIssues(List<String> predictedIssues) { this.predictedIssues = predictedIssues; return this; }
            public AIInsightsBuilder smartRecommendations(List<SmartRecommendation> smartRecommendations) { this.smartRecommendations = smartRecommendations; return this; }
            public AIInsightsBuilder aiConfidence(double aiConfidence) { this.aiConfidence = aiConfidence; return this; }
            public AIInsightsBuilder analysisTimestamp(long analysisTimestamp) { this.analysisTimestamp = analysisTimestamp; return this; }
            
            public AIInsights build() {
                AIInsights insights = new AIInsights();
                insights.setPatternAnalysis(patternAnalysis);
                insights.setInsights(this.insights);
                insights.setPredictedIssues(predictedIssues);
                insights.setSmartRecommendations(smartRecommendations);
                insights.setAiConfidence(aiConfidence);
                insights.setAnalysisTimestamp(analysisTimestamp);
                return insights;
            }
        }
    }
    
    /**
     * Code Pattern Analysis data class
     */
    public static class CodePatternAnalysis {
        private Map<String, Integer> securityPatterns;
        private Map<String, Integer> performancePatterns;
        private Map<String, Integer> breakingChangePatterns;
        private List<String> detectedPatterns;
        private int totalPatterns;
        
        // Builder pattern
        public static CodePatternAnalysisBuilder builder() {
            return new CodePatternAnalysisBuilder();
        }
        
        // Getters
        public Map<String, Integer> getSecurityPatterns() { return securityPatterns; }
        public Map<String, Integer> getPerformancePatterns() { return performancePatterns; }
        public Map<String, Integer> getBreakingChangePatterns() { return breakingChangePatterns; }
        public List<String> getDetectedPatterns() { return detectedPatterns; }
        public int getTotalPatterns() { return totalPatterns; }
        
        // Setters
        public void setSecurityPatterns(Map<String, Integer> securityPatterns) { this.securityPatterns = securityPatterns; }
        public void setPerformancePatterns(Map<String, Integer> performancePatterns) { this.performancePatterns = performancePatterns; }
        public void setBreakingChangePatterns(Map<String, Integer> breakingChangePatterns) { this.breakingChangePatterns = breakingChangePatterns; }
        public void setDetectedPatterns(List<String> detectedPatterns) { this.detectedPatterns = detectedPatterns; }
        public void setTotalPatterns(int totalPatterns) { this.totalPatterns = totalPatterns; }
        
        // Builder class
        public static class CodePatternAnalysisBuilder {
            private Map<String, Integer> securityPatterns;
            private Map<String, Integer> performancePatterns;
            private Map<String, Integer> breakingChangePatterns;
            private List<String> detectedPatterns;
            private int totalPatterns;
            
            public CodePatternAnalysisBuilder securityPatterns(Map<String, Integer> securityPatterns) { this.securityPatterns = securityPatterns; return this; }
            public CodePatternAnalysisBuilder performancePatterns(Map<String, Integer> performancePatterns) { this.performancePatterns = performancePatterns; return this; }
            public CodePatternAnalysisBuilder breakingChangePatterns(Map<String, Integer> breakingChangePatterns) { this.breakingChangePatterns = breakingChangePatterns; return this; }
            public CodePatternAnalysisBuilder detectedPatterns(List<String> detectedPatterns) { this.detectedPatterns = detectedPatterns; return this; }
            public CodePatternAnalysisBuilder totalPatterns(int totalPatterns) { this.totalPatterns = totalPatterns; return this; }
            
            public CodePatternAnalysis build() {
                CodePatternAnalysis analysis = new CodePatternAnalysis();
                analysis.setSecurityPatterns(securityPatterns);
                analysis.setPerformancePatterns(performancePatterns);
                analysis.setBreakingChangePatterns(breakingChangePatterns);
                analysis.setDetectedPatterns(detectedPatterns);
                analysis.setTotalPatterns(totalPatterns);
                return analysis;
            }
        }
    }
    
    /**
     * Smart Recommendation data class
     */
    public static class SmartRecommendation {
        private String category;
        private double priority;
        private String title;
        private String description;
        private String rationale;
        private List<String> suggestedTests;
        private String estimatedEffort;
        private String automationLevel;
        
        // Builder pattern
        public static SmartRecommendationBuilder builder() {
            return new SmartRecommendationBuilder();
        }
        
        // Getters
        public String getCategory() { return category; }
        public double getPriority() { return priority; }
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getRationale() { return rationale; }
        public List<String> getSuggestedTests() { return suggestedTests; }
        public String getEstimatedEffort() { return estimatedEffort; }
        public String getAutomationLevel() { return automationLevel; }
        
        // Setters
        public void setCategory(String category) { this.category = category; }
        public void setPriority(double priority) { this.priority = priority; }
        public void setTitle(String title) { this.title = title; }
        public void setDescription(String description) { this.description = description; }
        public void setRationale(String rationale) { this.rationale = rationale; }
        public void setSuggestedTests(List<String> suggestedTests) { this.suggestedTests = suggestedTests; }
        public void setEstimatedEffort(String estimatedEffort) { this.estimatedEffort = estimatedEffort; }
        public void setAutomationLevel(String automationLevel) { this.automationLevel = automationLevel; }
        
        // Builder class
        public static class SmartRecommendationBuilder {
            private String category;
            private double priority;
            private String title;
            private String description;
            private String rationale;
            private List<String> suggestedTests;
            private String estimatedEffort;
            private String automationLevel;
            
            public SmartRecommendationBuilder category(String category) { this.category = category; return this; }
            public SmartRecommendationBuilder priority(double priority) { this.priority = priority; return this; }
            public SmartRecommendationBuilder title(String title) { this.title = title; return this; }
            public SmartRecommendationBuilder description(String description) { this.description = description; return this; }
            public SmartRecommendationBuilder rationale(String rationale) { this.rationale = rationale; return this; }
            public SmartRecommendationBuilder suggestedTests(List<String> suggestedTests) { this.suggestedTests = suggestedTests; return this; }
            public SmartRecommendationBuilder estimatedEffort(String estimatedEffort) { this.estimatedEffort = estimatedEffort; return this; }
            public SmartRecommendationBuilder automationLevel(String automationLevel) { this.automationLevel = automationLevel; return this; }
            
            public SmartRecommendation build() {
                SmartRecommendation recommendation = new SmartRecommendation();
                recommendation.setCategory(category);
                recommendation.setPriority(priority);
                recommendation.setTitle(title);
                recommendation.setDescription(description);
                recommendation.setRationale(rationale);
                recommendation.setSuggestedTests(suggestedTests);
                recommendation.setEstimatedEffort(estimatedEffort);
                recommendation.setAutomationLevel(automationLevel);
                return recommendation;
            }
        }
    }
}
