package com.kb.jarvis.core.context;

import com.kb.jarvis.core.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Dependency Analysis Service
 * Analyzes service dependencies and relationships for comprehensive context understanding
 */
@Service
public class DependencyAnalysisService {
    
    private static final Logger log = LoggerFactory.getLogger(DependencyAnalysisService.class);
    
    // Service dependency graph
    private final Map<String, List<String>> serviceDependencies = new HashMap<>();
    
    // Test type dependencies
    private final Map<TestType, List<String>> testTypeDependencies = new HashMap<>();
    
    // Action dependencies
    private final Map<ActionType, List<String>> actionDependencies = new HashMap<>();
    
    public DependencyAnalysisService() {
        initializeDependencies();
    }
    
    /**
     * Analyze dependencies for test types, services, and actions
     */
    public DependencyGraph analyzeDependencies(List<TestType> testTypes, List<String> services, List<ActionType> actions) {
        log.info("Analyzing dependencies for testTypes: {}, services: {}, actions: {}", testTypes, services, actions);
        
        try {
            // 1. Analyze service dependencies
            List<String> affectedServices = analyzeServiceDependencies(services);
            log.debug("Affected services: {}", affectedServices);
            
            // 2. Analyze test type dependencies
            Map<String, List<String>> dependencies = analyzeTestTypeDependencies(testTypes, services);
            log.debug("Test type dependencies: {}", dependencies);
            
            // 3. Calculate blast radius
            int blastRadius = calculateBlastRadius(affectedServices, dependencies);
            log.debug("Blast radius: {}", blastRadius);
            
            // 4. Determine severity level
            String severityLevel = determineSeverityLevel(blastRadius, testTypes, actions);
            log.debug("Severity level: {}", severityLevel);
            
            // 5. Identify critical path
            List<String> criticalPath = identifyCriticalPath(services, dependencies);
            log.debug("Critical path: {}", criticalPath);
            
            // 6. Analyze impact
            Map<String, Object> impactAnalysis = analyzeImpact(testTypes, services, actions, blastRadius);
            log.debug("Impact analysis: {}", impactAnalysis);
            
            // 7. Identify isolation points
            List<String> isolationPoints = identifyIsolationPoints(services, dependencies);
            log.debug("Isolation points: {}", isolationPoints);
            
            // 8. Assess risk factors
            Map<String, Object> riskFactors = assessRiskFactors(testTypes, services, actions, blastRadius);
            log.debug("Risk factors: {}", riskFactors);
            
            DependencyGraph dependencyGraph = DependencyGraph.builder()
                .affectedServices(affectedServices)
                .dependencies(dependencies)
                .blastRadius(blastRadius)
                .severityLevel(severityLevel)
                .criticalPath(criticalPath)
                .impactAnalysis(impactAnalysis)
                .isolationPoints(isolationPoints)
                .riskFactors(riskFactors)
                .build();
            
            log.info("Dependency analysis completed with blast radius: {}", blastRadius);
            return dependencyGraph;
            
        } catch (Exception e) {
            log.error("Error analyzing dependencies: {}", e.getMessage(), e);
            return createErrorDependencyGraph();
        }
    }
    
    /**
     * Analyze service dependencies
     */
    private List<String> analyzeServiceDependencies(List<String> services) {
        Set<String> affectedServices = new HashSet<>(services);
        
        // Add direct dependencies
        for (String service : services) {
            List<String> dependencies = serviceDependencies.get(service);
            if (dependencies != null) {
                affectedServices.addAll(dependencies);
            }
        }
        
        // Add indirect dependencies (dependencies of dependencies)
        Set<String> indirectDependencies = new HashSet<>();
        for (String service : affectedServices) {
            List<String> deps = serviceDependencies.get(service);
            if (deps != null) {
                indirectDependencies.addAll(deps);
            }
        }
        affectedServices.addAll(indirectDependencies);
        
        return new ArrayList<>(affectedServices);
    }
    
