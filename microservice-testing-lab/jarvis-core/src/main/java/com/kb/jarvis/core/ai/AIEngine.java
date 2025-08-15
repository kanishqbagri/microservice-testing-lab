package com.kb.jarvis.core.ai;

import com.kb.jarvis.core.model.*;
import com.kb.jarvis.core.memory.MemoryManager;
import com.kb.jarvis.core.context.ContextManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

@Component
public class AIEngine {
    
    private static final Logger log = LoggerFactory.getLogger(AIEngine.class);
    
    @Autowired
    private ChatClient chatClient;
    
    @Autowired
    private MemoryManager memoryManager;
    
    @Autowired
    private ContextManager contextManager;
    
    @Value("${jarvis.ai.confidence-threshold:0.7}")
    private double confidenceThreshold;
    
    @Value("${jarvis.ai.max-patterns:100}")
    private int maxPatterns;
    
    // Pattern cache for performance
    private final Map<String, Pattern> patternCache = new ConcurrentHashMap<>();
    
    // Risk patterns and their weights
    private static final Map<String, Double> RISK_PATTERNS = new HashMap<>();
    private static final Map<String, Double> PERFORMANCE_PATTERNS = new HashMap<>();
    
    static {
        initializeRiskPatterns();
        initializePerformancePatterns();
    }
    
    public AIAnalysis analyzeIntent(UserIntent intent) {
        log.info("Analyzing intent: {}", intent.getType());
        
        try {
            // Step 1: Pattern Recognition
            List<Pattern> patterns = recognizePatterns(intent);
            
            // Step 2: Risk Assessment
            RiskAssessment riskAssessment = assessRisk(intent);
            
            // Step 3: Performance Prediction
            PerformancePrediction performancePrediction = predictPerformance(intent);
            
            // Step 4: Generate AI Insights
            List<String> insights = generateInsights(intent, patterns, riskAssessment, performancePrediction);
            
            // Step 5: Calculate overall confidence
            double confidence = calculateConfidence(intent, patterns, riskAssessment, performancePrediction);
            
            // Step 6: Generate recommendations
            List<Recommendation> recommendations = generateRecommendations(intent, riskAssessment, performancePrediction);
            
            AIAnalysis analysis = AIAnalysis.builder()
                .intent(intent)
                .patterns(patterns)
                .riskAssessment(riskAssessment)
                .performancePrediction(performancePrediction)
                .insights(insights)
                .confidence(confidence)
                .recommendations(recommendations)
                .timestamp(LocalDateTime.now())
                .build();
            
            log.info("AI analysis completed with confidence: {}", confidence);
            return analysis;
            
        } catch (Exception e) {
            log.error("Error in AI analysis: {}", e.getMessage(), e);
            return createFallbackAnalysis(intent);
        }
    }
    
    private List<Pattern> recognizePatterns(UserIntent intent) {
        List<Pattern> patterns = new ArrayList<>();
        
        // Analyze historical patterns from memory
        List<MemoryEntry> historicalData = memoryManager.retrieveMemory("test_patterns");
        
        // Pattern 1: Service-specific patterns
        if (intent.getParameters().containsKey("services")) {
            List<String> services = (List<String>) intent.getParameters().get("services");
            for (String service : services) {
                Pattern servicePattern = Pattern.builder()
                    .name("service_" + service)
                    .description("Pattern for " + service + " testing")
                    .frequency(calculateServicePatternFrequency(service, historicalData))
                    .confidence(0.8)
                    .build();
                patterns.add(servicePattern);
            }
        }
        
        // Pattern 2: Test type patterns
        if (intent.getParameters().containsKey("testTypes")) {
            List<String> testTypes = (List<String>) intent.getParameters().get("testTypes");
            for (String testType : testTypes) {
                Pattern testTypePattern = Pattern.builder()
                    .name("test_type_" + testType)
                    .description("Pattern for " + testType + " execution")
                    .frequency(calculateTestTypePatternFrequency(testType, historicalData))
                    .confidence(0.75)
                    .build();
                patterns.add(testTypePattern);
            }
        }
        
        // Pattern 3: Time-based patterns
        Pattern timePattern = analyzeTimePattern(intent);
        if (timePattern != null) {
            patterns.add(timePattern);
        }
        
        // Pattern 4: Failure patterns
        Pattern failurePattern = analyzeFailurePattern(intent);
        if (failurePattern != null) {
            patterns.add(failurePattern);
        }
        
        return patterns;
    }
    
