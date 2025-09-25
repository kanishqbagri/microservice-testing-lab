package com.kb.synthetic;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Simple Synthetic Data Generator for AI/ML Model Training
 * 
 * This is a simplified version that generates synthetic data without complex dependencies.
 * It creates realistic datasets for training AI/ML models in microservice environments.
 */
public class SimpleSyntheticDataGenerator {
    
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
    public void generateSyntheticData(int prCount, int runsPerPR) {
        System.out.println("Starting synthetic data generation: " + prCount + " PRs with " + runsPerPR + " runs each");
        
        List<PullRequestData> pullRequests = generatePullRequests(prCount);
        
        for (PullRequestData pr : pullRequests) {
            List<SystemRunData> systemRuns = generateSystemRuns(pr, runsPerPR);
            
            for (SystemRunData run : systemRuns) {
                generateServiceMetrics(run);
                generateEcommerceFlows(run);
            }
        }
        
        System.out.println("Completed synthetic data generation");
        System.out.println("Generated " + pullRequests.size() + " pull requests");
        System.out.println("Generated " + (pullRequests.size() * runsPerPR) + " system runs");
    }
    
    /**
     * Generate synthetic Pull Requests with realistic characteristics
     */
    public List<PullRequestData> generatePullRequests(int count) {
        List<PullRequestData> prs = new ArrayList<>();
        
        for (int i = 0; i < count; i++) {
            PullRequestData pr = new PullRequestData();
            pr.prNumber = "PR-" + String.format("%06d", i + 1);
            pr.title = generatePRTitle();
            pr.description = generatePRDescription();
            pr.author = generateAuthor();
            pr.targetBranch = "main";
            pr.sourceBranch = "feature/" + generateBranchName();
            pr.status = generatePRStatus();
            pr.createdAt = generateRandomDateTime();
            pr.mergedAt = generateMergedAt();
            pr.closedAt = generateClosedAt();
            pr.linesAdded = random.nextInt(500) + 10;
            pr.linesDeleted = random.nextInt(200) + 1;
            pr.filesChanged = random.nextInt(20) + 1;
            pr.testCoverage = ThreadLocalRandom.current().nextDouble(0.6, 1.0);
            pr.codeComplexity = ThreadLocalRandom.current().nextDouble(1.0, 10.0);
            pr.cyclomaticComplexity = random.nextInt(20) + 1;
            pr.performanceImpactScore = ThreadLocalRandom.current().nextDouble(0.0, 1.0);
            pr.avgResponseTimeMs = (long) (random.nextInt(500) + 100);
            pr.errorRateIncrease = ThreadLocalRandom.current().nextDouble(0.0, 0.1);
            pr.memoryUsageIncrease = ThreadLocalRandom.current().nextDouble(0.0, 0.2);
            pr.cpuUsageIncrease = ThreadLocalRandom.current().nextDouble(0.0, 0.15);
            pr.serviceImpactScores = generateServiceImpactScores();
            pr.affectedServices = generateAffectedServices();
            pr.changeTypes = generateChangeTypes();
            pr.riskScore = calculateRiskScore();
            pr.hasPerformanceRegression = random.nextDouble() < 0.2; // 20% chance
            pr.hasSecurityVulnerability = random.nextDouble() < 0.05; // 5% chance
            pr.hasBreakingChange = random.nextDouble() < 0.1; // 10% chance
            
            prs.add(pr);
        }
        
        return prs;
    }
    
