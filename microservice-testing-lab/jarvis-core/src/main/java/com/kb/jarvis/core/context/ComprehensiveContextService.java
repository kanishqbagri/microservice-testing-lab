package com.kb.jarvis.core.context;

import com.kb.jarvis.core.model.*;
import com.kb.jarvis.core.nlp.EnhancedNLPEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Comprehensive Context Service
 * Analyzes commands and provides complete context understanding for all test types, services, and actions
 */
@Service
public class ComprehensiveContextService {
    
    private static final Logger log = LoggerFactory.getLogger(ComprehensiveContextService.class);
    
    @Autowired
    private EnhancedNLPEngine enhancedNLPEngine;
    
    @Autowired
    private ContextMappingService contextMappingService;
    
    @Autowired
    private DependencyAnalysisService dependencyAnalysisService;
    
    // Test Type Context Mapping
    private final Map<TestType, TestTypeContext> testTypeContexts = new HashMap<>();
    
    // Service Context Mapping  
    private final Map<String, ServiceContext> serviceContexts = new HashMap<>();
    
    // Action Context Mapping
    private final Map<ActionType, ActionContext> actionContexts = new HashMap<>();
    
    public ComprehensiveContextService() {
        initializeContextMappings();
    }
    
    /**
     * Analyze a command and provide comprehensive context understanding
     */
    public ComprehensiveContext analyzeCommand(String command) {
        log.info("Analyzing command: {}", command);
        
        try {
            // 1. Parse command using enhanced NLP
            ParsedCommand parsed = enhancedNLPEngine.parseCommand(command);
            log.debug("Parsed command: {}", parsed);
            
            // 2. Identify all test types mentioned
            List<TestType> testTypes = identifyTestTypes(parsed);
            log.debug("Identified test types: {}", testTypes);
            
            // 3. Identify all services mentioned
            List<String> services = identifyServices(parsed);
            log.debug("Identified services: {}", services);
            
            // 4. Identify all actions mentioned
            List<ActionType> actions = identifyActions(parsed);
            log.debug("Identified actions: {}", actions);
            
            // 5. Analyze dependencies and relationships
            DependencyGraph dependencies = dependencyAnalysisService.analyzeDependencies(testTypes, services, actions);
            log.debug("Dependency analysis: {}", dependencies);
            
            // 6. Generate execution plan
            ExecutionPlan executionPlan = generateExecutionPlan(testTypes, services, actions, dependencies);
            log.debug("Execution plan: {}", executionPlan);
            
            // 7. Assess risks
            RiskAssessment riskAssessment = assessRisks(testTypes, services, actions);
            log.debug("Risk assessment: {}", riskAssessment);
            
            // 8. Calculate resource requirements
            ResourceRequirements resourceRequirements = calculateResourceRequirements(testTypes, services, actions);
            log.debug("Resource requirements: {}", resourceRequirements);
            
            // 9. Estimate duration
            String estimatedDuration = estimateDuration(testTypes, services, actions);
            log.debug("Estimated duration: {}", estimatedDuration);
            
            // 10. Generate warnings and suggestions
            List<String> warnings = generateWarnings(testTypes, services, actions, riskAssessment);
            List<String> suggestions = generateSuggestions(testTypes, services, actions, dependencies);
            
            // 11. Calculate overall confidence
            double confidence = calculateOverallConfidence(parsed, testTypes, services, actions);
            
            // 12. Create comprehensive context
            ComprehensiveContext context = ComprehensiveContext.builder()
                .command(command)
                .parsedCommand(parsed)
                .testTypes(testTypes)
                .services(services)
                .actions(actions)
                .dependencies(dependencies)
                .executionPlan(executionPlan)
                .riskAssessment(riskAssessment)
                .resourceRequirements(resourceRequirements)
                .estimatedDuration(estimatedDuration)
                .warnings(warnings)
                .suggestions(suggestions)
                .confidence(confidence)
                .build();
            
            log.info("Comprehensive context analysis completed with confidence: {}", confidence);
            return context;
            
        } catch (Exception e) {
            log.error("Error analyzing command: {}", e.getMessage(), e);
            return createErrorContext(command, e.getMessage());
        }
    }
    
    /**
     * Identify test types from parsed command
     */
    private List<TestType> identifyTestTypes(ParsedCommand parsed) {
        List<TestType> testTypes = new ArrayList<>();
        
        // Get test types from parsed command
        if (parsed.getTestTypes() != null) {
            testTypes.addAll(parsed.getTestTypes());
        }
        
        // If no test types specified, infer from context
        if (testTypes.isEmpty()) {
            testTypes = inferTestTypesFromContext(parsed);
        }
        
        return testTypes;
    }
    