    private RiskAssessment assessRisk(UserIntent intent) {
        double riskScore = 0.0;
        List<String> riskFactors = new ArrayList<>();
        
        // Factor 1: Service complexity
        if (intent.getParameters().containsKey("services")) {
            List<String> services = (List<String>) intent.getParameters().get("services");
            for (String service : services) {
                double serviceRisk = calculateServiceRisk(service);
                riskScore += serviceRisk;
                if (serviceRisk > 0.5) {
                    riskFactors.add("High complexity in " + service);
                }
            }
        }
        
        // Factor 2: Test type risk
        if (intent.getParameters().containsKey("testTypes")) {
            List<String> testTypes = (List<String>) intent.getParameters().get("testTypes");
            for (String testType : testTypes) {
                double testTypeRisk = calculateTestTypeRisk(testType);
                riskScore += testTypeRisk;
                if (testTypeRisk > 0.6) {
                    riskFactors.add("High risk test type: " + testType);
                }
            }
        }
        
        // Factor 3: System health risk
        SystemHealth systemHealth = contextManager.getSystemHealth();
        if (systemHealth != null && systemHealth.getStatus() == HealthStatus.DEGRADED) {
            riskScore += 0.3;
            riskFactors.add("System health is degraded");
        }
        
        // Factor 4: Recent failures
        List<TestFailure> recentFailures = memoryManager.getRecentFailures();
        if (recentFailures.size() > 5) {
            riskScore += 0.2;
            riskFactors.add("Multiple recent failures detected");
        }
        
        // Normalize risk score
        riskScore = Math.min(riskScore, 1.0);
        
        // Determine risk level
        RiskLevel riskLevel = determineRiskLevel(riskScore);
        
        return RiskAssessment.builder()
            .riskScore(riskScore)
            .riskLevel(riskLevel)
            .riskFactors(riskFactors)
            .timestamp(LocalDateTime.now())
            .build();
    }
    
    private PerformancePrediction predictPerformance(UserIntent intent) {
        double estimatedTime = 0.0;
        double confidence = 0.7;
        List<String> factors = new ArrayList<>();
        
        // Factor 1: Test type performance
        if (intent.getParameters().containsKey("testTypes")) {
            List<String> testTypes = (List<String>) intent.getParameters().get("testTypes");
            for (String testType : testTypes) {
                double testTypeTime = estimateTestTypeTime(testType);
                estimatedTime += testTypeTime;
                factors.add(testType + " estimated time: " + testTypeTime + " minutes");
            }
        }
        
        // Factor 2: Service count impact
        if (intent.getParameters().containsKey("services")) {
            List<String> services = (List<String>) intent.getParameters().get("services");
            estimatedTime *= (1 + (services.size() - 1) * 0.2); // 20% overhead per additional service
            factors.add("Multi-service overhead: " + services.size() + " services");
        }
        
        // Factor 3: System load impact
        PerformanceMetrics metrics = contextManager.getPerformanceMetrics();
        if (metrics != null && metrics.getCpuUsage() > 80) {
            estimatedTime *= 1.5; // 50% slower under high load
            factors.add("High system load detected");
            confidence -= 0.1;
        }
        
        // Factor 4: Historical performance
        double historicalTime = getHistoricalPerformance(intent);
        if (historicalTime > 0) {
            estimatedTime = (estimatedTime + historicalTime) / 2; // Average with historical
            confidence += 0.1;
            factors.add("Historical data available");
        }
        
        return PerformancePrediction.builder()
            .estimatedTime(estimatedTime)
            .confidence(confidence)
            .factors(factors)
            .timestamp(LocalDateTime.now())
            .build();
    }
    
    private List<String> generateInsights(UserIntent intent, List<Pattern> patterns, 
                                        RiskAssessment riskAssessment, PerformancePrediction performancePrediction) {
        List<String> insights = new ArrayList<>();
        
        // Insight 1: Pattern-based insights
        if (!patterns.isEmpty()) {
            insights.add("Recognized " + patterns.size() + " relevant patterns from historical data");
        }
        
        // Insight 2: Risk-based insights
        if (riskAssessment.getRiskLevel() == RiskLevel.HIGH) {
            insights.add("High risk detected - consider running tests in isolation first");
        } else if (riskAssessment.getRiskLevel() == RiskLevel.MEDIUM) {
            insights.add("Moderate risk - monitor execution closely");
        }
        
        // Insight 3: Performance insights
        if (performancePrediction.getEstimatedTime() > 30) {
            insights.add("Long execution time expected - consider parallel execution");
        }
        
        // Insight 4: System health insights
        SystemHealth health = contextManager.getSystemHealth();
        if (health != null && health.getStatus() != HealthStatus.HEALTHY) {
            insights.add("System health issues detected - may impact test reliability");
        }
        
        // Insight 5: AI-generated insights using Spring AI
        try {
            String aiPrompt = buildInsightPrompt(intent, patterns, riskAssessment, performancePrediction);
            Prompt prompt = new Prompt(aiPrompt);
            ChatResponse response = chatClient.call(prompt);
            String aiInsight = response.getResult().getOutput().getContent();
            insights.add("AI Insight: " + aiInsight);
        } catch (Exception e) {
            log.warn("Could not generate AI insight: {}", e.getMessage());
        }
        
        return insights;
    }
    