    /**
     * Analyze test type dependencies
     */
    private Map<String, List<String>> analyzeTestTypeDependencies(List<TestType> testTypes, List<String> services) {
        Map<String, List<String>> dependencies = new HashMap<>();
        
        for (String service : services) {
            List<String> serviceDeps = new ArrayList<>();
            
            // Add service-specific dependencies
            List<String> directDeps = serviceDependencies.get(service);
            if (directDeps != null) {
                serviceDeps.addAll(directDeps);
            }
            
            // Add test type specific dependencies
            for (TestType testType : testTypes) {
                List<String> testDeps = testTypeDependencies.get(testType);
                if (testDeps != null) {
                    serviceDeps.addAll(testDeps);
                }
            }
            
            dependencies.put(service, new ArrayList<>(new HashSet<>(serviceDeps)));
        }
        
        return dependencies;
    }
    
    /**
     * Calculate blast radius
     */
    private int calculateBlastRadius(List<String> affectedServices, Map<String, List<String>> dependencies) {
        int blastRadius = affectedServices.size();
        
        // Add dependency impact
        for (List<String> deps : dependencies.values()) {
            blastRadius += deps.size();
        }
        
        return blastRadius;
    }
    
    /**
     * Determine severity level
     */
    private String determineSeverityLevel(int blastRadius, List<TestType> testTypes, List<ActionType> actions) {
        // High severity if blast radius is large
        if (blastRadius >= 5) {
            return "HIGH";
        }
        
        // High severity if chaos tests are involved
        if (testTypes.contains(TestType.CHAOS_TEST)) {
            return "HIGH";
        }
        
        // High severity if high-risk actions are involved
        if (actions.contains(ActionType.RUN_CHAOS_TESTS)) {
            return "HIGH";
        }
        
        // Medium severity for medium blast radius
        if (blastRadius >= 3) {
            return "MEDIUM";
        }
        
        return "LOW";
    }
    
    /**
     * Identify critical path
     */
    private List<String> identifyCriticalPath(List<String> services, Map<String, List<String>> dependencies) {
        List<String> criticalPath = new ArrayList<>();
        
        // Start with gateway service if present (it's usually critical)
        if (services.contains("gateway-service")) {
            criticalPath.add("gateway-service");
        }
        
        // Add other critical services
        for (String service : services) {
            if (isCriticalService(service) && !criticalPath.contains(service)) {
                criticalPath.add(service);
            }
        }
        
        // Add dependencies of critical services
        for (String service : criticalPath) {
            List<String> deps = dependencies.get(service);
            if (deps != null) {
                for (String dep : deps) {
                    if (!criticalPath.contains(dep)) {
                        criticalPath.add(dep);
                    }
                }
            }
        }
        
        return criticalPath;
    }
    
    /**
     * Analyze impact
     */
    private Map<String, Object> analyzeImpact(List<TestType> testTypes, List<String> services, 
                                             List<ActionType> actions, int blastRadius) {
        Map<String, Object> impact = new HashMap<>();
        
        // Calculate impact score
        double impactScore = calculateImpactScore(testTypes, services, actions, blastRadius);
        impact.put("impactScore", impactScore);
        
        // Determine impact level
        String impactLevel = determineImpactLevel(impactScore);
        impact.put("impactLevel", impactLevel);
        
        // Calculate affected endpoints
        int affectedEndpoints = calculateAffectedEndpoints(services);
        impact.put("affectedEndpoints", affectedEndpoints);
        
        // Calculate estimated downtime
        String estimatedDowntime = estimateDowntime(testTypes, services, actions);
        impact.put("estimatedDowntime", estimatedDowntime);
        
        // Calculate resource impact
        Map<String, String> resourceImpact = calculateResourceImpact(testTypes, services, actions);
        impact.put("resourceImpact", resourceImpact);
        
        return impact;
    }
    
    /**
     * Identify isolation points
     */
    private List<String> identifyIsolationPoints(List<String> services, Map<String, List<String>> dependencies) {
        List<String> isolationPoints = new ArrayList<>();
        
        // Services that can be isolated
        for (String service : services) {
            if (canBeIsolated(service)) {
                isolationPoints.add(service);
            }
        }
        
        return isolationPoints;
    }
    