    /**
     * Identify services from parsed command
     */
    private List<String> identifyServices(ParsedCommand parsed) {
        List<String> services = new ArrayList<>();
        
        // Get services from parsed command
        if (parsed.getServices() != null) {
            services.addAll(parsed.getServices());
        }
        
        // If no services specified, infer from context
        if (services.isEmpty()) {
            services = inferServicesFromContext(parsed);
        }
        
        return services;
    }
    
    /**
     * Identify actions from parsed command
     */
    private List<ActionType> identifyActions(ParsedCommand parsed) {
        List<ActionType> actions = new ArrayList<>();
        
        // Get actions from parsed intents
        if (parsed.getIntents() != null) {
            for (IntentType intent : parsed.getIntents()) {
                ActionType action = mapIntentToAction(intent);
                if (action != null) {
                    actions.add(action);
                }
            }
        }
        
        // If no actions specified, infer from context
        if (actions.isEmpty()) {
            actions = inferActionsFromContext(parsed);
        }
        
        return actions;
    }
    
    /**
     * Generate execution plan based on test types, services, and actions
     */
    private ExecutionPlan generateExecutionPlan(List<TestType> testTypes, List<String> services, 
                                               List<ActionType> actions, DependencyGraph dependencies) {
        List<ExecutionStep> steps = new ArrayList<>();
        
        // Create execution steps for each combination
        for (ActionType action : actions) {
            for (String service : services) {
                for (TestType testType : testTypes) {
                    ExecutionStep step = ExecutionStep.builder()
                        .stepId(generateStepId(action, service, testType))
                        .stepName(generateStepName(action, service, testType))
                        .actionType(action)
                        .serviceName(service)
                        .testType(testType)
                        .parameters(generateStepParameters(action, service, testType))
                        .dependencies(getStepDependencies(service, dependencies))
                        .estimatedDuration(estimateStepDuration(action, service, testType))
                        .status("PENDING")
                        .build();
                    
                    steps.add(step);
                }
            }
        }
        
        // Determine execution order
        String executionOrder = determineExecutionOrder(steps, dependencies);
        
        // Calculate total duration
        String totalDuration = calculateTotalDuration(steps);
        
        return ExecutionPlan.builder()
            .steps(steps)
            .executionOrder(executionOrder)
            .estimatedDuration(totalDuration)
            .executionStrategy(determineExecutionStrategy(testTypes, services, actions))
            .build();
    }
    
    /**
     * Assess risks for the command execution
     */
    private RiskAssessment assessRisks(List<TestType> testTypes, List<String> services, List<ActionType> actions) {
        List<String> riskFactors = new ArrayList<>();
        Map<String, RiskLevel> riskLevels = new HashMap<>();
        List<String> mitigationStrategies = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        
        // Assess risks for each test type
        for (TestType testType : testTypes) {
            TestTypeContext context = testTypeContexts.get(testType);
            if (context != null) {
                // Convert string risk level to enum
                RiskLevel riskLevel = convertStringToRiskLevel(context.getRiskLevel());
                riskLevels.put(testType.name(), riskLevel);
                if (RiskLevel.HIGH.equals(riskLevel)) {
                    riskFactors.add("High risk test type: " + testType.getDisplayName());
                    warnings.add("HIGH RISK: " + testType.getDisplayName() + " may cause system disruption");
                }
            }
        }
        
        // Assess risks for each service
        for (String service : services) {
            ServiceContext context = serviceContexts.get(service);
            if (context != null) {
                if ("HIGH".equals(context.getCriticality())) {
                    riskFactors.add("Critical service: " + service);
                    warnings.add("CRITICAL SERVICE: " + service + " is essential for system operation");
                }
            }
        }
        
        // Assess risks for each action
        for (ActionType action : actions) {
            ActionContext context = actionContexts.get(action);
            if (context != null) {
                RiskLevel riskLevel = convertStringToRiskLevel(context.getRiskLevel());
                riskLevels.put(action.name(), riskLevel);
                if (RiskLevel.HIGH.equals(riskLevel)) {
                    riskFactors.add("High risk action: " + action.name());
                    warnings.add("HIGH RISK ACTION: " + action.name() + " may have significant impact");
                }
            }
        }
        
        // Determine overall risk level
        RiskLevel overallRiskLevel = determineOverallRiskLevel(riskLevels);
        
        // Generate mitigation strategies
        mitigationStrategies = generateMitigationStrategies(riskFactors, riskLevels);
        
        return RiskAssessment.builder()
            .overallRiskLevel(overallRiskLevel)
            .riskFactors(riskFactors)
            .riskLevels(riskLevels)
            .mitigationStrategies(mitigationStrategies)
            .warnings(warnings)
            .confidence("HIGH")
            .build();
    }
    