    /**
     * Generate system runs for a Pull Request
     */
    public List<SystemRunData> generateSystemRuns(PullRequestData pr, int count) {
        List<SystemRunData> runs = new ArrayList<>();
        
        for (int i = 0; i < count; i++) {
            SystemRunData run = new SystemRunData();
            run.pullRequest = pr;
            run.runId = "RUN-" + pr.prNumber + "-" + String.format("%03d", i + 1);
            run.startTime = generateRandomDateTime();
            run.endTime = generateEndTime();
            run.status = generateRunStatus();
            run.runType = generateRunType();
            run.overallSuccessRate = ThreadLocalRandom.current().nextDouble(0.7, 1.0);
            run.totalRequests = (long) (random.nextInt(10000) + 1000);
            run.successfulRequests = calculateSuccessfulRequests();
            run.failedRequests = calculateFailedRequests();
            run.avgResponseTimeMs = ThreadLocalRandom.current().nextDouble(100, 1000);
            run.p95ResponseTimeMs = ThreadLocalRandom.current().nextDouble(200, 2000);
            run.p99ResponseTimeMs = ThreadLocalRandom.current().nextDouble(500, 5000);
            run.throughputRps = ThreadLocalRandom.current().nextDouble(10, 1000);
            run.avgCpuUsage = ThreadLocalRandom.current().nextDouble(20, 80);
            run.maxCpuUsage = ThreadLocalRandom.current().nextDouble(60, 95);
            run.avgMemoryUsage = ThreadLocalRandom.current().nextDouble(30, 80);
            run.maxMemoryUsage = ThreadLocalRandom.current().nextDouble(70, 95);
            run.avgDiskUsage = ThreadLocalRandom.current().nextDouble(10, 60);
            run.maxDiskUsage = ThreadLocalRandom.current().nextDouble(40, 80);
            run.errorAnalysis = generateErrorAnalysis();
            run.hasPerformanceRegression = random.nextDouble() < 0.2; // 20% chance
            run.performanceRegressionScore = ThreadLocalRandom.current().nextDouble(0.0, 1.0);
            run.regressionDetails = generateRegressionDetails();
            run.testScenario = generateTestScenario();
            run.loadIntensity = ThreadLocalRandom.current().nextDouble(0.1, 1.0);
            run.failurePattern = generateFailurePattern();
            run.isAnomalous = random.nextDouble() < 0.1; // 10% chance
            run.anomalyScore = ThreadLocalRandom.current().nextDouble(0.0, 1.0);
            
            runs.add(run);
        }
        
        return runs;
    }
    
    /**
     * Generate service metrics for each service in a system run
     */
    public void generateServiceMetrics(SystemRunData run) {
        for (String serviceName : SERVICE_NAMES) {
            ServiceMetricsData metrics = new ServiceMetricsData();
            metrics.systemRun = run;
            metrics.serviceName = serviceName;
            metrics.serviceVersion = "v" + (random.nextInt(10) + 1) + "." + random.nextInt(20);
            metrics.timestamp = run.startTime;
            metrics.healthStatus = generateHealthStatus();
            metrics.healthScore = ThreadLocalRandom.current().nextDouble(0.5, 1.0);
            metrics.totalRequests = (long) (random.nextInt(5000) + 100);
            metrics.successfulRequests = calculateServiceSuccessfulRequests();
            metrics.failedRequests = calculateServiceFailedRequests();
            metrics.successRate = ThreadLocalRandom.current().nextDouble(0.8, 1.0);
            metrics.errorRate = ThreadLocalRandom.current().nextDouble(0.0, 0.2);
            metrics.avgResponseTimeMs = ThreadLocalRandom.current().nextDouble(50, 800);
            metrics.minResponseTimeMs = ThreadLocalRandom.current().nextDouble(10, 100);
            metrics.maxResponseTimeMs = ThreadLocalRandom.current().nextDouble(500, 3000);
            metrics.p50ResponseTimeMs = ThreadLocalRandom.current().nextDouble(50, 400);
            metrics.p95ResponseTimeMs = ThreadLocalRandom.current().nextDouble(200, 1500);
            metrics.p99ResponseTimeMs = ThreadLocalRandom.current().nextDouble(500, 3000);
            metrics.throughputRps = ThreadLocalRandom.current().nextDouble(5, 500);
            metrics.cpuUsagePercent = ThreadLocalRandom.current().nextDouble(10, 90);
            metrics.memoryUsageMB = ThreadLocalRandom.current().nextDouble(100, 2000);
            metrics.memoryUsagePercent = ThreadLocalRandom.current().nextDouble(20, 80);
            metrics.diskUsageMB = ThreadLocalRandom.current().nextDouble(50, 1000);
            metrics.diskUsagePercent = ThreadLocalRandom.current().nextDouble(10, 60);
            metrics.networkInMBps = ThreadLocalRandom.current().nextDouble(1, 100);
            metrics.networkOutMBps = ThreadLocalRandom.current().nextDouble(1, 100);
            metrics.errorBreakdown = generateErrorBreakdown();
            metrics.upstreamDependencies = random.nextInt(5);
            metrics.downstreamDependencies = random.nextInt(8);
            metrics.dependencyHealthScore = ThreadLocalRandom.current().nextDouble(0.6, 1.0);
            metrics.ordersProcessed = serviceName.equals("order-service") ? (long) (random.nextInt(1000) + 100) : null;
            metrics.productsViewed = serviceName.equals("product-service") ? (long) (random.nextInt(5000) + 500) : null;
            metrics.usersActive = serviceName.equals("user-service") ? (long) (random.nextInt(1000) + 100) : null;
            metrics.revenueGenerated = serviceName.equals("order-service") ? ThreadLocalRandom.current().nextDouble(1000, 50000) : null;
            metrics.isAnomalous = random.nextDouble() < 0.15; // 15% chance
            metrics.anomalyScore = ThreadLocalRandom.current().nextDouble(0.0, 1.0);
            metrics.anomalyType = generateAnomalyType();
            metrics.anomalyDetails = generateAnomalyDetails();
            metrics.featureVector = generateFeatureVector();
            metrics.predictedFailureProbability = ThreadLocalRandom.current().nextDouble(0.0, 0.3);
            metrics.riskFactors = generateRiskFactors();
            
            // Store metrics (in a real implementation, this would be saved to database)
            run.serviceMetrics.add(metrics);
        }
    }
    