    /**
     * Assess risk factors
     */
    private Map<String, Object> assessRiskFactors(List<TestType> testTypes, List<String> services, 
                                                 List<ActionType> actions, int blastRadius) {
        Map<String, Object> riskFactors = new HashMap<>();
        
        // Blast radius risk
        riskFactors.put("blastRadiusRisk", blastRadius >= 5 ? "HIGH" : blastRadius >= 3 ? "MEDIUM" : "LOW");
        
        // Test type risk
        String testTypeRisk = assessTestTypeRisk(testTypes);
        riskFactors.put("testTypeRisk", testTypeRisk);
        
        // Service criticality risk
        String serviceRisk = assessServiceRisk(services);
        riskFactors.put("serviceRisk", serviceRisk);
        
        // Action risk
        String actionRisk = assessActionRisk(actions);
        riskFactors.put("actionRisk", actionRisk);
        
        // Overall risk
        String overallRisk = determineOverallRisk(testTypeRisk, serviceRisk, actionRisk);
        riskFactors.put("overallRisk", overallRisk);
        
        return riskFactors;
    }
    
    // Helper methods
    private boolean isCriticalService(String service) {
        return Arrays.asList("gateway-service", "user-service", "order-service").contains(service);
    }
    
    private boolean canBeIsolated(String service) {
        // Services that can be isolated for testing
        return Arrays.asList("product-service", "notification-service").contains(service);
    }
    
    private double calculateImpactScore(List<TestType> testTypes, List<String> services, 
                                       List<ActionType> actions, int blastRadius) {
        double score = 0.0;
        
        // Base score from blast radius
        score += blastRadius * 0.2;
        
        // Add score for test types
        for (TestType testType : testTypes) {
            switch (testType) {
                case CHAOS_TEST:
                    score += 0.5;
                    break;
                case PERFORMANCE_TEST:
                    score += 0.3;
                    break;
                case SECURITY_TEST:
                    score += 0.3;
                    break;
                case END_TO_END_TEST:
                    score += 0.2;
                    break;
                default:
                    score += 0.1;
                    break;
            }
        }
        
        // Add score for critical services
        for (String service : services) {
            if (isCriticalService(service)) {
                score += 0.2;
            }
        }
        
        return Math.min(score, 1.0);
    }
    
    private String determineImpactLevel(double impactScore) {
        if (impactScore >= 0.7) {
            return "HIGH";
        } else if (impactScore >= 0.4) {
            return "MEDIUM";
        } else {
            return "LOW";
        }
    }
    
    private int calculateAffectedEndpoints(List<String> services) {
        int endpoints = 0;
        for (String service : services) {
            endpoints += getServiceEndpointCount(service);
        }
        return endpoints;
    }
    
    private int getServiceEndpointCount(String service) {
        switch (service) {
            case "gateway-service":
                return 10;
            case "user-service":
                return 8;
            case "product-service":
                return 6;
            case "order-service":
                return 8;
            case "notification-service":
                return 6;
            default:
                return 5;
        }
    }
    
    private String estimateDowntime(List<TestType> testTypes, List<String> services, List<ActionType> actions) {
        // Estimate downtime based on test types and services
        int minutes = 0;
        
        for (TestType testType : testTypes) {
            switch (testType) {
                case CHAOS_TEST:
                    minutes += 5;
                    break;
                case PERFORMANCE_TEST:
                    minutes += 10;
                    break;
                case END_TO_END_TEST:
                    minutes += 15;
                    break;
                default:
                    minutes += 2;
                    break;
            }
        }
        
        return minutes + " minutes";
    }
    
    private Map<String, String> calculateResourceImpact(List<TestType> testTypes, List<String> services, List<ActionType> actions) {
        Map<String, String> resourceImpact = new HashMap<>();
        
        // CPU impact
        String cpuImpact = "LOW";
        if (testTypes.contains(TestType.PERFORMANCE_TEST)) {
            cpuImpact = "HIGH";
        } else if (testTypes.contains(TestType.CHAOS_TEST)) {
            cpuImpact = "MEDIUM";
        }
        resourceImpact.put("cpu", cpuImpact);
        
        // Memory impact
        String memoryImpact = "LOW";
        if (testTypes.contains(TestType.PERFORMANCE_TEST)) {
            memoryImpact = "HIGH";
        } else if (testTypes.contains(TestType.INTEGRATION_TEST)) {
            memoryImpact = "MEDIUM";
        }
        resourceImpact.put("memory", memoryImpact);
        
        // Network impact
        String networkImpact = "LOW";
        if (testTypes.contains(TestType.API_TEST) || testTypes.contains(TestType.INTEGRATION_TEST)) {
            networkImpact = "MEDIUM";
        }
        resourceImpact.put("network", networkImpact);
        
        return resourceImpact;
    }
    
