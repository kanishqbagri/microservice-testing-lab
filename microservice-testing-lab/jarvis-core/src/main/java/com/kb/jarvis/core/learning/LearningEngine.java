package com.kb.jarvis.core.learning;

import com.kb.jarvis.core.model.*;
import com.kb.jarvis.core.memory.MemoryManager;
import com.kb.jarvis.core.context.ContextManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class LearningEngine {
    
    private static final Logger log = LoggerFactory.getLogger(LearningEngine.class);
    
    @Autowired
    private MemoryManager memoryManager;
    
    @Autowired
    private ContextManager contextManager;
    
    @Value("${jarvis.learning.min-data-points:10}")
    private int minDataPoints;
    
    @Value("${jarvis.learning.confidence-threshold:0.7}")
    private double confidenceThreshold;
    
    @Value("${jarvis.learning.analysis-interval:300000}") // 5 minutes
    private long analysisInterval;
    
    // Learning cache
    private final Map<String, Double> patternConfidence = new ConcurrentHashMap<>();
    private final Map<String, Trend> trendCache = new ConcurrentHashMap<>();
    private final Map<String, Optimization> optimizationCache = new ConcurrentHashMap<>();
    
    // Learning insights cache
    private final AtomicReference<LearningInsights> currentInsights = new AtomicReference<>();
    
    public void learnFromInteraction(UserIntent intent, AIAnalysis analysis, DecisionAction action) {
        log.debug("Learning from interaction: {} -> {}", intent.getType(), action.getType());
        
        try {
            // Step 1: Store interaction data
            storeInteractionData(intent, analysis, action);
            
            // Step 2: Analyze patterns
            analyzePatterns(intent, analysis, action);
            
            // Step 3: Update trends
            updateTrends(intent, analysis, action);
            
            // Step 4: Generate optimizations
            generateOptimizations(intent, analysis, action);
            
            // Step 5: Update learning insights
            updateLearningInsights();
            
        } catch (Exception e) {
            log.error("Error learning from interaction: {}", e.getMessage(), e);
        }
    }
    
    public LearningInsights getInsights() {
        return currentInsights.get();
    }
    
    private void storeInteractionData(UserIntent intent, AIAnalysis analysis, DecisionAction action) {
        // Create learning data entry
        Map<String, Object> data = new HashMap<>();
        data.put("intentType", intent.getType().toString());
        data.put("intentConfidence", intent.getConfidence());
        data.put("aiConfidence", analysis.getConfidence());
        data.put("actionType", action.getType().toString());
        data.put("actionPriority", action.getPriority().toString());
        data.put("riskLevel", analysis.getRiskAssessment().getRiskLevel().toString());
        data.put("estimatedTime", analysis.getPerformancePrediction().getEstimatedTime());
        
        LearningData learningData = LearningData.builder()
            .type(LearningDataType.INTERACTION)
            .data(data)
            .insights("Interaction: " + intent.getType() + " -> " + action.getType())
            .timestamp(LocalDateTime.now())
            .build();
        
        memoryManager.addLearningData(learningData);
    }
    
    private void analyzePatterns(UserIntent intent, AIAnalysis analysis, DecisionAction action) {
        // Pattern 1: Intent-Action patterns
        String intentActionKey = intent.getType() + "_" + action.getType();
        updatePatternConfidence(intentActionKey, analysis.getConfidence());
        
        // Pattern 2: Risk-based patterns
        String riskPatternKey = "risk_" + analysis.getRiskAssessment().getRiskLevel();
        updatePatternConfidence(riskPatternKey, analysis.getConfidence());
        
        // Pattern 3: Service-specific patterns
        if (intent.getParameters().containsKey("services")) {
            List<String> services = (List<String>) intent.getParameters().get("services");
            for (String service : services) {
                String servicePatternKey = "service_" + service + "_" + action.getType();
                updatePatternConfidence(servicePatternKey, analysis.getConfidence());
            }
        }
        
        // Pattern 4: Test type patterns
        if (intent.getParameters().containsKey("testTypes")) {
            List<String> testTypes = (List<String>) intent.getParameters().get("testTypes");
            for (String testType : testTypes) {
                String testTypePatternKey = "testtype_" + testType + "_" + action.getType();
                updatePatternConfidence(testTypePatternKey, analysis.getConfidence());
            }
        }
        
        // Store high-confidence patterns
        for (Map.Entry<String, Double> entry : patternConfidence.entrySet()) {
            if (entry.getValue() >= confidenceThreshold) {
                Pattern pattern = Pattern.builder()
                    .name(entry.getKey())
                    .description("Learned pattern: " + entry.getKey())
                    .frequency(entry.getValue())
                    .confidence(entry.getValue())
                    .build();
                
                memoryManager.storePattern(pattern);
            }
        }
    }
    
    private void updateTrends(UserIntent intent, AIAnalysis analysis, DecisionAction action) {
        LocalDateTime now = LocalDateTime.now();
        
        // Trend 1: Success rate trends
        updateSuccessRateTrend(now);
        
        // Trend 2: Performance trends
        updatePerformanceTrend(now);
        
        // Trend 3: Risk level trends
        updateRiskTrend(now);
        
        // Trend 4: Service usage trends
        updateServiceUsageTrend(now);
    }
    
    private void generateOptimizations(UserIntent intent, AIAnalysis analysis, DecisionAction action) {
        // Optimization 1: Test execution optimization
        generateTestExecutionOptimizations();
        
        // Optimization 2: Resource allocation optimization
        generateResourceOptimizations();
        
        // Optimization 3: Risk mitigation optimization
        generateRiskOptimizations();
        
        // Optimization 4: Performance optimization
        generatePerformanceOptimizations();
    }
    
    private void updatePatternConfidence(String patternKey, double confidence) {
        Double currentConfidence = patternConfidence.get(patternKey);
        if (currentConfidence == null) {
            patternConfidence.put(patternKey, confidence);
        } else {
            // Exponential moving average
            double alpha = 0.3; // Learning rate
            double newConfidence = alpha * confidence + (1 - alpha) * currentConfidence;
            patternConfidence.put(patternKey, newConfidence);
        }
    }
    
    private void updateSuccessRateTrend(LocalDateTime now) {
        List<TestResult> recentResults = memoryManager.getTestResults();
        
        if (recentResults.size() < minDataPoints) {
            return;
        }
        
        // Calculate success rate for different time periods
        LocalDateTime oneHourAgo = now.minus(1, ChronoUnit.HOURS);
        LocalDateTime oneDayAgo = now.minus(1, ChronoUnit.DAYS);
        LocalDateTime oneWeekAgo = now.minus(7, ChronoUnit.DAYS);
        
        double recentSuccessRate = calculateSuccessRate(recentResults, oneHourAgo, now);
        double dailySuccessRate = calculateSuccessRate(recentResults, oneDayAgo, now);
        double weeklySuccessRate = calculateSuccessRate(recentResults, oneWeekAgo, now);
        
        // Determine trend direction
        TrendDirection direction = determineTrendDirection(recentSuccessRate, dailySuccessRate, weeklySuccessRate);
        
        Trend trend = Trend.builder()
            .name("test_success_rate")
            .description("Test success rate trend")
            .currentValue(recentSuccessRate)
            .previousValue(dailySuccessRate)
            .direction(direction)
            .confidence(0.8)
            .build();
        
        trendCache.put("success_rate", trend);
    }
    
    private void updatePerformanceTrend(LocalDateTime now) {
        List<TestResult> recentResults = memoryManager.getTestResults();
        
        if (recentResults.size() < minDataPoints) {
            return;
        }
        
        // Calculate average execution time
        LocalDateTime oneHourAgo = now.minus(1, ChronoUnit.HOURS);
        LocalDateTime oneDayAgo = now.minus(1, ChronoUnit.DAYS);
        
        double recentAvgTime = calculateAverageExecutionTime(recentResults, oneHourAgo, now);
        double dailyAvgTime = calculateAverageExecutionTime(recentResults, oneDayAgo, now);
        
        TrendDirection direction = recentAvgTime < dailyAvgTime ? TrendDirection.IMPROVING : TrendDirection.DEGRADING;
        
        Trend trend = Trend.builder()
            .name("execution_time")
            .description("Test execution time trend")
            .currentValue(recentAvgTime)
            .previousValue(dailyAvgTime)
            .direction(direction)
            .confidence(0.7)
            .build();
        
        trendCache.put("execution_time", trend);
    }
    
    private void updateRiskTrend(LocalDateTime now) {
        List<TestFailure> recentFailures = memoryManager.getRecentFailures();
        
        if (recentFailures.size() < minDataPoints / 2) {
            return;
        }
        
        // Calculate failure rate
        LocalDateTime oneHourAgo = now.minus(1, ChronoUnit.HOURS);
        LocalDateTime oneDayAgo = now.minus(1, ChronoUnit.DAYS);
        
        double recentFailureRate = calculateFailureRate(recentFailures, oneHourAgo, now);
        double dailyFailureRate = calculateFailureRate(recentFailures, oneDayAgo, now);
        
        TrendDirection direction = recentFailureRate < dailyFailureRate ? TrendDirection.IMPROVING : TrendDirection.DEGRADING;
        
        Trend trend = Trend.builder()
            .name("failure_rate")
            .description("Test failure rate trend")
            .currentValue(recentFailureRate)
            .previousValue(dailyFailureRate)
            .direction(direction)
            .confidence(0.6)
            .build();
        
        trendCache.put("failure_rate", trend);
    }
    
    private void updateServiceUsageTrend(LocalDateTime now) {
        List<TestResult> recentResults = memoryManager.getTestResults();
        
        if (recentResults.size() < minDataPoints) {
            return;
        }
        
        // Calculate service usage patterns
        Map<String, Long> serviceUsage = recentResults.stream()
            .collect(Collectors.groupingBy(TestResult::getServiceName, Collectors.counting()));
        
        // Find most and least used services
        String mostUsedService = serviceUsage.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("unknown");
        
        String leastUsedService = serviceUsage.entrySet().stream()
            .min(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("unknown");
        
        // Store service usage insights
        Map<String, Object> data = new HashMap<>();
        data.put("mostUsedService", mostUsedService);
        data.put("leastUsedService", leastUsedService);
        data.put("serviceUsage", serviceUsage);
        
        LearningData learningData = LearningData.builder()
            .type(LearningDataType.SERVICE_USAGE)
            .data(data)
            .insights("Service usage analysis: " + mostUsedService + " most used, " + leastUsedService + " least used")
            .timestamp(now)
            .build();
        
        memoryManager.addLearningData(learningData);
    }
    
    private void generateTestExecutionOptimizations() {
        List<TestResult> results = memoryManager.getTestResults();
        
        if (results.size() < minDataPoints) {
            return;
        }
        
        // Optimization 1: Parallel execution for fast tests
        long fastTests = results.stream()
            .filter(result -> result.getDuration() < 5.0) // Less than 5 seconds
            .count();
        
        if (fastTests > results.size() * 0.3) { // More than 30% are fast tests
            Optimization optimization = Optimization.builder()
                .name("parallel_fast_tests")
                .description("Consider parallel execution for fast tests to improve overall execution time")
                .impact("HIGH")
                .confidence(0.8)
                .build();
            
            optimizationCache.put("parallel_fast_tests", optimization);
        }
        
        // Optimization 2: Sequential execution for slow tests
        long slowTests = results.stream()
            .filter(result -> result.getDuration() > 30.0) // More than 30 seconds
            .count();
        
        if (slowTests > results.size() * 0.2) { // More than 20% are slow tests
            Optimization optimization = Optimization.builder()
                .name("sequential_slow_tests")
                .description("Consider sequential execution for slow tests to reduce resource contention")
                .impact("MEDIUM")
                .confidence(0.7)
                .build();
            
            optimizationCache.put("sequential_slow_tests", optimization);
        }
    }
    
    private void generateResourceOptimizations() {
        PerformanceMetrics metrics = contextManager.getPerformanceMetrics();
        
        if (metrics != null) {
            // Optimization 1: High CPU usage
            if (metrics.getCpuUsage() > 80) {
                Optimization optimization = Optimization.builder()
                    .name("reduce_parallelism")
                    .description("High CPU usage detected - consider reducing parallel test execution")
                    .impact("HIGH")
                    .confidence(0.9)
                    .build();
                
                optimizationCache.put("reduce_parallelism", optimization);
            }
            
            // Optimization 2: High memory usage
            if (metrics.getMemoryUsage() > 85) {
                Optimization optimization = Optimization.builder()
                    .name("memory_optimization")
                    .description("High memory usage detected - consider running fewer concurrent tests")
                    .impact("HIGH")
                    .confidence(0.8)
                    .build();
                
                optimizationCache.put("memory_optimization", optimization);
            }
        }
    }
    
    private void generateRiskOptimizations() {
        List<TestFailure> failures = memoryManager.getRecentFailures();
        
        if (failures.size() > 5) {
            // Group failures by type
            Map<FailureType, Long> failureTypes = failures.stream()
                .collect(Collectors.groupingBy(TestFailure::getFailureType, Collectors.counting()));
            
            // Find most common failure type
            FailureType mostCommonFailure = failureTypes.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(FailureType.UNKNOWN);
            
            Optimization optimization = Optimization.builder()
                .name("failure_pattern_" + mostCommonFailure)
                .description("High frequency of " + mostCommonFailure + " failures - consider preventive measures")
                .impact("HIGH")
                .confidence(0.8)
                .build();
            
            optimizationCache.put("failure_pattern_" + mostCommonFailure, optimization);
        }
    }
    
    private void generatePerformanceOptimizations() {
        List<TestResult> results = memoryManager.getTestResults();
        
        if (results.size() < minDataPoints) {
            return;
        }
        
        // Calculate average execution time by service
        Map<String, Double> avgTimeByService = results.stream()
            .collect(Collectors.groupingBy(
                TestResult::getServiceName,
                Collectors.averagingDouble(TestResult::getDuration)
            ));
        
        // Find slowest service
        String slowestService = avgTimeByService.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("unknown");
        
        double slowestAvgTime = avgTimeByService.getOrDefault(slowestService, 0.0);
        
        if (slowestAvgTime > 20.0) { // More than 20 seconds average
            Optimization optimization = Optimization.builder()
                .name("optimize_" + slowestService)
                .description(slowestService + " has high average execution time - consider optimization")
                .impact("MEDIUM")
                .confidence(0.7)
                .build();
            
            optimizationCache.put("optimize_" + slowestService, optimization);
        }
    }
    
    private void updateLearningInsights() {
        List<String> insights = new ArrayList<>();
        
        // Add pattern insights
        for (Map.Entry<String, Double> entry : patternConfidence.entrySet()) {
            if (entry.getValue() >= confidenceThreshold) {
                insights.add("Strong pattern detected: " + entry.getKey() + " (confidence: " + 
                    String.format("%.2f", entry.getValue()) + ")");
            }
        }
        
        // Add trend insights
        for (Trend trend : trendCache.values()) {
            insights.add("Trend: " + trend.getName() + " is " + trend.getDirection().toString().toLowerCase() + 
                " (current: " + String.format("%.2f", trend.getCurrentValue()) + 
                ", previous: " + String.format("%.2f", trend.getPreviousValue()) + ")");
        }
        
        // Add optimization insights
        for (Optimization optimization : optimizationCache.values()) {
            insights.add("Optimization: " + optimization.getDescription() + " (impact: " + optimization.getImpact() + ")");
        }
        
        LearningInsights learningInsights = LearningInsights.builder()
            .insights(insights)
            .timestamp(LocalDateTime.now())
            .build();
        
        currentInsights.set(learningInsights);
    }
    
    // Helper methods
    private double calculateSuccessRate(List<TestResult> results, LocalDateTime start, LocalDateTime end) {
        List<TestResult> filteredResults = results.stream()
            .filter(result -> !result.getTimestamp().isBefore(start) && !result.getTimestamp().isAfter(end))
            .collect(Collectors.toList());
        
        if (filteredResults.isEmpty()) {
            return 0.0;
        }
        
        long successfulTests = filteredResults.stream()
            .filter(result -> result.getStatus() == TestStatus.PASSED)
            .count();
        
        return (double) successfulTests / filteredResults.size();
    }
    
    private double calculateAverageExecutionTime(List<TestResult> results, LocalDateTime start, LocalDateTime end) {
        List<TestResult> filteredResults = results.stream()
            .filter(result -> !result.getTimestamp().isBefore(start) && !result.getTimestamp().isAfter(end))
            .collect(Collectors.toList());
        
        if (filteredResults.isEmpty()) {
            return 0.0;
        }
        
        return filteredResults.stream()
            .mapToDouble(TestResult::getDuration)
            .average()
            .orElse(0.0);
    }
    
    private double calculateFailureRate(List<TestFailure> failures, LocalDateTime start, LocalDateTime end) {
        List<TestFailure> filteredFailures = failures.stream()
            .filter(failure -> !failure.getTimestamp().isBefore(start) && !failure.getTimestamp().isAfter(end))
            .collect(Collectors.toList());
        
        if (filteredFailures.isEmpty()) {
            return 0.0;
        }
        
        // Calculate failure rate as failures per hour
        long hours = ChronoUnit.HOURS.between(start, end);
        return hours > 0 ? (double) filteredFailures.size() / hours : 0.0;
    }
    
    private TrendDirection determineTrendDirection(double current, double previous, double baseline) {
        if (current > previous && previous > baseline) {
            return TrendDirection.DEGRADING;
        } else if (current < previous && previous < baseline) {
            return TrendDirection.IMPROVING;
        } else if (current > previous) {
            return TrendDirection.DEGRADING;
        } else {
            return TrendDirection.IMPROVING;
        }
    }
    
    /**
     * Scheduled analysis to update learning insights
     */
    @Scheduled(fixedDelayString = "${jarvis.learning.analysis-interval:300000}")
    public void scheduledAnalysis() {
        log.debug("Running scheduled learning analysis");
        
        try {
            LocalDateTime now = LocalDateTime.now();
            
            // Update all trends
            updateSuccessRateTrend(now);
            updatePerformanceTrend(now);
            updateRiskTrend(now);
            updateServiceUsageTrend(now);
            
            // Generate optimizations
            generateTestExecutionOptimizations();
            generateResourceOptimizations();
            generateRiskOptimizations();
            generatePerformanceOptimizations();
            
            // Update insights
            updateLearningInsights();
            
        } catch (Exception e) {
            log.error("Error in scheduled learning analysis: {}", e.getMessage(), e);
        }
    }
} 