    /**
     * Generate e-commerce flows for a system run
     */
    public void generateEcommerceFlows(SystemRunData run) {
        int flowCount = random.nextInt(50) + 10; // 10-60 flows per run
        
        for (int i = 0; i < flowCount; i++) {
            EcommerceFlowData flow = new EcommerceFlowData();
            flow.systemRun = run;
            flow.flowId = "FLOW-" + run.runId + "-" + String.format("%03d", i + 1);
            flow.userId = "USER-" + String.format("%06d", random.nextInt(100000) + 1);
            flow.flowType = generateFlowType();
            flow.startTime = run.startTime.plusMinutes(random.nextInt(60));
            flow.endTime = generateFlowEndTime();
            flow.status = generateFlowStatus();
            flow.totalDurationMs = (long) (random.nextInt(30000) + 1000);
            flow.avgStepDurationMs = ThreadLocalRandom.current().nextDouble(100, 2000);
            flow.totalSteps = random.nextInt(10) + 2;
            flow.successfulSteps = calculateSuccessfulSteps();
            flow.failedSteps = calculateFailedSteps();
            flow.successRate = ThreadLocalRandom.current().nextDouble(0.6, 1.0);
            flow.itemsInCart = random.nextInt(10) + 1;
            flow.cartValue = ThreadLocalRandom.current().nextDouble(10, 1000);
            flow.finalOrderValue = ThreadLocalRandom.current().nextDouble(10, 1000);
            flow.paymentMethod = generatePaymentMethod();
            flow.paymentSuccessful = random.nextDouble() < 0.95; // 95% success rate
            flow.shippingAddress = generateAddress();
            flow.billingAddress = generateAddress();
            flow.errorAnalysis = generateFlowErrorAnalysis();
            flow.failurePoint = generateFailurePoint();
            flow.failureReason = generateFailureReason();
            flow.userSegment = generateUserSegment();
            flow.deviceType = generateDeviceType();
            flow.browserType = generateBrowserType();
            flow.location = generateLocation();
            flow.sessionDurationMinutes = ThreadLocalRandom.current().nextDouble(5, 120);
            flow.converted = random.nextDouble() < 0.3; // 30% conversion rate
            flow.conversionValue = ThreadLocalRandom.current().nextDouble(0, 500);
            flow.conversionType = generateConversionType();
            flow.userBehaviorPattern = generateUserBehaviorPattern();
            flow.abandonmentProbability = ThreadLocalRandom.current().nextDouble(0.0, 0.8);
            flow.riskFactors = generateFlowRiskFactors();
            flow.isAnomalous = random.nextDouble() < 0.1; // 10% chance
            flow.anomalyScore = ThreadLocalRandom.current().nextDouble(0.0, 1.0);
            flow.anomalyType = generateFlowAnomalyType();
            
            // Store flow (in a real implementation, this would be saved to database)
            run.ecommerceFlows.add(flow);
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
    
    private String generatePRStatus() {
        String[] statuses = {"OPEN", "MERGED", "CLOSED", "DRAFT", "MERGE_CONFLICT", "NEEDS_REVIEW", "APPROVED", "REJECTED"};
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
    
    private String generateRunStatus() {
        String[] statuses = {"RUNNING", "COMPLETED", "FAILED", "CANCELLED", "TIMEOUT", "PARTIAL_SUCCESS"};
        return statuses[random.nextInt(statuses.length)];
    }
    
    private String generateRunType() {
        String[] types = {"UNIT_TEST", "INTEGRATION_TEST", "PERFORMANCE_TEST", "LOAD_TEST", "STRESS_TEST", "CHAOS_TEST", "SECURITY_TEST", "END_TO_END_TEST", "SMOKE_TEST", "REGRESSION_TEST", "CANARY_DEPLOYMENT", "BLUE_GREEN_DEPLOYMENT", "A_B_TEST"};
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
    
    private String generateHealthStatus() {
        String[] statuses = {"HEALTHY", "DEGRADED", "UNHEALTHY", "CRITICAL", "UNKNOWN", "STARTING", "STOPPING", "MAINTENANCE"};
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
    
    private String generateFlowType() {
        String[] types = {"PRODUCT_BROWSE", "ADD_TO_CART", "CHECKOUT", "PAYMENT", "ORDER_CONFIRMATION", "USER_REGISTRATION", "USER_LOGIN", "PRODUCT_SEARCH", "WISHLIST_MANAGEMENT", "ORDER_TRACKING", "RETURN_PROCESS", "REVIEW_SUBMISSION", "CUSTOMER_SUPPORT", "INVENTORY_CHECK", "PRICE_COMPARISON"};
        return types[random.nextInt(types.length)];
    }
    
    private String generateFlowStatus() {
        String[] statuses = {"STARTED", "IN_PROGRESS", "COMPLETED", "FAILED", "ABANDONED", "TIMEOUT", "CANCELLED", "PARTIAL_SUCCESS"};
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
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        SimpleSyntheticDataGenerator generator = new SimpleSyntheticDataGenerator();
        
        int prCount = args.length > 0 ? Integer.parseInt(args[0]) : 100;
        int runsPerPR = args.length > 1 ? Integer.parseInt(args[1]) : 5;
        
        System.out.println("🎯 Simple Synthetic Data Generator for AI/ML Model Training");
        System.out.println("==========================================================");
        System.out.println("Generating " + prCount + " PRs with " + runsPerPR + " runs each");
        System.out.println();
        
        long startTime = System.currentTimeMillis();
        generator.generateSyntheticData(prCount, runsPerPR);
        long endTime = System.currentTimeMillis();
        
        System.out.println();
        System.out.println("✅ Data generation completed in " + (endTime - startTime) + " ms");
        System.out.println("📊 Generated data includes:");
        System.out.println("   - Pull Requests with code metrics and performance impact");
        System.out.println("   - System Runs with performance and resource metrics");
        System.out.println("   - Service Metrics with health and business metrics");
        System.out.println("   - E-commerce Flows with user journey and conversion data");
        System.out.println("   - 20% performance regression injection");
        System.out.println("   - 10% anomaly generation");
        System.out.println("   - Realistic error patterns and failure scenarios");
    }
}

// Data classes
class PullRequestData {
    public String prNumber;
    public String title;
    public String description;
    public String author;
    public String targetBranch;
    public String sourceBranch;
    public String status;
    public LocalDateTime createdAt;
    public LocalDateTime mergedAt;
    public LocalDateTime closedAt;
    public Integer linesAdded;
    public Integer linesDeleted;
    public Integer filesChanged;
    public Double testCoverage;
    public Double codeComplexity;
    public Integer cyclomaticComplexity;
    public Double performanceImpactScore;
    public Long avgResponseTimeMs;
    public Double errorRateIncrease;
    public Double memoryUsageIncrease;
    public Double cpuUsageIncrease;
    public Map<String, Double> serviceImpactScores;
    public String affectedServices;
    public String changeTypes;
    public Double riskScore;
    public Boolean hasPerformanceRegression;
    public Boolean hasSecurityVulnerability;
    public Boolean hasBreakingChange;
}

class SystemRunData {
    public PullRequestData pullRequest;
    public String runId;
    public LocalDateTime startTime;
    public LocalDateTime endTime;
    public String status;
    public String runType;
    public Double overallSuccessRate;
    public Long totalRequests;
    public Long successfulRequests;
    public Long failedRequests;
    public Double avgResponseTimeMs;
    public Double p95ResponseTimeMs;
    public Double p99ResponseTimeMs;
    public Double throughputRps;
    public Double avgCpuUsage;
    public Double maxCpuUsage;
    public Double avgMemoryUsage;
    public Double maxMemoryUsage;
    public Double avgDiskUsage;
    public Double maxDiskUsage;
    public Map<String, Integer> errorAnalysis;
    public Boolean hasPerformanceRegression;
    public Double performanceRegressionScore;
    public String regressionDetails;
    public String testScenario;
    public Double loadIntensity;
    public String failurePattern;
    public Boolean isAnomalous;
    public Double anomalyScore;
    public List<ServiceMetricsData> serviceMetrics = new ArrayList<>();
    public List<EcommerceFlowData> ecommerceFlows = new ArrayList<>();
}

class ServiceMetricsData {
    public SystemRunData systemRun;
    public String serviceName;
    public String serviceVersion;
    public LocalDateTime timestamp;
    public String healthStatus;
    public Double healthScore;
    public Long totalRequests;
    public Long successfulRequests;
    public Long failedRequests;
    public Double successRate;
    public Double errorRate;
    public Double avgResponseTimeMs;
    public Double minResponseTimeMs;
    public Double maxResponseTimeMs;
    public Double p50ResponseTimeMs;
    public Double p95ResponseTimeMs;
    public Double p99ResponseTimeMs;
    public Double throughputRps;
    public Double cpuUsagePercent;
    public Double memoryUsageMB;
    public Double memoryUsagePercent;
    public Double diskUsageMB;
    public Double diskUsagePercent;
    public Double networkInMBps;
    public Double networkOutMBps;
    public Map<String, Integer> errorBreakdown;
    public Integer upstreamDependencies;
    public Integer downstreamDependencies;
    public Double dependencyHealthScore;
    public Long ordersProcessed;
    public Long productsViewed;
    public Long usersActive;
    public Double revenueGenerated;
    public Boolean isAnomalous;
    public Double anomalyScore;
    public String anomalyType;
    public String anomalyDetails;
    public String featureVector;
    public Double predictedFailureProbability;
    public String riskFactors;
}

class EcommerceFlowData {
    public SystemRunData systemRun;
    public String flowId;
    public String userId;
    public String flowType;
    public LocalDateTime startTime;
    public LocalDateTime endTime;
    public String status;
    public Long totalDurationMs;
    public Double avgStepDurationMs;
    public Integer totalSteps;
    public Integer successfulSteps;
    public Integer failedSteps;
    public Double successRate;
    public Integer itemsInCart;
    public Double cartValue;
    public Double finalOrderValue;
    public String paymentMethod;
    public Boolean paymentSuccessful;
    public String shippingAddress;
    public String billingAddress;
    public Map<String, Integer> errorAnalysis;
    public String failurePoint;
    public String failureReason;
    public String userSegment;
    public String deviceType;
    public String browserType;
    public String location;
    public Double sessionDurationMinutes;
    public Boolean converted;
    public Double conversionValue;
    public String conversionType;
    public String userBehaviorPattern;
    public Double abandonmentProbability;
    public String riskFactors;
    public Boolean isAnomalous;
    public Double anomalyScore;
    public String anomalyType;
}
