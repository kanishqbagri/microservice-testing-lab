package com.kb.jarvis.core.context;

import com.kb.jarvis.core.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Context Mapping Service
 * Provides mapping and lookup services for test types, services, and actions
 */
@Service
public class ContextMappingService {
    
    private static final Logger log = LoggerFactory.getLogger(ContextMappingService.class);
    
    /**
     * Get test type context by test type
     */
    public TestTypeContext getTestTypeContext(TestType testType) {
        // This would typically be loaded from a database or configuration
        // For now, return a basic context
        return TestTypeContext.builder()
            .testType(testType)
            .description(testType.getDescription())
            .executionTime("5-15 minutes")
            .resourceUsage("MEDIUM")
            .riskLevel("MEDIUM")
            .parallelizable(true)
            .build();
    }
    
    /**
     * Get service context by service name
     */
    public ServiceContext getServiceContext(String serviceName) {
        // This would typically be loaded from a database or configuration
        // For now, return a basic context
        return ServiceContext.builder()
            .serviceName(serviceName)
            .port(getServicePort(serviceName))
            .description("Microservice: " + serviceName)
            .criticality("MEDIUM")
            .build();
    }
    
    /**
     * Get action context by action type
     */
    public ActionContext getActionContext(ActionType actionType) {
        // This would typically be loaded from a database or configuration
        // For now, return a basic context
        return ActionContext.builder()
            .actionType(actionType)
            .description("Action: " + actionType.name())
            .executionTime("5-30 minutes")
            .resourceUsage("MEDIUM")
            .riskLevel("MEDIUM")
            .parallelizable(false)
            .build();
    }
    
    /**
     * Get all supported test types for a service
     */
    public List<TestType> getSupportedTestTypes(String serviceName) {
        // This would typically be loaded from configuration
        return Arrays.asList(
            TestType.UNIT_TEST,
            TestType.INTEGRATION_TEST,
            TestType.API_TEST,
            TestType.PERFORMANCE_TEST
        );
    }
    
    /**
     * Get all supported actions for a service
     */
    public List<ActionType> getSupportedActions(String serviceName) {
        // This would typically be loaded from configuration
        return Arrays.asList(
            ActionType.RUN_TESTS,
            ActionType.ANALYZE_FAILURES,
            ActionType.GENERATE_TESTS,
            ActionType.HEALTH_CHECK
        );
    }
    
    /**
     * Check if a test type is supported for a service
     */
    public boolean isTestTypeSupported(String serviceName, TestType testType) {
        List<TestType> supportedTypes = getSupportedTestTypes(serviceName);
        return supportedTypes.contains(testType);
    }
    
    /**
     * Check if an action is supported for a service
     */
    public boolean isActionSupported(String serviceName, ActionType actionType) {
        List<ActionType> supportedActions = getSupportedActions(serviceName);
        return supportedActions.contains(actionType);
    }
    
    /**
     * Get service port by service name
     */
    private int getServicePort(String serviceName) {
        switch (serviceName) {
            case "gateway-service":
                return 8080;
            case "user-service":
                return 8081;
            case "product-service":
                return 8082;
            case "order-service":
                return 8083;
            case "notification-service":
                return 8084;
            default:
                return 8080;
        }
    }
}