    /**
     * Calculate resource requirements
     */
    private ResourceRequirements calculateResourceRequirements(List<TestType> testTypes, 
                                                              List<String> services, List<ActionType> actions) {
        String cpuRequirements = "MEDIUM";
        String memoryRequirements = "MEDIUM";
        String storageRequirements = "LOW";
        String networkRequirements = "MEDIUM";
        List<String> externalDependencies = new ArrayList<>();
        
        // Calculate based on test types
        for (TestType testType : testTypes) {
            TestTypeContext context = testTypeContexts.get(testType);
            if (context != null) {
                if ("HIGH".equals(context.getResourceUsage())) {
                    cpuRequirements = "HIGH";
                    memoryRequirements = "HIGH";
                }
                if (context.getDependencies() != null) {
                    externalDependencies.addAll(context.getDependencies());
                }
            }
        }
        
        // Calculate based on services
        for (String service : services) {
            ServiceContext context = serviceContexts.get(service);
            if (context != null) {
                if (context.getDependencies() != null) {
                    externalDependencies.addAll(context.getDependencies());
                }
            }
        }
        
        return ResourceRequirements.builder()
            .cpuRequirements(cpuRequirements)
            .memoryRequirements(memoryRequirements)
            .storageRequirements(storageRequirements)
            .networkRequirements(networkRequirements)
            .externalDependencies(externalDependencies)
            .priority("NORMAL")
            .build();
    }
    
    /**
     * Estimate execution duration
     */
    private String estimateDuration(List<TestType> testTypes, List<String> services, List<ActionType> actions) {
        int totalMinutes = 0;
        
        // Estimate based on test types
        for (TestType testType : testTypes) {
            TestTypeContext context = testTypeContexts.get(testType);
            if (context != null && context.getExecutionTime() != null) {
                totalMinutes += parseExecutionTime(context.getExecutionTime());
            }
        }
        
        // Multiply by number of services
        totalMinutes *= services.size();
        
        // Multiply by number of actions
        totalMinutes *= actions.size();
        
        return formatDuration(totalMinutes);
    }
    
    /**
     * Initialize context mappings for all test types, services, and actions
     */
    private void initializeContextMappings() {
        initializeTestTypeContexts();
        initializeServiceContexts();
        initializeActionContexts();
    }
    