    private String assessTestTypeRisk(List<TestType> testTypes) {
        if (testTypes.contains(TestType.CHAOS_TEST)) {
            return "HIGH";
        } else if (testTypes.contains(TestType.PERFORMANCE_TEST) || testTypes.contains(TestType.SECURITY_TEST)) {
            return "MEDIUM";
        } else {
            return "LOW";
        }
    }
    
    private String assessServiceRisk(List<String> services) {
        boolean hasCriticalService = services.stream().anyMatch(this::isCriticalService);
        if (hasCriticalService) {
            return "HIGH";
        } else {
            return "MEDIUM";
        }
    }
    
    private String assessActionRisk(List<ActionType> actions) {
        if (actions.contains(ActionType.RUN_CHAOS_TESTS)) {
            return "HIGH";
        } else if (actions.contains(ActionType.RUN_PERFORMANCE_TESTS) || actions.contains(ActionType.RUN_SECURITY_TESTS)) {
            return "MEDIUM";
        } else {
            return "LOW";
        }
    }
    
    private String determineOverallRisk(String testTypeRisk, String serviceRisk, String actionRisk) {
        if ("HIGH".equals(testTypeRisk) || "HIGH".equals(serviceRisk) || "HIGH".equals(actionRisk)) {
            return "HIGH";
        } else if ("MEDIUM".equals(testTypeRisk) || "MEDIUM".equals(serviceRisk) || "MEDIUM".equals(actionRisk)) {
            return "MEDIUM";
        } else {
            return "LOW";
        }
    }
    
    private DependencyGraph createErrorDependencyGraph() {
        return DependencyGraph.builder()
            .affectedServices(new ArrayList<>())
            .dependencies(new HashMap<>())
            .blastRadius(0)
            .severityLevel("UNKNOWN")
            .criticalPath(new ArrayList<>())
            .impactAnalysis(new HashMap<>())
            .isolationPoints(new ArrayList<>())
            .riskFactors(new HashMap<>())
            .build();
    }
    
    /**
     * Initialize service and test type dependencies
     */
    private void initializeDependencies() {
        // Service dependencies
        serviceDependencies.put("gateway-service", Arrays.asList("user-service", "product-service", "order-service", "notification-service"));
        serviceDependencies.put("user-service", Arrays.asList("users-db"));
        serviceDependencies.put("product-service", Arrays.asList("products-db"));
        serviceDependencies.put("order-service", Arrays.asList("orders-db", "user-service", "product-service", "notification-service"));
        serviceDependencies.put("notification-service", Arrays.asList("notifications-db"));
        
        // Test type dependencies
        testTypeDependencies.put(TestType.INTEGRATION_TEST, Arrays.asList("Database", "External Services"));
        testTypeDependencies.put(TestType.API_TEST, Arrays.asList("API Gateway", "Service Endpoints"));
        testTypeDependencies.put(TestType.PERFORMANCE_TEST, Arrays.asList("Load Balancer", "Monitoring"));
        testTypeDependencies.put(TestType.CHAOS_TEST, Arrays.asList("Chaos Engine", "Monitoring"));
        testTypeDependencies.put(TestType.CONTRACT_TEST, Arrays.asList("Contract Repository", "Service Registry"));
        testTypeDependencies.put(TestType.END_TO_END_TEST, Arrays.asList("Browser", "Test Data", "External Services"));
        testTypeDependencies.put(TestType.SECURITY_TEST, Arrays.asList("Security Scanner", "Vulnerability Database"));
        
        // Action dependencies
        actionDependencies.put(ActionType.RUN_TESTS, Arrays.asList("Test Environment", "Test Data"));
        actionDependencies.put(ActionType.ANALYZE_FAILURES, Arrays.asList("Test Results", "Log Files"));
        actionDependencies.put(ActionType.GENERATE_TESTS, Arrays.asList("Source Code", "Test Requirements"));
        actionDependencies.put(ActionType.HEALTH_CHECK, Arrays.asList("Service Endpoints", "Monitoring Tools"));
        actionDependencies.put(ActionType.RUN_CHAOS_TESTS, Arrays.asList("Chaos Engine", "Monitoring", "Rollback Plan"));
    }
}
