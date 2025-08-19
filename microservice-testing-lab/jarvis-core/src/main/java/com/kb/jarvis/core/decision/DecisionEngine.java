package com.kb.jarvis.core.decision;

import com.kb.jarvis.core.model.*;
import com.kb.jarvis.core.context.ContextManager;
import com.kb.jarvis.core.memory.MemoryManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DecisionEngine {
    
    private static final Logger log = LoggerFactory.getLogger(DecisionEngine.class);
    
    @Autowired
    private ContextManager contextManager;
    
    @Autowired
    private MemoryManager memoryManager;
    
    @Value("${jarvis.decision.confidence-threshold:0.6}")
    private double confidenceThreshold;
    
    @Value("${jarvis.decision.max-parallel-actions:5}")
    private int maxParallelActions;
    
    // Action templates for different intent types
    private static final Map<IntentType, ActionTemplate> ACTION_TEMPLATES = new HashMap<>();
    
    // Priority weights for different factors
    private static final Map<String, Double> PRIORITY_WEIGHTS = new HashMap<>();
    
    static {
        initializeActionTemplates();
        initializePriorityWeights();
    }
    
    public DecisionAction decideAction(UserIntent intent, AIAnalysis analysis) {
        log.info("Making decision for intent: {} with confidence: {}", intent.getType(), analysis.getConfidence());
        
        try {
            // Step 1: Validate confidence threshold
            if (analysis.getConfidence() < confidenceThreshold) {
                return createLowConfidenceAction(intent, analysis);
            }
            
            // Step 2: Determine action type based on intent
            ActionType actionType = determineActionType(intent, analysis);
            
            // Step 3: Calculate priority
            Priority priority = calculatePriority(intent, analysis);
            
            // Step 4: Determine execution strategy
            ExecutionStrategy strategy = determineExecutionStrategy(intent, analysis);
            
            // Step 5: Generate action parameters
            Map<String, Object> parameters = generateActionParameters(intent, analysis);
            
            // Step 6: Estimate execution time
            String estimatedTime = estimateExecutionTime(intent, analysis);
            
            // Step 7: Check resource availability
            if (!checkResourceAvailability(actionType, strategy)) {
                return createResourceUnavailableAction(intent, analysis);
            }
            
            // Step 8: Generate action description
            String description = generateActionDescription(intent, actionType, analysis);
            
            // Convert parameters to TestParameters
            TestParameters testParameters = TestParameters.builder()
                .serviceName(intent.getServiceName())
                .testType(actionType.name())
                .endpoints(new ArrayList<>())
                .configuration(parameters)
                .scope(TestScope.FULL)
                .strategy(strategy)
                .build();
            
            DecisionAction action = DecisionAction.builder()
                .type(actionType)
                .description(description)
                .priority(priority)
                .executionStrategy(strategy)
                .parameters(testParameters)
                .estimatedTime(estimatedTime)
                .confidence(analysis.getConfidence())
                .timestamp(LocalDateTime.now())
                .build();
            
            log.info("Decision made: {} with priority: {}", actionType, priority);
            return action;
            
        } catch (Exception e) {
            log.error("Error in decision making: {}", e.getMessage(), e);
            return createFallbackAction(intent, analysis);
        }
    }
    
    private ActionType determineActionType(UserIntent intent, AIAnalysis analysis) {
        // Map intent types to action types
        switch (intent.getType()) {
            case RUN_TESTS:
                return determineTestActionType(intent, analysis);
            case ANALYZE_FAILURES:
                return ActionType.ANALYZE_FAILURES;
            case GENERATE_TESTS:
                return ActionType.GENERATE_TESTS;
            case OPTIMIZE_TESTS:
                return ActionType.OPTIMIZE_TESTS;
            case HEALTH_CHECK:
                return ActionType.HEALTH_CHECK;
            case GET_STATUS:
                return ActionType.MONITOR_SYSTEM;
            case HELP:
                return ActionType.GENERATE_REPORT;
            default:
                return ActionType.UNKNOWN;
        }
    }
    
    private ActionType determineTestActionType(UserIntent intent, AIAnalysis analysis) {
        // Determine specific test action based on parameters and risk assessment
        Map<String, Object> params = intent.getParameters();
        
        // Check for specific test types
        if (params.containsKey("testTypes")) {
            List<String> testTypes = (List<String>) params.get("testTypes");
            
            // High-risk tests get special handling
            if (analysis.getRiskAssessment().getRiskLevel() == RiskLevel.HIGH) {
                if (testTypes.contains("chaos_test") || testTypes.contains("performance_test")) {
                    return ActionType.RUN_ISOLATED_TESTS;
                }
            }
            
            // Performance tests
            if (testTypes.contains("performance_test") || testTypes.contains("load_test")) {
                return ActionType.RUN_PERFORMANCE_TESTS;
            }
            
            // Security tests
            if (testTypes.contains("security_test") || testTypes.contains("penetration_test")) {
                return ActionType.RUN_SECURITY_TESTS;
            }
            
            // Integration tests
            if (testTypes.contains("integration_test")) {
                return ActionType.RUN_INTEGRATION_TESTS;
            }
        }
        
        // Default to general test execution
        return ActionType.RUN_TESTS;
    }
    
    private Priority calculatePriority(UserIntent intent, AIAnalysis analysis) {
        double priorityScore = 0.0;
        
        // Factor 1: Risk level (higher risk = higher priority)
        RiskLevel riskLevel = analysis.getRiskAssessment().getRiskLevel();
        switch (riskLevel) {
            case HIGH:
                priorityScore += 0.4;
                break;
            case MEDIUM:
                priorityScore += 0.2;
                break;
            case LOW:
                priorityScore += 0.1;
                break;
        }
        
        // Factor 2: System health (poor health = higher priority)
        SystemHealth health = contextManager.getSystemHealth();
        if (health != null && health.getStatus() != HealthStatus.HEALTHY) {
            priorityScore += 0.3;
        }
        
        // Factor 3: Recent failures (more failures = higher priority)
        List<TestFailure> recentFailures = memoryManager.getRecentFailures();
        if (recentFailures.size() > 3) {
            priorityScore += 0.2;
        }
        
        // Factor 4: Time constraints
        if (intent.getParameters().containsKey("timeConstraint")) {
            String timeConstraint = (String) intent.getParameters().get("timeConstraint");
            if ("urgent".equals(timeConstraint) || "immediate".equals(timeConstraint)) {
                priorityScore += 0.3;
            }
        }
        
        // Factor 5: Scope (larger scope = higher priority)
        if (intent.getParameters().containsKey("scope")) {
            String scope = (String) intent.getParameters().get("scope");
            if ("full".equals(scope) || "comprehensive".equals(scope)) {
                priorityScore += 0.1;
            }
        }
        
        // Normalize and determine priority
        priorityScore = Math.min(priorityScore, 1.0);
        
        if (priorityScore >= 0.7) return Priority.HIGH;
        if (priorityScore >= 0.4) return Priority.MEDIUM;
        return Priority.LOW;
    }
    
    private ExecutionStrategy determineExecutionStrategy(UserIntent intent, AIAnalysis analysis) {
        // Determine execution strategy based on various factors
        
        // Strategy 1: Parallel execution for multiple services
        if (intent.getParameters().containsKey("services")) {
            List<String> services = (List<String>) intent.getParameters().get("services");
            if (services.size() > 2 && analysis.getPerformancePrediction().getEstimatedTime() > 20) {
                return ExecutionStrategy.PARALLEL;
            }
        }
        
        // Strategy 2: Sequential execution for high-risk tests
        if (analysis.getRiskAssessment().getRiskLevel() == RiskLevel.HIGH) {
            return ExecutionStrategy.SEQUENTIAL;
        }
        
        // Strategy 3: Isolated execution for chaos tests
        if (intent.getParameters().containsKey("testTypes")) {
            List<String> testTypes = (List<String>) intent.getParameters().get("testTypes");
            if (testTypes.contains("chaos_test")) {
                return ExecutionStrategy.ISOLATED;
            }
        }
        
        // Strategy 4: Adaptive execution based on system load
        PerformanceMetrics metrics = contextManager.getPerformanceMetrics();
        if (metrics != null && metrics.getCpuUsage() > 80) {
            return ExecutionStrategy.SEQUENTIAL; // Reduce load
        }
        
        // Default strategy
        return ExecutionStrategy.ADAPTIVE;
    }
    
    private Map<String, Object> generateActionParameters(UserIntent intent, AIAnalysis analysis) {
        Map<String, Object> parameters = new HashMap<>();
        
        // Copy intent parameters
        parameters.putAll(intent.getParameters());
        
        // Add AI-derived parameters
        parameters.put("riskLevel", analysis.getRiskAssessment().getRiskLevel());
        parameters.put("estimatedTime", analysis.getPerformancePrediction().getEstimatedTime());
        parameters.put("confidence", analysis.getConfidence());
        
        // Add execution parameters
        parameters.put("maxRetries", determineMaxRetries(analysis));
        parameters.put("timeout", determineTimeout(analysis));
        parameters.put("parallelism", determineParallelism(intent, analysis));
        
        // Add monitoring parameters
        parameters.put("enableMonitoring", true);
        parameters.put("alertThreshold", determineAlertThreshold(analysis));
        
        return parameters;
    }
    
    private String estimateExecutionTime(UserIntent intent, AIAnalysis analysis) {
        double estimatedMinutes = analysis.getPerformancePrediction().getEstimatedTime();
        
        if (estimatedMinutes < 1) {
            return "Less than 1 minute";
        } else if (estimatedMinutes < 60) {
            return String.format("%.0f minutes", estimatedMinutes);
        } else {
            int hours = (int) (estimatedMinutes / 60);
            int minutes = (int) (estimatedMinutes % 60);
            if (minutes == 0) {
                return String.format("%d hours", hours);
            } else {
                return String.format("%d hours %d minutes", hours, minutes);
            }
        }
    }
    
    private boolean checkResourceAvailability(ActionType actionType, ExecutionStrategy strategy) {
        // Check if system has resources for the action
        
        // Check active tests count
        List<ActiveTest> activeTests = memoryManager.getActiveTests();
        if (activeTests.size() >= maxParallelActions) {
            return false;
        }
        
        // Check system resources
        PerformanceMetrics metrics = contextManager.getPerformanceMetrics();
        if (metrics != null) {
            // High CPU usage
            if (metrics.getCpuUsage() > 90) {
                return false;
            }
            
            // High memory usage
            if (metrics.getMemoryUsage() > 85) {
                return false;
            }
        }
        
        // Check for conflicting actions
        if (actionType == ActionType.RUN_CHAOS_TESTS) {
            // Don't run chaos tests if other critical tests are running
            for (ActiveTest test : activeTests) {
                if (test.getType() == TestType.INTEGRATION_TEST || 
                    test.getType() == TestType.PERFORMANCE_TEST) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    private String generateActionDescription(UserIntent intent, ActionType actionType, AIAnalysis analysis) {
        StringBuilder description = new StringBuilder();
        
        switch (actionType) {
            case RUN_TESTS:
                description.append("Executing test suite");
                break;
            case RUN_PERFORMANCE_TESTS:
                description.append("Running performance tests");
                break;
            case RUN_SECURITY_TESTS:
                description.append("Executing security tests");
                break;
            case RUN_INTEGRATION_TESTS:
                description.append("Running integration tests");
                break;
            case RUN_ISOLATED_TESTS:
                description.append("Executing tests in isolated environment");
                break;
            case ANALYZE_FAILURES:
                description.append("Analyzing recent test failures");
                break;
            case GENERATE_TESTS:
                description.append("Generating new test cases");
                break;
            case OPTIMIZE_TESTS:
                description.append("Optimizing test suite");
                break;
            case HEALTH_CHECK:
                description.append("Performing system health check");
                break;
            case MONITOR_SYSTEM:
                description.append("Monitoring system performance");
                break;
            case GENERATE_REPORT:
                description.append("Generating comprehensive report");
                break;
            default:
                description.append("Processing request");
        }
        
        // Add context information
        if (intent.getParameters().containsKey("services")) {
            List<String> services = (List<String>) intent.getParameters().get("services");
            description.append(" for ").append(String.join(", ", services));
        }
        
        // Add risk information
        if (analysis.getRiskAssessment().getRiskLevel() == RiskLevel.HIGH) {
            description.append(" (high-risk operation)");
        }
        
        return description.toString();
    }
    
    // Helper methods
    private int determineMaxRetries(AIAnalysis analysis) {
        if (analysis.getRiskAssessment().getRiskLevel() == RiskLevel.HIGH) {
            return 1; // Fewer retries for high-risk operations
        }
        return 3; // Default retries
    }
    
    private int determineTimeout(AIAnalysis analysis) {
        double estimatedTime = analysis.getPerformancePrediction().getEstimatedTime();
        return (int) (estimatedTime * 1.5 * 60); // 1.5x estimated time in seconds
    }
    
    private int determineParallelism(UserIntent intent, AIAnalysis analysis) {
        if (analysis.getRiskAssessment().getRiskLevel() == RiskLevel.HIGH) {
            return 1; // Sequential for high risk
        }
        
        if (intent.getParameters().containsKey("services")) {
            List<String> services = (List<String>) intent.getParameters().get("services");
            return Math.min(services.size(), 3); // Max 3 parallel executions
        }
        
        return 2; // Default parallelism
    }
    
    private double determineAlertThreshold(AIAnalysis analysis) {
        if (analysis.getRiskAssessment().getRiskLevel() == RiskLevel.HIGH) {
            return 0.1; // Alert on 10% failure rate for high-risk tests
        }
        return 0.2; // Default 20% failure rate threshold
    }
    
    private DecisionAction createLowConfidenceAction(UserIntent intent, AIAnalysis analysis) {
        return DecisionAction.builder()
            .type(ActionType.REQUEST_CLARIFICATION)
            .description("Requesting clarification due to low confidence in understanding")
            .priority(Priority.LOW)
            .confidence(analysis.getConfidence())
            .timestamp(LocalDateTime.now())
            .build();
    }
    
    private DecisionAction createResourceUnavailableAction(UserIntent intent, AIAnalysis analysis) {
        return DecisionAction.builder()
            .type(ActionType.QUEUE_ACTION)
            .description("Queuing action due to insufficient resources")
            .priority(calculatePriority(intent, analysis))
            .confidence(analysis.getConfidence())
            .timestamp(LocalDateTime.now())
            .build();
    }
    
    private DecisionAction createFallbackAction(UserIntent intent, AIAnalysis analysis) {
        return DecisionAction.builder()
            .type(ActionType.UNKNOWN)
            .description("Fallback action due to decision engine error")
            .priority(Priority.LOW)
            .confidence(0.1)
            .timestamp(LocalDateTime.now())
                .build();
    }
    
    private static void initializeActionTemplates() {
        // Initialize action templates for different intent types
        ACTION_TEMPLATES.put(IntentType.RUN_TESTS, new ActionTemplate(ActionType.RUN_TESTS, 0.8));
        ACTION_TEMPLATES.put(IntentType.ANALYZE_FAILURES, new ActionTemplate(ActionType.ANALYZE_FAILURES, 0.9));
        ACTION_TEMPLATES.put(IntentType.GENERATE_TESTS, new ActionTemplate(ActionType.GENERATE_TESTS, 0.7));
        ACTION_TEMPLATES.put(IntentType.OPTIMIZE_TESTS, new ActionTemplate(ActionType.OPTIMIZE_TESTS, 0.6));
        ACTION_TEMPLATES.put(IntentType.HEALTH_CHECK, new ActionTemplate(ActionType.HEALTH_CHECK, 0.9));
    }
    
    private static void initializePriorityWeights() {
        PRIORITY_WEIGHTS.put("risk_level", 0.4);
        PRIORITY_WEIGHTS.put("system_health", 0.3);
        PRIORITY_WEIGHTS.put("recent_failures", 0.2);
        PRIORITY_WEIGHTS.put("time_constraint", 0.3);
        PRIORITY_WEIGHTS.put("scope", 0.1);
    }
    
    // Helper class for action templates
    private static class ActionTemplate {
        private final ActionType actionType;
        private final double confidence;
        
        public ActionTemplate(ActionType actionType, double confidence) {
            this.actionType = actionType;
            this.confidence = confidence;
        }
        
        public ActionType getActionType() { return actionType; }
        public double getConfidence() { return confidence; }
    }
} 