    /**
     * Initialize test type contexts
     */
    private void initializeTestTypeContexts() {
        // Unit Test Context
        testTypeContexts.put(TestType.UNIT_TEST, TestTypeContext.builder()
            .testType(TestType.UNIT_TEST)
            .description("Individual component testing")
            .tools(Arrays.asList("JUnit 5", "Mockito", "TestNG"))
            .executionTime("1-5 minutes")
            .resourceUsage("LOW")
            .dependencies(Arrays.asList())
            .riskLevel("LOW")
            .parallelizable(true)
            .criticality("LOW")
            .supportedServices(Arrays.asList("user-service", "product-service", "order-service", "notification-service", "gateway-service"))
            .build());
            
        // Integration Test Context
        testTypeContexts.put(TestType.INTEGRATION_TEST, TestTypeContext.builder()
            .testType(TestType.INTEGRATION_TEST)
            .description("Component interaction testing")
            .tools(Arrays.asList("Spring Boot Test", "TestContainers", "WireMock"))
            .executionTime("5-15 minutes")
            .resourceUsage("MEDIUM")
            .dependencies(Arrays.asList("Database", "External Services"))
            .riskLevel("MEDIUM")
            .parallelizable(false)
            .criticality("MEDIUM")
            .supportedServices(Arrays.asList("user-service", "product-service", "order-service", "notification-service", "gateway-service"))
            .build());
            
        // API Test Context
        testTypeContexts.put(TestType.API_TEST, TestTypeContext.builder()
            .testType(TestType.API_TEST)
            .description("REST API endpoint testing")
            .tools(Arrays.asList("RestAssured", "Karate", "Postman"))
            .executionTime("3-10 minutes")
            .resourceUsage("MEDIUM")
            .dependencies(Arrays.asList("API Gateway", "Service Endpoints"))
            .riskLevel("LOW")
            .parallelizable(true)
            .criticality("MEDIUM")
            .supportedServices(Arrays.asList("user-service", "product-service", "order-service", "notification-service", "gateway-service"))
            .build());
            
        // Performance Test Context
        testTypeContexts.put(TestType.PERFORMANCE_TEST, TestTypeContext.builder()
            .testType(TestType.PERFORMANCE_TEST)
            .description("Load and stress testing")
            .tools(Arrays.asList("JMeter", "Gatling", "K6"))
            .executionTime("15-60 minutes")
            .resourceUsage("HIGH")
            .dependencies(Arrays.asList("Load Balancer", "Monitoring"))
            .riskLevel("HIGH")
            .parallelizable(false)
            .criticality("HIGH")
            .supportedServices(Arrays.asList("user-service", "product-service", "order-service", "notification-service", "gateway-service"))
            .build());
            
        // Security Test Context
        testTypeContexts.put(TestType.SECURITY_TEST, TestTypeContext.builder()
            .testType(TestType.SECURITY_TEST)
            .description("Security vulnerability testing")
            .tools(Arrays.asList("OWASP ZAP", "Burp Suite", "Nessus"))
            .executionTime("10-30 minutes")
            .resourceUsage("MEDIUM")
            .dependencies(Arrays.asList("Security Scanner", "Vulnerability Database"))
            .riskLevel("MEDIUM")
            .parallelizable(true)
            .criticality("HIGH")
            .supportedServices(Arrays.asList("user-service", "product-service", "order-service", "notification-service", "gateway-service"))
            .build());
            
        // Chaos Test Context
        testTypeContexts.put(TestType.CHAOS_TEST, TestTypeContext.builder()
            .testType(TestType.CHAOS_TEST)
            .description("Resilience and failure testing")
            .tools(Arrays.asList("Litmus Chaos", "Chaos Monkey", "Gremlin"))
            .executionTime("5-30 minutes")
            .resourceUsage("MEDIUM")
            .dependencies(Arrays.asList("Chaos Engine", "Monitoring"))
            .riskLevel("HIGH")
            .parallelizable(false)
            .criticality("HIGH")
            .supportedServices(Arrays.asList("user-service", "product-service", "order-service", "notification-service", "gateway-service"))
            .build());
            
        // Contract Test Context
        testTypeContexts.put(TestType.CONTRACT_TEST, TestTypeContext.builder()
            .testType(TestType.CONTRACT_TEST)
            .description("Service contract validation")
            .tools(Arrays.asList("Pact", "Spring Cloud Contract", "WireMock"))
            .executionTime("2-8 minutes")
            .resourceUsage("LOW")
            .dependencies(Arrays.asList("Contract Repository", "Service Registry"))
            .riskLevel("LOW")
            .parallelizable(true)
            .criticality("MEDIUM")
            .supportedServices(Arrays.asList("user-service", "product-service", "order-service", "notification-service", "gateway-service"))
            .build());
            
        // End-to-End Test Context
        testTypeContexts.put(TestType.END_TO_END_TEST, TestTypeContext.builder()
            .testType(TestType.END_TO_END_TEST)
            .description("Complete workflow testing")
            .tools(Arrays.asList("Selenium", "Cypress", "Playwright"))
            .executionTime("10-45 minutes")
            .resourceUsage("HIGH")
            .dependencies(Arrays.asList("Browser", "Test Data", "External Services"))
            .riskLevel("MEDIUM")
            .parallelizable(false)
            .criticality("HIGH")
            .supportedServices(Arrays.asList("user-service", "product-service", "order-service", "notification-service", "gateway-service"))
            .build());
            
        // Smoke Test Context
        testTypeContexts.put(TestType.SMOKE_TEST, TestTypeContext.builder()
            .testType(TestType.SMOKE_TEST)
            .description("Basic functionality verification")
            .tools(Arrays.asList("JUnit 5", "RestAssured", "Selenium"))
            .executionTime("1-3 minutes")
            .resourceUsage("LOW")
            .dependencies(Arrays.asList())
            .riskLevel("LOW")
            .parallelizable(true)
            .criticality("LOW")
            .supportedServices(Arrays.asList("user-service", "product-service", "order-service", "notification-service", "gateway-service"))
            .build());
            
        // Regression Test Context
        testTypeContexts.put(TestType.REGRESSION_TEST, TestTypeContext.builder()
            .testType(TestType.REGRESSION_TEST)
            .description("Regression detection testing")
            .tools(Arrays.asList("JUnit 5", "TestNG", "Selenium"))
            .executionTime("5-20 minutes")
            .resourceUsage("MEDIUM")
            .dependencies(Arrays.asList("Test Data", "Baseline Results"))
            .riskLevel("LOW")
            .parallelizable(true)
            .criticality("MEDIUM")
            .supportedServices(Arrays.asList("user-service", "product-service", "order-service", "notification-service", "gateway-service"))
            .build());
            
        // Exploratory Test Context
        testTypeContexts.put(TestType.EXPLORATORY_TEST, TestTypeContext.builder()
            .testType(TestType.EXPLORATORY_TEST)
            .description("Ad-hoc testing")
            .tools(Arrays.asList("Manual Testing", "Session-Based Testing"))
            .executionTime("15-60 minutes")
            .resourceUsage("LOW")
            .dependencies(Arrays.asList("Test Environment", "Test Data"))
            .riskLevel("LOW")
            .parallelizable(false)
            .criticality("LOW")
            .supportedServices(Arrays.asList("user-service", "product-service", "order-service", "notification-service", "gateway-service"))
            .build());
            
        // Accessibility Test Context
        testTypeContexts.put(TestType.ACCESSIBILITY_TEST, TestTypeContext.builder()
            .testType(TestType.ACCESSIBILITY_TEST)
            .description("Accessibility compliance testing")
            .tools(Arrays.asList("axe-core", "WAVE", "Lighthouse"))
            .executionTime("3-10 minutes")
            .resourceUsage("LOW")
            .dependencies(Arrays.asList("Browser", "Accessibility Standards"))
            .riskLevel("LOW")
            .parallelizable(true)
            .criticality("MEDIUM")
            .supportedServices(Arrays.asList("user-service", "product-service", "order-service", "notification-service", "gateway-service"))
            .build());
            
        // Compatibility Test Context
        testTypeContexts.put(TestType.COMPATIBILITY_TEST, TestTypeContext.builder()
            .testType(TestType.COMPATIBILITY_TEST)
            .description("Cross-platform compatibility testing")
            .tools(Arrays.asList("BrowserStack", "Sauce Labs", "CrossBrowserTesting"))
            .executionTime("10-30 minutes")
            .resourceUsage("MEDIUM")
            .dependencies(Arrays.asList("Multiple Browsers", "Multiple Devices"))
            .riskLevel("LOW")
            .parallelizable(true)
            .criticality("MEDIUM")
            .supportedServices(Arrays.asList("user-service", "product-service", "order-service", "notification-service", "gateway-service"))
            .build());
            
        // Localization Test Context
        testTypeContexts.put(TestType.LOCALIZATION_TEST, TestTypeContext.builder()
            .testType(TestType.LOCALIZATION_TEST)
            .description("Internationalization testing")
            .tools(Arrays.asList("i18n Testing Tools", "Translation Management"))
            .executionTime("5-15 minutes")
            .resourceUsage("LOW")
            .dependencies(Arrays.asList("Translation Files", "Locale Data"))
            .riskLevel("LOW")
            .parallelizable(true)
            .criticality("LOW")
            .supportedServices(Arrays.asList("user-service", "product-service", "order-service", "notification-service", "gateway-service"))
            .build());
    }
    