    private List<Recommendation> generateRecommendations(UserIntent intent, RiskAssessment riskAssessment, 
                                                        PerformancePrediction performancePrediction) {
        List<Recommendation> recommendations = new ArrayList<>();
        
        // Recommendation 1: Risk mitigation
        if (riskAssessment.getRiskLevel() == RiskLevel.HIGH) {
            recommendations.add(Recommendation.builder()
                .type(RecommendationType.RISK_MITIGATION)
                .description("Run tests in isolated environment first")
                .priority(Priority.HIGH)
                .build());
        }
        
        // Recommendation 2: Performance optimization
        if (performancePrediction.getEstimatedTime() > 30) {
            recommendations.add(Recommendation.builder()
                .type(RecommendationType.PERFORMANCE_OPTIMIZATION)
                .description("Consider parallel test execution")
                .priority(Priority.MEDIUM)
                .build());
        }
        
        // Recommendation 3: Test scope optimization
        if (intent.getParameters().containsKey("scope") && 
            "full".equals(intent.getParameters().get("scope"))) {
            recommendations.add(Recommendation.builder()
                .type(RecommendationType.TEST_OPTIMIZATION)
                .description("Consider running smoke tests first")
                .priority(Priority.MEDIUM)
                .build());
        }
        
        return recommendations;
    }
    
    private double calculateConfidence(UserIntent intent, List<Pattern> patterns, 
                                     RiskAssessment riskAssessment, PerformancePrediction performancePrediction) {
        double confidence = 0.5; // Base confidence
        
        // Pattern confidence
        if (!patterns.isEmpty()) {
            confidence += 0.2;
        }
        
        // Intent confidence
        confidence += intent.getConfidence() * 0.2;
        
        // Risk assessment confidence
        if (riskAssessment.getRiskFactors().size() < 3) {
            confidence += 0.1;
        }
        
        // Performance prediction confidence
        confidence += performancePrediction.getConfidence() * 0.1;
        
        return Math.min(confidence, 1.0);
    }
    
    // Helper methods
    private double calculateServicePatternFrequency(String service, List<MemoryEntry> historicalData) {
        // Implementation for calculating service pattern frequency
        return 0.8; // Placeholder
    }
    
    private double calculateTestTypePatternFrequency(String testType, List<MemoryEntry> historicalData) {
        // Implementation for calculating test type pattern frequency
        return 0.7; // Placeholder
    }
    
    private Pattern analyzeTimePattern(UserIntent intent) {
        // Implementation for time pattern analysis
        return null; // Placeholder
    }
    
    private Pattern analyzeFailurePattern(UserIntent intent) {
        // Implementation for failure pattern analysis
        return null; // Placeholder
    }
    
    private double calculateServiceRisk(String service) {
        // Implementation for service risk calculation
        return 0.3; // Placeholder
    }
    
    private double calculateTestTypeRisk(String testType) {
        // Implementation for test type risk calculation
        return 0.4; // Placeholder
    }
    
    private RiskLevel determineRiskLevel(double riskScore) {
        if (riskScore >= 0.7) return RiskLevel.HIGH;
        if (riskScore >= 0.4) return RiskLevel.MEDIUM;
        return RiskLevel.LOW;
    }
    
    private double estimateTestTypeTime(String testType) {
        // Implementation for test type time estimation
        return 10.0; // Placeholder
    }
    
    private double getHistoricalPerformance(UserIntent intent) {
        // Implementation for historical performance lookup
        return 0.0; // Placeholder
    }
    
    private String buildInsightPrompt(UserIntent intent, List<Pattern> patterns, 
                                    RiskAssessment riskAssessment, PerformancePrediction performancePrediction) {
        return String.format("""
            Analyze this test execution request and provide one key insight:
            
            Intent: %s
            Patterns: %s
            Risk Level: %s
            Estimated Time: %.1f minutes
            
            Provide a concise, actionable insight that would help improve the test execution.
            """, 
            intent.getDescription(),
            patterns.size(),
            riskAssessment.getRiskLevel(),
            performancePrediction.getEstimatedTime()
        );
    }
    
    private AIAnalysis createFallbackAnalysis(UserIntent intent) {
        return AIAnalysis.builder()
            .intent(intent)
            .confidence(0.3)
            .insights(List.of("Limited analysis due to insufficient data"))
            .timestamp(LocalDateTime.now())
            .build();
    }
    
    private static void initializeRiskPatterns() {
        RISK_PATTERNS.put("performance_test", 0.8);
        RISK_PATTERNS.put("chaos_test", 0.9);
        RISK_PATTERNS.put("security_test", 0.7);
        RISK_PATTERNS.put("integration_test", 0.6);
        RISK_PATTERNS.put("unit_test", 0.2);
    }
    
    private static void initializePerformancePatterns() {
        PERFORMANCE_PATTERNS.put("performance_test", 30.0);
        PERFORMANCE_PATTERNS.put("load_test", 45.0);
        PERFORMANCE_PATTERNS.put("stress_test", 60.0);
        PERFORMANCE_PATTERNS.put("integration_test", 15.0);
        PERFORMANCE_PATTERNS.put("unit_test", 2.0);
    }
} 