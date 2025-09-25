package com.kb.synthetic.service;

import com.kb.synthetic.model.*;
import com.kb.synthetic.model.enums.*;
import com.kb.synthetic.repository.*;
import com.kb.synthetic.config.SyntheticDataConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class SyntheticDataGeneratorService {
    
    private final PullRequestRepository pullRequestRepository;
    private final SystemRunRepository systemRunRepository;
    private final ServiceMetricsRepository serviceMetricsRepository;
    private final EcommerceFlowRepository ecommerceFlowRepository;
    private final FlowStepRepository flowStepRepository;
    private final SyntheticDataConfig config;
    
    private final Random random = new Random();
    
    // Service names from the microservices architecture
    private static final List<String> SERVICE_NAMES = Arrays.asList(
        "gateway-service", "user-service", "product-service", 
        "order-service", "notification-service"
    );
    
    private static final List<String> CHANGE_TYPES = Arrays.asList(
        "API_CHANGE", "DB_CHANGE", "CONFIG_CHANGE", "UI_CHANGE", 
        "SECURITY_UPDATE", "PERFORMANCE_OPTIMIZATION", "BUG_FIX", "FEATURE_ADD"
    );
    
    private static final List<String> ERROR_TYPES = Arrays.asList(
        "TIMEOUT", "VALIDATION_ERROR", "SERVICE_UNAVAILABLE", "AUTHENTICATION_ERROR",
        "AUTHORIZATION_ERROR", "RATE_LIMIT_EXCEEDED", "INVALID_REQUEST", "INTERNAL_ERROR"
    );
    
    /**
     * Generate synthetic data for AI/ML model training
     */
    @Transactional
    public void generateSyntheticData(int prCount, int runsPerPR) {
        log.info("Starting synthetic data generation: {} PRs with {} runs each", prCount, runsPerPR);
        
        List<PullRequest> pullRequests = generatePullRequests(prCount);
        pullRequestRepository.saveAll(pullRequests);
        
        for (PullRequest pr : pullRequests) {
            List<SystemRun> systemRuns = generateSystemRuns(pr, runsPerPR);
            systemRunRepository.saveAll(systemRuns);
            
            for (SystemRun run : systemRuns) {
                generateServiceMetrics(run);
                generateEcommerceFlows(run);
            }
        }
        
        log.info("Completed synthetic data generation");
    }
    
    /**
     * Generate synthetic Pull Requests with realistic characteristics
     */
    private List<PullRequest> generatePullRequests(int count) {
        List<PullRequest> prs = new ArrayList<>();
        
        for (int i = 0; i < count; i++) {
            PullRequest pr = PullRequest.builder()
                .prNumber("PR-" + String.format("%06d", i + 1))
                .title(generatePRTitle())
                .description(generatePRDescription())
                .author(generateAuthor())
                .targetBranch("main")
                .sourceBranch("feature/" + generateBranchName())
                .status(generatePRStatus())
                .createdAt(generateRandomDateTime())
                .mergedAt(generateMergedAt())
                .closedAt(generateClosedAt())
                .linesAdded(random.nextInt(500) + 10)
                .linesDeleted(random.nextInt(200) + 1)
                .filesChanged(random.nextInt(20) + 1)
                .testCoverage(ThreadLocalRandom.current().nextDouble(0.6, 1.0))
                .codeComplexity(ThreadLocalRandom.current().nextDouble(1.0, 10.0))
                .cyclomaticComplexity(random.nextInt(20) + 1)
                .performanceImpactScore(ThreadLocalRandom.current().nextDouble(0.0, 1.0))
                .avgResponseTimeMs((long) (random.nextInt(500) + 100))
                .errorRateIncrease(ThreadLocalRandom.current().nextDouble(0.0, 0.1))
                .memoryUsageIncrease(ThreadLocalRandom.current().nextDouble(0.0, 0.2))
                .cpuUsageIncrease(ThreadLocalRandom.current().nextDouble(0.0, 0.15))
                .serviceImpactScores(generateServiceImpactScores())
                .affectedServices(generateAffectedServices())
                .changeTypes(generateChangeTypes())
                .riskScore(calculateRiskScore())
                .hasPerformanceRegression(random.nextDouble() < 0.2) // 20% chance
                .hasSecurityVulnerability(random.nextDouble() < 0.05) // 5% chance
                .hasBreakingChange(random.nextDouble() < 0.1) // 10% chance
                .build();
            
            prs.add(pr);
        }
        
        return prs;
    }
    
    /**
     * Generate system runs for a Pull Request
     */
    private List<SystemRun> generateSystemRuns(PullRequest pr, int count) {
        List<SystemRun> runs = new ArrayList<>();
        
        for (int i = 0; i < count; i++) {
            SystemRun run = SystemRun.builder()
                .pullRequest(pr)
                .runId("RUN-" + pr.getPrNumber() + "-" + String.format("%03d", i + 1))
                .startTime(generateRandomDateTime())
                .endTime(generateEndTime())
                .status(generateRunStatus())
                .runType(generateRunType())
                .overallSuccessRate(ThreadLocalRandom.current().nextDouble(0.7, 1.0))
                .totalRequests((long) (random.nextInt(10000) + 1000))
                .successfulRequests(calculateSuccessfulRequests())
                .failedRequests(calculateFailedRequests())
                .avgResponseTimeMs(ThreadLocalRandom.current().nextDouble(100, 1000))
                .p95ResponseTimeMs(ThreadLocalRandom.current().nextDouble(200, 2000))
                .p99ResponseTimeMs(ThreadLocalRandom.current().nextDouble(500, 5000))
                .throughputRps(ThreadLocalRandom.current().nextDouble(10, 1000))
                .avgCpuUsage(ThreadLocalRandom.current().nextDouble(20, 80))
                .maxCpuUsage(ThreadLocalRandom.current().nextDouble(60, 95))
                .avgMemoryUsage(ThreadLocalRandom.current().nextDouble(30, 80))
                .maxMemoryUsage(ThreadLocalRandom.current().nextDouble(70, 95))
                .avgDiskUsage(ThreadLocalRandom.current().nextDouble(10, 60))
                .maxDiskUsage(ThreadLocalRandom.current().nextDouble(40, 80))
                .errorAnalysis(generateErrorAnalysis())
                .hasPerformanceRegression(random.nextDouble() < 0.2) // 20% chance
                .performanceRegressionScore(ThreadLocalRandom.current().nextDouble(0.0, 1.0))
                .regressionDetails(generateRegressionDetails())
                .testScenario(generateTestScenario())
                .loadIntensity(ThreadLocalRandom.current().nextDouble(0.1, 1.0))
                .failurePattern(generateFailurePattern())
                .isAnomalous(random.nextDouble() < 0.1) // 10% chance
                .anomalyScore(ThreadLocalRandom.current().nextDouble(0.0, 1.0))
                .build();
            
            runs.add(run);
        }
        
        return runs;
    }
    
    /**
     * Generate service metrics for each service in a system run
     */
    private void generateServiceMetrics(SystemRun run) {
        for (String serviceName : SERVICE_NAMES) {
            ServiceMetrics metrics = ServiceMetrics.builder()
                .systemRun(run)
                .serviceName(serviceName)
                .serviceVersion("v" + (random.nextInt(10) + 1) + "." + random.nextInt(20))
                .timestamp(run.getStartTime())
                .healthStatus(generateHealthStatus())
                .healthScore(ThreadLocalRandom.current().nextDouble(0.5, 1.0))
                .totalRequests((long) (random.nextInt(5000) + 100))
                .successfulRequests(calculateServiceSuccessfulRequests())
                .failedRequests(calculateServiceFailedRequests())
                .successRate(ThreadLocalRandom.current().nextDouble(0.8, 1.0))
                .errorRate(ThreadLocalRandom.current().nextDouble(0.0, 0.2))
                .avgResponseTimeMs(ThreadLocalRandom.current().nextDouble(50, 800))
                .minResponseTimeMs(ThreadLocalRandom.current().nextDouble(10, 100))
                .maxResponseTimeMs(ThreadLocalRandom.current().nextDouble(500, 3000))
                .p50ResponseTimeMs(ThreadLocalRandom.current().nextDouble(50, 400))
                .p95ResponseTimeMs(ThreadLocalRandom.current().nextDouble(200, 1500))
                .p99ResponseTimeMs(ThreadLocalRandom.current().nextDouble(500, 3000))
                .throughputRps(ThreadLocalRandom.current().nextDouble(5, 500))
                .cpuUsagePercent(ThreadLocalRandom.current().nextDouble(10, 90))
                .memoryUsageMB(ThreadLocalRandom.current().nextDouble(100, 2000))
                .memoryUsagePercent(ThreadLocalRandom.current().nextDouble(20, 80))
                .diskUsageMB(ThreadLocalRandom.current().nextDouble(50, 1000))
                .diskUsagePercent(ThreadLocalRandom.current().nextDouble(10, 60))
                .networkInMBps(ThreadLocalRandom.current().nextDouble(1, 100))
                .networkOutMBps(ThreadLocalRandom.current().nextDouble(1, 100))
                .errorBreakdown(generateErrorBreakdown())
                .upstreamDependencies(random.nextInt(5))
                .downstreamDependencies(random.nextInt(8))
                .dependencyHealthScore(ThreadLocalRandom.current().nextDouble(0.6, 1.0))
                .ordersProcessed(serviceName.equals("order-service") ? (long) (random.nextInt(1000) + 100) : null)
                .productsViewed(serviceName.equals("product-service") ? (long) (random.nextInt(5000) + 500) : null)
                .usersActive(serviceName.equals("user-service") ? (long) (random.nextInt(1000) + 100) : null)
                .revenueGenerated(serviceName.equals("order-service") ? ThreadLocalRandom.current().nextDouble(1000, 50000) : null)
                .isAnomalous(random.nextDouble() < 0.15) // 15% chance
                .anomalyScore(ThreadLocalRandom.current().nextDouble(0.0, 1.0))
                .anomalyType(generateAnomalyType())
                .anomalyDetails(generateAnomalyDetails())
                .featureVector(generateFeatureVector())
                .predictedFailureProbability(ThreadLocalRandom.current().nextDouble(0.0, 0.3))
                .riskFactors(generateRiskFactors())
                .build();
            
            serviceMetricsRepository.save(metrics);
        }
    }
    
    /**
     * Generate e-commerce flows for a system run
     */
    private void generateEcommerceFlows(SystemRun run) {
        int flowCount = random.nextInt(50) + 10; // 10-60 flows per run
        
        for (int i = 0; i < flowCount; i++) {
            EcommerceFlow flow = EcommerceFlow.builder()
                .systemRun(run)
                .flowId("FLOW-" + run.getRunId() + "-" + String.format("%03d", i + 1))
                .userId("USER-" + String.format("%06d", random.nextInt(100000) + 1))
                .flowType(generateFlowType())
                .startTime(run.getStartTime().plusMinutes(random.nextInt(60)))
                .endTime(generateFlowEndTime())
                .status(generateFlowStatus())
                .totalDurationMs((long) (random.nextInt(30000) + 1000))
                .avgStepDurationMs(ThreadLocalRandom.current().nextDouble(100, 2000))
                .totalSteps(random.nextInt(10) + 2)
                .successfulSteps(calculateSuccessfulSteps())
                .failedSteps(calculateFailedSteps())
                .successRate(ThreadLocalRandom.current().nextDouble(0.6, 1.0))
                .itemsInCart(random.nextInt(10) + 1)
                .cartValue(ThreadLocalRandom.current().nextDouble(10, 1000))
                .finalOrderValue(ThreadLocalRandom.current().nextDouble(10, 1000))
                .paymentMethod(generatePaymentMethod())
                .paymentSuccessful(random.nextDouble() < 0.95) // 95% success rate
                .shippingAddress(generateAddress())
                .billingAddress(generateAddress())
                .errorAnalysis(generateFlowErrorAnalysis())
                .failurePoint(generateFailurePoint())
                .failureReason(generateFailureReason())
                .userSegment(generateUserSegment())
                .deviceType(generateDeviceType())
                .browserType(generateBrowserType())
                .location(generateLocation())
                .sessionDurationMinutes(ThreadLocalRandom.current().nextDouble(5, 120))
                .converted(random.nextDouble() < 0.3) // 30% conversion rate
                .conversionValue(ThreadLocalRandom.current().nextDouble(0, 500))
                .conversionType(generateConversionType())
                .userBehaviorPattern(generateUserBehaviorPattern())
                .abandonmentProbability(ThreadLocalRandom.current().nextDouble(0.0, 0.8))
                .riskFactors(generateFlowRiskFactors())
                .isAnomalous(random.nextDouble() < 0.1) // 10% chance
                .anomalyScore(ThreadLocalRandom.current().nextDouble(0.0, 1.0))
                .anomalyType(generateFlowAnomalyType())
                .build();
            
            ecommerceFlowRepository.save(flow);
            generateFlowSteps(flow);
        }
    }
    
    /**
     * Generate individual steps for an e-commerce flow
     */
    private void generateFlowSteps(EcommerceFlow flow) {
        List<String> stepNames = Arrays.asList(
            "PRODUCT_SEARCH", "PRODUCT_VIEW", "ADD_TO_CART", "CART_REVIEW",
            "CHECKOUT_START", "SHIPPING_INFO", "PAYMENT_INFO", "ORDER_REVIEW",
            "PAYMENT_PROCESS", "ORDER_CONFIRMATION", "INVENTORY_UPDATE", "NOTIFICATION_SEND"
        );
        
        for (int i = 0; i < flow.getTotalSteps(); i++) {
            String stepName = stepNames.get(Math.min(i, stepNames.size() - 1));
            String serviceName = mapStepToService(stepName);
            
            FlowStep step = FlowStep.builder()
                .ecommerceFlow(flow)
                .stepId("STEP-" + flow.getFlowId() + "-" + String.format("%02d", i + 1))
                .stepName(stepName)
                .stepOrder(i + 1)
                .serviceName(serviceName)
                .endpoint(generateEndpoint(stepName))
                .httpMethod(generateHttpMethod(stepName))
                .startTime(flow.getStartTime().plusSeconds(i * 2))
                .endTime(flow.getStartTime().plusSeconds(i * 2 + random.nextInt(5) + 1))
                .status(generateStepStatus())
                .durationMs((long) (random.nextInt(3000) + 100))
                .responseCode(generateResponseCode())
                .responseSizeBytes((long) (random.nextInt(10000) + 100))
                .responseTimeMs(ThreadLocalRandom.current().nextDouble(50, 2000))
                .requestPayload(generateRequestPayload(stepName))
                .responsePayload(generateResponsePayload(stepName))
                .errorMessage(generateErrorMessage())
                .businessAction(stepName)
                .userContext(generateUserContext())
                .sessionContext(generateSessionContext())
                .errorType(generateErrorType())
                .errorCategory(generateErrorCategory())
                .isRetryable(random.nextBoolean())
                .retryAttempts(random.nextInt(3))
                .featureVector(generateStepFeatureVector())
                .failureProbability(ThreadLocalRandom.current().nextDouble(0.0, 0.2))
                .riskFactors(generateStepRiskFactors())
                .isAnomalous(random.nextDouble() < 0.05) // 5% chance
                .anomalyScore(ThreadLocalRandom.current().nextDouble(0.0, 1.0))
                .build();
            
            flowStepRepository.save(step);
        }
    }
    
    // Helper methods for generating realistic data
    private String generatePRTitle() {
        String[] prefixes = {"Fix", "Add", "Update", "Refactor", "Optimize", "Implement", "Remove", "Enhance"};
        String[] subjects = {"user authentication", "order processing", "product catalog", "payment gateway", 
                           "notification system", "API endpoints", "database queries", "error handling"};
        return prefixes[random.nextInt(prefixes.length)] + " " + subjects[random.nextInt(subjects.length)];
    }
    
    private String generatePRDescription() {
        return "This PR " + generatePRTitle().toLowerCase() + " to improve system performance and reliability.";
    }
    
    private String generateAuthor() {
        String[] authors = {"john.doe", "jane.smith", "bob.wilson", "alice.brown", "charlie.davis", 
                          "diana.miller", "eve.jones", "frank.garcia", "grace.lee", "henry.taylor"};
        return authors[random.nextInt(authors.length)];
    }
    
    private String generateBranchName() {
        String[] types = {"auth", "orders", "products", "payments", "notifications", "ui", "api", "db"};
        return types[random.nextInt(types.length)] + "-" + (random.nextInt(999) + 1);
    }
    
    private PRStatus generatePRStatus() {
        PRStatus[] statuses = PRStatus.values();
        return statuses[random.nextInt(statuses.length)];
    }
    
    private LocalDateTime generateRandomDateTime() {
        return LocalDateTime.now().minusDays(random.nextInt(365)).minusHours(random.nextInt(24));
    }
    
    private LocalDateTime generateMergedAt() {
        return random.nextDouble() < 0.8 ? generateRandomDateTime().plusHours(random.nextInt(72)) : null;
    }
    
    private LocalDateTime generateClosedAt() {
        return random.nextDouble() < 0.9 ? generateRandomDateTime().plusHours(random.nextInt(168)) : null;
    }
    
    private LocalDateTime generateEndTime() {
        return generateRandomDateTime().plusMinutes(random.nextInt(120));
    }
    
    private LocalDateTime generateFlowEndTime() {
        return generateRandomDateTime().plusMinutes(random.nextInt(30));
    }
    
    private Map<String, Double> generateServiceImpactScores() {
        Map<String, Double> scores = new HashMap<>();
        for (String service : SERVICE_NAMES) {
            scores.put(service, ThreadLocalRandom.current().nextDouble(0.0, 1.0));
        }
        return scores;
    }
    
    private String generateAffectedServices() {
        List<String> affected = new ArrayList<>();
        int count = random.nextInt(3) + 1;
        for (int i = 0; i < count; i++) {
            affected.add(SERVICE_NAMES.get(random.nextInt(SERVICE_NAMES.size())));
        }
        return String.join(",", affected);
    }
    
    private String generateChangeTypes() {
        List<String> changes = new ArrayList<>();
        int count = random.nextInt(3) + 1;
        for (int i = 0; i < count; i++) {
            changes.add(CHANGE_TYPES.get(random.nextInt(CHANGE_TYPES.size())));
        }
        return String.join(",", changes);
    }
    
    private Double calculateRiskScore() {
        return ThreadLocalRandom.current().nextDouble(0.0, 1.0);
    }
    
    private RunStatus generateRunStatus() {
        RunStatus[] statuses = RunStatus.values();
        return statuses[random.nextInt(statuses.length)];
    }
    
    private RunType generateRunType() {
        RunType[] types = RunType.values();
        return types[random.nextInt(types.length)];
    }
    
    private Long calculateSuccessfulRequests() {
        return (long) (random.nextInt(8000) + 1000);
    }
    
    private Long calculateFailedRequests() {
        return (long) (random.nextInt(500) + 10);
    }
    
    private Long calculateServiceSuccessfulRequests() {
        return (long) (random.nextInt(4000) + 100);
    }
    
    private Long calculateServiceFailedRequests() {
        return (long) (random.nextInt(200) + 5);
    }
    
    private Map<String, Integer> generateErrorAnalysis() {
        Map<String, Integer> errors = new HashMap<>();
        for (String errorType : ERROR_TYPES) {
            if (random.nextDouble() < 0.3) {
                errors.put(errorType, random.nextInt(50) + 1);
            }
        }
        return errors;
    }
    
    private Map<String, Integer> generateErrorBreakdown() {
        Map<String, Integer> errors = new HashMap<>();
        String[] codes = {"400", "401", "403", "404", "500", "502", "503", "504"};
        for (String code : codes) {
            if (random.nextDouble() < 0.2) {
                errors.put(code, random.nextInt(20) + 1);
            }
        }
        return errors;
    }
    
    private String generateRegressionDetails() {
        return "{\"type\":\"performance\",\"impact\":\"medium\",\"services\":[\"order-service\",\"user-service\"]}";
    }
    
    private String generateTestScenario() {
        return "{\"load\":\"high\",\"duration\":\"30m\",\"users\":1000}";
    }
    
    private String generateFailurePattern() {
        return "{\"pattern\":\"intermittent\",\"frequency\":\"low\",\"recovery\":\"automatic\"}";
    }
    
    private ServiceHealthStatus generateHealthStatus() {
        ServiceHealthStatus[] statuses = ServiceHealthStatus.values();
        return statuses[random.nextInt(statuses.length)];
    }
    
    private String generateAnomalyType() {
        String[] types = {"PERFORMANCE", "ERROR_RATE", "RESOURCE_USAGE", "BEHAVIOR"};
        return types[random.nextInt(types.length)];
    }
    
    private String generateAnomalyDetails() {
        return "{\"severity\":\"medium\",\"duration\":\"5m\",\"affected_services\":2}";
    }
    
    private String generateFeatureVector() {
        return "{\"response_time\":0.5,\"error_rate\":0.1,\"cpu_usage\":0.7,\"memory_usage\":0.6}";
    }
    
    private String generateRiskFactors() {
        return "{\"high_cpu\":true,\"low_memory\":false,\"network_latency\":true}";
    }
    
    private FlowType generateFlowType() {
        FlowType[] types = FlowType.values();
        return types[random.nextInt(types.length)];
    }
    
    private FlowStatus generateFlowStatus() {
        FlowStatus[] statuses = FlowStatus.values();
        return statuses[random.nextInt(statuses.length)];
    }
    
    private Integer calculateSuccessfulSteps() {
        return random.nextInt(8) + 2;
    }
    
    private Integer calculateFailedSteps() {
        return random.nextInt(3);
    }
    
    private String generatePaymentMethod() {
        String[] methods = {"CREDIT_CARD", "DEBIT_CARD", "PAYPAL", "APPLE_PAY", "GOOGLE_PAY"};
        return methods[random.nextInt(methods.length)];
    }
    
    private String generateAddress() {
        return "123 Main St, City, State 12345";
    }
    
    private Map<String, Integer> generateFlowErrorAnalysis() {
        Map<String, Integer> errors = new HashMap<>();
        if (random.nextDouble() < 0.3) {
            errors.put("VALIDATION_ERROR", random.nextInt(5) + 1);
        }
        if (random.nextDouble() < 0.2) {
            errors.put("PAYMENT_ERROR", random.nextInt(3) + 1);
        }
        return errors;
    }
    
    private String generateFailurePoint() {
        String[] points = {"PAYMENT_PROCESS", "INVENTORY_CHECK", "SHIPPING_VALIDATION", "USER_AUTH"};
        return points[random.nextInt(points.length)];
    }
    
    private String generateFailureReason() {
        String[] reasons = {"INSUFFICIENT_INVENTORY", "PAYMENT_DECLINED", "INVALID_ADDRESS", "SERVICE_UNAVAILABLE"};
        return reasons[random.nextInt(reasons.length)];
    }
    
    private String generateUserSegment() {
        String[] segments = {"NEW_USER", "RETURNING_USER", "VIP_USER", "BULK_BUYER"};
        return segments[random.nextInt(segments.length)];
    }
    
    private String generateDeviceType() {
        String[] devices = {"MOBILE", "DESKTOP", "TABLET"};
        return devices[random.nextInt(devices.length)];
    }
    
    private String generateBrowserType() {
        String[] browsers = {"CHROME", "FIREFOX", "SAFARI", "EDGE"};
        return browsers[random.nextInt(browsers.length)];
    }
    
    private String generateLocation() {
        String[] locations = {"US", "EU", "ASIA", "LATAM"};
        return locations[random.nextInt(locations.length)];
    }
    
    private String generateConversionType() {
        String[] types = {"PURCHASE", "SIGNUP", "SUBSCRIPTION", "DOWNLOAD"};
        return types[random.nextInt(types.length)];
    }
    
    private String generateUserBehaviorPattern() {
        return "{\"browsing_time\":\"medium\",\"cart_abandonment\":\"low\",\"return_visits\":\"high\"}";
    }
    
    private String generateFlowRiskFactors() {
        return "{\"high_value_cart\":true,\"new_user\":false,\"mobile_device\":true}";
    }
    
    private String generateFlowAnomalyType() {
        String[] types = {"PERFORMANCE", "BEHAVIOR", "CONVERSION", "ABANDONMENT"};
        return types[random.nextInt(types.length)];
    }
    
    private String mapStepToService(String stepName) {
        switch (stepName) {
            case "PRODUCT_SEARCH":
            case "PRODUCT_VIEW":
                return "product-service";
            case "ADD_TO_CART":
            case "CART_REVIEW":
                return "order-service";
            case "CHECKOUT_START":
            case "SHIPPING_INFO":
            case "PAYMENT_INFO":
            case "ORDER_REVIEW":
            case "PAYMENT_PROCESS":
            case "ORDER_CONFIRMATION":
                return "order-service";
            case "INVENTORY_UPDATE":
                return "product-service";
            case "NOTIFICATION_SEND":
                return "notification-service";
            default:
                return "gateway-service";
        }
    }
    
    private String generateEndpoint(String stepName) {
        switch (stepName) {
            case "PRODUCT_SEARCH":
                return "/api/products/search";
            case "PRODUCT_VIEW":
                return "/api/products/{id}";
            case "ADD_TO_CART":
                return "/api/orders/cart/add";
            case "CART_REVIEW":
                return "/api/orders/cart";
            case "CHECKOUT_START":
                return "/api/orders/checkout";
            case "PAYMENT_PROCESS":
                return "/api/orders/payment";
            case "ORDER_CONFIRMATION":
                return "/api/orders/confirm";
            case "NOTIFICATION_SEND":
                return "/api/notifications/send";
            default:
                return "/api/health";
        }
    }
    
    private String generateHttpMethod(String stepName) {
        switch (stepName) {
            case "PRODUCT_SEARCH":
            case "PRODUCT_VIEW":
            case "CART_REVIEW":
                return "GET";
            case "ADD_TO_CART":
            case "CHECKOUT_START":
            case "PAYMENT_PROCESS":
            case "ORDER_CONFIRMATION":
            case "NOTIFICATION_SEND":
                return "POST";
            default:
                return "GET";
        }
    }
    
    private StepStatus generateStepStatus() {
        StepStatus[] statuses = StepStatus.values();
        return statuses[random.nextInt(statuses.length)];
    }
    
    private Integer generateResponseCode() {
        int[] codes = {200, 201, 400, 401, 403, 404, 500, 502, 503};
        return codes[random.nextInt(codes.length)];
    }
    
    private String generateRequestPayload(String stepName) {
        return "{\"step\":\"" + stepName + "\",\"timestamp\":\"" + LocalDateTime.now() + "\"}";
    }
    
    private String generateResponsePayload(String stepName) {
        return "{\"status\":\"success\",\"step\":\"" + stepName + "\",\"data\":{}}";
    }
    
    private String generateErrorMessage() {
        if (random.nextDouble() < 0.1) {
            return "Service temporarily unavailable";
        }
        return null;
    }
    
    private String generateUserContext() {
        return "{\"user_id\":\"USER-123\",\"session_id\":\"SESSION-456\",\"ip\":\"192.168.1.1\"}";
    }
    
    private String generateSessionContext() {
        return "{\"session_duration\":\"15m\",\"page_views\":5,\"referrer\":\"google.com\"}";
    }
    
    private String generateErrorType() {
        return ERROR_TYPES.get(random.nextInt(ERROR_TYPES.size()));
    }
    
    private String generateErrorCategory() {
        String[] categories = {"CLIENT_ERROR", "SERVER_ERROR", "NETWORK_ERROR", "TIMEOUT_ERROR"};
        return categories[random.nextInt(categories.length)];
    }
    
    private String generateStepFeatureVector() {
        return "{\"response_time\":0.3,\"success_rate\":0.95,\"retry_count\":0}";
    }
    
    private String generateStepRiskFactors() {
        return "{\"high_latency\":false,\"error_prone\":false,\"external_dependency\":true}";
    }
}