    /**
     * Initialize service contexts
     */
    private void initializeServiceContexts() {
        // Gateway Service Context
        serviceContexts.put("gateway-service", ServiceContext.builder()
            .serviceName("gateway-service")
            .port(8080)
            .description("API Gateway and routing service")
            .dependencies(Arrays.asList("user-service", "product-service", "order-service", "notification-service"))
            .endpoints(Arrays.asList("/api/gateway/health", "/api/gateway/routes"))
            .supportedTestTypes(Arrays.asList(TestType.API_TEST, TestType.INTEGRATION_TEST, TestType.PERFORMANCE_TEST, TestType.SECURITY_TEST))
            .criticality("HIGH")
            .healthCheckEndpoints(Arrays.asList("/actuator/health"))
            .supportedActions(Arrays.asList("RUN_TESTS", "HEALTH_CHECK", "MONITOR_SYSTEM"))
            .deploymentType("DOCKER")
            .build());
            
        // User Service Context
        serviceContexts.put("user-service", ServiceContext.builder()
            .serviceName("user-service")
            .port(8081)
            .description("User management, authentication, and authorization")
            .dependencies(Arrays.asList("users-db"))
            .endpoints(Arrays.asList("/api/users/register", "/api/users/{id}", "/api/auth/login", "/api/users"))
            .supportedTestTypes(Arrays.asList(TestType.UNIT_TEST, TestType.INTEGRATION_TEST, TestType.API_TEST, TestType.SECURITY_TEST))
            .criticality("HIGH")
            .healthCheckEndpoints(Arrays.asList("/actuator/health"))
            .supportedActions(Arrays.asList("RUN_TESTS", "ANALYZE_FAILURES", "GENERATE_TESTS", "HEALTH_CHECK"))
            .deploymentType("DOCKER")
            .build());
            
        // Product Service Context
        serviceContexts.put("product-service", ServiceContext.builder()
            .serviceName("product-service")
            .port(8082)
            .description("Product catalog management")
            .dependencies(Arrays.asList("products-db"))
            .endpoints(Arrays.asList("/api/products", "/api/products/{id}"))
            .supportedTestTypes(Arrays.asList(TestType.UNIT_TEST, TestType.INTEGRATION_TEST, TestType.API_TEST, TestType.PERFORMANCE_TEST))
            .criticality("MEDIUM")
            .healthCheckEndpoints(Arrays.asList("/actuator/health"))
            .supportedActions(Arrays.asList("RUN_TESTS", "ANALYZE_FAILURES", "GENERATE_TESTS", "HEALTH_CHECK"))
            .deploymentType("DOCKER")
            .build());
            
        // Order Service Context
        serviceContexts.put("order-service", ServiceContext.builder()
            .serviceName("order-service")
            .port(8083)
            .description("Order processing and management")
            .dependencies(Arrays.asList("orders-db", "user-service", "product-service", "notification-service"))
            .endpoints(Arrays.asList("/api/orders", "/api/orders/{id}", "/api/orders/user/{userId}"))
            .supportedTestTypes(Arrays.asList(TestType.UNIT_TEST, TestType.INTEGRATION_TEST, TestType.API_TEST, TestType.CHAOS_TEST, TestType.PERFORMANCE_TEST))
            .criticality("HIGH")
            .healthCheckEndpoints(Arrays.asList("/actuator/health"))
            .supportedActions(Arrays.asList("RUN_TESTS", "ANALYZE_FAILURES", "GENERATE_TESTS", "HEALTH_CHECK", "RUN_CHAOS_TESTS"))
            .deploymentType("DOCKER")
            .build());
            
        // Notification Service Context
        serviceContexts.put("notification-service", ServiceContext.builder()
            .serviceName("notification-service")
            .port(8084)
            .description("Notification management and delivery")
            .dependencies(Arrays.asList("notifications-db"))
            .endpoints(Arrays.asList("/api/notifications", "/api/notifications/user/{userId}", "/api/notifications/{id}"))
            .supportedTestTypes(Arrays.asList(TestType.UNIT_TEST, TestType.INTEGRATION_TEST, TestType.API_TEST, TestType.PERFORMANCE_TEST))
            .criticality("MEDIUM")
            .healthCheckEndpoints(Arrays.asList("/actuator/health"))
            .supportedActions(Arrays.asList("RUN_TESTS", "ANALYZE_FAILURES", "GENERATE_TESTS", "HEALTH_CHECK"))
            .deploymentType("DOCKER")
            .build());
    }
    
    /**
     * Initialize action contexts
     */
    private void initializeActionContexts() {
        // RUN_TESTS Action Context
        actionContexts.put(ActionType.RUN_TESTS, ActionContext.builder()
            .actionType(ActionType.RUN_TESTS)
            .description("Execute test suites")
            .prerequisites(Arrays.asList("Test Environment", "Test Data"))
            .supportedServices(Arrays.asList("user-service", "product-service", "order-service", "notification-service", "gateway-service"))
            .supportedTestTypes(Arrays.asList(TestType.UNIT_TEST, TestType.INTEGRATION_TEST, TestType.API_TEST, TestType.PERFORMANCE_TEST, TestType.SECURITY_TEST, TestType.CHAOS_TEST))
            .executionTime("5-60 minutes")
            .resourceUsage("MEDIUM")
            .riskLevel("LOW")
            .parallelizable(true)
            .criticality("MEDIUM")
            .outputTypes(Arrays.asList("Test Results", "Coverage Reports", "Performance Metrics"))
            .build());
            
        // ANALYZE_FAILURES Action Context
        actionContexts.put(ActionType.ANALYZE_FAILURES, ActionContext.builder()
            .actionType(ActionType.ANALYZE_FAILURES)
            .description("Analyze test failures")
            .prerequisites(Arrays.asList("Test Results", "Log Files"))
            .supportedServices(Arrays.asList("user-service", "product-service", "order-service", "notification-service", "gateway-service"))
            .supportedTestTypes(Arrays.asList(TestType.UNIT_TEST, TestType.INTEGRATION_TEST, TestType.API_TEST, TestType.PERFORMANCE_TEST, TestType.SECURITY_TEST, TestType.CHAOS_TEST))
            .executionTime("2-10 minutes")
            .resourceUsage("LOW")
            .riskLevel("LOW")
            .parallelizable(false)
            .criticality("HIGH")
            .outputTypes(Arrays.asList("Failure Analysis", "Root Cause Analysis", "Recommendations"))
            .build());
            
        // GENERATE_TESTS Action Context
        actionContexts.put(ActionType.GENERATE_TESTS, ActionContext.builder()
            .actionType(ActionType.GENERATE_TESTS)
            .description("Generate new test cases")
            .prerequisites(Arrays.asList("Source Code", "Test Requirements"))
            .supportedServices(Arrays.asList("user-service", "product-service", "order-service", "notification-service", "gateway-service"))
            .supportedTestTypes(Arrays.asList(TestType.UNIT_TEST, TestType.INTEGRATION_TEST, TestType.API_TEST))
            .executionTime("5-30 minutes")
            .resourceUsage("MEDIUM")
            .riskLevel("LOW")
            .parallelizable(false)
            .criticality("MEDIUM")
            .outputTypes(Arrays.asList("Generated Test Code", "Test Documentation", "Test Data"))
            .build());
            
        // HEALTH_CHECK Action Context
        actionContexts.put(ActionType.HEALTH_CHECK, ActionContext.builder()
            .actionType(ActionType.HEALTH_CHECK)
            .description("Perform system health checks")
            .prerequisites(Arrays.asList("Service Endpoints", "Monitoring Tools"))
            .supportedServices(Arrays.asList("user-service", "product-service", "order-service", "notification-service", "gateway-service"))
            .supportedTestTypes(Arrays.asList())
            .executionTime("1-5 minutes")
            .resourceUsage("LOW")
            .riskLevel("LOW")
            .parallelizable(true)
            .criticality("HIGH")
            .outputTypes(Arrays.asList("Health Status", "Metrics", "Alerts"))
            .build());
            
        // RUN_CHAOS_TESTS Action Context
        actionContexts.put(ActionType.RUN_CHAOS_TESTS, ActionContext.builder()
            .actionType(ActionType.RUN_CHAOS_TESTS)
            .description("Execute chaos engineering tests")
            .prerequisites(Arrays.asList("Chaos Engine", "Monitoring", "Rollback Plan"))
            .supportedServices(Arrays.asList("user-service", "product-service", "order-service", "notification-service", "gateway-service"))
            .supportedTestTypes(Arrays.asList(TestType.CHAOS_TEST))
            .executionTime("5-30 minutes")
            .resourceUsage("MEDIUM")
            .riskLevel("HIGH")
            .parallelizable(false)
            .criticality("HIGH")
            .outputTypes(Arrays.asList("Chaos Results", "Resilience Metrics", "Recovery Analysis"))
            .build());
    }
    
    // Helper methods
    private List<TestType> inferTestTypesFromContext(ParsedCommand parsed) {
        // Default to unit and integration tests if no specific test types mentioned
        return Arrays.asList(TestType.UNIT_TEST, TestType.INTEGRATION_TEST);
    }
    
    private List<String> inferServicesFromContext(ParsedCommand parsed) {
        // Default to all services if no specific services mentioned
        return Arrays.asList("user-service", "product-service", "order-service", "notification-service", "gateway-service");
    }
    
    private List<ActionType> inferActionsFromContext(ParsedCommand parsed) {
        // Default to RUN_TESTS if no specific actions mentioned
        return Arrays.asList(ActionType.RUN_TESTS);
    }
    
    private ActionType mapIntentToAction(IntentType intent) {
        switch (intent) {
            case RUN_TESTS:
                return ActionType.RUN_TESTS;
            case ANALYZE_FAILURES:
                return ActionType.ANALYZE_FAILURES;
            case GENERATE_TESTS:
                return ActionType.GENERATE_TESTS;
            case OPTIMIZE_TESTS:
                return ActionType.OPTIMIZE_TESTS;
            case HEALTH_CHECK:
                return ActionType.HEALTH_CHECK;
            default:
                return null;
        }
    }
    
    private String generateStepId(ActionType action, String service, TestType testType) {
        return action.name() + "_" + service + "_" + testType.name();
    }
    
    private String generateStepName(ActionType action, String service, TestType testType) {
        return action.name() + " " + testType.getDisplayName() + " for " + service;
    }
    
    private Map<String, Object> generateStepParameters(ActionType action, String service, TestType testType) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("action", action.name());
        parameters.put("service", service);
        parameters.put("testType", testType.name());
        parameters.put("timeout", "300s");
        parameters.put("retries", 2);
        return parameters;
    }
    
    private List<String> getStepDependencies(String service, DependencyGraph dependencies) {
        if (dependencies != null && dependencies.getDependencies() != null) {
            return dependencies.getDependencies().getOrDefault(service, new ArrayList<>());
        }
        return new ArrayList<>();
    }
    
    private String estimateStepDuration(ActionType action, String service, TestType testType) {
        TestTypeContext testContext = testTypeContexts.get(testType);
        if (testContext != null) {
            return testContext.getExecutionTime();
        }
        return "5 minutes";
    }
    
    private String determineExecutionOrder(List<ExecutionStep> steps, DependencyGraph dependencies) {
        // Simple execution order - can be enhanced with dependency analysis
        return "SEQUENTIAL";
    }
    
    private String calculateTotalDuration(List<ExecutionStep> steps) {
        int totalMinutes = 0;
        for (ExecutionStep step : steps) {
            totalMinutes += parseExecutionTime(step.getEstimatedDuration());
        }
        return formatDuration(totalMinutes);
    }
    
    private String determineExecutionStrategy(List<TestType> testTypes, List<String> services, List<ActionType> actions) {
        // Determine if parallel execution is possible
        boolean canParallelize = testTypes.stream()
            .map(testTypeContexts::get)
            .filter(Objects::nonNull)
            .allMatch(TestTypeContext::isParallelizable);
            
        return canParallelize ? "PARALLEL" : "SEQUENTIAL";
    }
    
    private RiskLevel determineOverallRiskLevel(Map<String, RiskLevel> riskLevels) {
        if (riskLevels.containsValue(RiskLevel.HIGH)) {
            return RiskLevel.HIGH;
        } else if (riskLevels.containsValue(RiskLevel.MEDIUM)) {
            return RiskLevel.MEDIUM;
        } else {
            return RiskLevel.LOW;
        }
    }
    
    private List<String> generateMitigationStrategies(List<String> riskFactors, Map<String, RiskLevel> riskLevels) {
        List<String> strategies = new ArrayList<>();
        
        if (riskFactors.contains("High risk test type: Chaos Test")) {
            strategies.add("Enable monitoring and rollback mechanisms");
            strategies.add("Run during low-traffic periods");
            strategies.add("Prepare emergency stop procedures");
        }
        
        if (riskFactors.contains("Critical service: gateway-service")) {
            strategies.add("Ensure gateway service redundancy");
            strategies.add("Monitor gateway health continuously");
            strategies.add("Prepare failover procedures");
        }
        
        return strategies;
    }
    
    private List<String> generateWarnings(List<TestType> testTypes, List<String> services, 
                                         List<ActionType> actions, RiskAssessment riskAssessment) {
        List<String> warnings = new ArrayList<>();
        
        if (riskAssessment != null && riskAssessment.getWarnings() != null) {
            warnings.addAll(riskAssessment.getWarnings());
        }
        
        return warnings;
    }
    
    private List<String> generateSuggestions(List<TestType> testTypes, List<String> services, 
                                            List<ActionType> actions, DependencyGraph dependencies) {
        List<String> suggestions = new ArrayList<>();
        
        // Suggest parallel execution if possible
        boolean canParallelize = testTypes.stream()
            .map(testTypeContexts::get)
            .filter(Objects::nonNull)
            .allMatch(TestTypeContext::isParallelizable);
            
        if (canParallelize) {
            suggestions.add("Consider parallel execution to reduce total time");
        }
        
        // Suggest monitoring for high-risk operations
        if (testTypes.contains(TestType.CHAOS_TEST)) {
            suggestions.add("Enable comprehensive monitoring during chaos testing");
            suggestions.add("Prepare rollback procedures");
        }
        
        return suggestions;
    }
    
    private double calculateOverallConfidence(ParsedCommand parsed, List<TestType> testTypes, 
                                             List<String> services, List<ActionType> actions) {
        double confidence = 0.0;
        
        if (parsed != null) {
            confidence += parsed.getConfidence() * 0.4;
        }
        
        // Add confidence based on context completeness
        if (!testTypes.isEmpty()) confidence += 0.2;
        if (!services.isEmpty()) confidence += 0.2;
        if (!actions.isEmpty()) confidence += 0.2;
        
        return Math.min(confidence, 1.0);
    }
    
    private int parseExecutionTime(String timeStr) {
        if (timeStr == null) return 5;
        
        // Parse time strings like "5-15 minutes", "1-5 minutes", etc.
        String[] parts = timeStr.split("-");
        if (parts.length >= 2) {
            try {
                return Integer.parseInt(parts[1].trim().split(" ")[0]);
            } catch (NumberFormatException e) {
                return 5;
            }
        }
        return 5;
    }
    
    private String formatDuration(int minutes) {
        if (minutes < 60) {
            return minutes + " minutes";
        } else {
            int hours = minutes / 60;
            int remainingMinutes = minutes % 60;
            if (remainingMinutes == 0) {
                return hours + " hour" + (hours > 1 ? "s" : "");
            } else {
                return hours + " hour" + (hours > 1 ? "s" : "") + " " + remainingMinutes + " minutes";
            }
        }
    }
    
    private RiskLevel convertStringToRiskLevel(String riskLevel) {
        if (riskLevel == null) return RiskLevel.LOW;
        switch (riskLevel.toUpperCase()) {
            case "HIGH": return RiskLevel.HIGH;
            case "MEDIUM": return RiskLevel.MEDIUM;
            case "LOW": return RiskLevel.LOW;
            case "CRITICAL": return RiskLevel.CRITICAL;
            default: return RiskLevel.LOW;
        }
    }
    
    private ComprehensiveContext createErrorContext(String command, String errorMessage) {
        return ComprehensiveContext.builder()
            .command(command)
            .confidence(0.0)
            .warnings(Arrays.asList("Error analyzing command: " + errorMessage))
            .build();
    }
}
