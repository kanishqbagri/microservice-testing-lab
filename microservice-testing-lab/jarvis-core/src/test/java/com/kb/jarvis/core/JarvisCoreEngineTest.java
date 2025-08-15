package com.kb.jarvis.core;

import com.kb.jarvis.core.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

@SpringBootTest
@ActiveProfiles("test")
public class JarvisCoreEngineTest {

    @Autowired
    private JarvisCoreEngine jarvisCoreEngine;

    @Test
    public void testProcessCommand_RunTests() {
        // Given
        String userInput = "Run integration tests for user-service and product-service";

        // When
        JarvisResponse response = jarvisCoreEngine.processCommand(userInput);

        // Then
        assertNotNull(response);
        assertNotNull(response.getMessage());
        assertNotEquals(JarvisResponseStatus.ERROR, response.getStatus());
        
        // Verify the response contains expected information
        assertTrue(response.getMessage().contains("integration tests"));
        assertTrue(response.getMessage().contains("user-service") || response.getMessage().contains("product-service"));
        
        // Verify action was created
        assertNotNull(response.getAction());
        assertNotNull(response.getAction().getType());
        assertNotNull(response.getAction().getDescription());
    }

    @Test
    public void testProcessCommand_AnalyzeFailures() {
        // Given
        String userInput = "Analyze recent test failures and provide root cause analysis";

        // When
        JarvisResponse response = jarvisCoreEngine.processCommand(userInput);

        // Then
        assertNotNull(response);
        assertNotNull(response.getMessage());
        assertNotEquals(JarvisResponseStatus.ERROR, response.getStatus());
        
        // Verify the response contains expected information
        assertTrue(response.getMessage().contains("failures") || response.getMessage().contains("analysis"));
        
        // Verify action was created
        assertNotNull(response.getAction());
        assertEquals(ActionType.ANALYZE_FAILURES, response.getAction().getType());
    }

    @Test
    public void testProcessCommand_HealthCheck() {
        // Given
        String userInput = "Check system health and performance";

        // When
        JarvisResponse response = jarvisCoreEngine.processCommand(userInput);

        // Then
        assertNotNull(response);
        assertNotNull(response.getMessage());
        assertNotEquals(JarvisResponseStatus.ERROR, response.getStatus());
        
        // Verify action was created
        assertNotNull(response.getAction());
        assertEquals(ActionType.HEALTH_CHECK, response.getAction().getType());
    }

    @Test
    public void testProcessCommand_PerformanceTests() {
        // Given
        String userInput = "Run performance tests on order-service with high load";

        // When
        JarvisResponse response = jarvisCoreEngine.processCommand(userInput);

        // Then
        assertNotNull(response);
        assertNotNull(response.getMessage());
        assertNotEquals(JarvisResponseStatus.ERROR, response.getStatus());
        
        // Verify the response contains expected information
        assertTrue(response.getMessage().contains("performance") || response.getMessage().contains("order-service"));
        
        // Verify action was created
        assertNotNull(response.getAction());
        assertTrue(response.getAction().getType() == ActionType.RUN_PERFORMANCE_TESTS || 
                  response.getAction().getType() == ActionType.RUN_TESTS);
    }

    @Test
    public void testProcessCommand_GenerateTests() {
        // Given
        String userInput = "Generate new API tests for notification-service";

        // When
        JarvisResponse response = jarvisCoreEngine.processCommand(userInput);

        // Then
        assertNotNull(response);
        assertNotNull(response.getMessage());
        assertNotEquals(JarvisResponseStatus.ERROR, response.getStatus());
        
        // Verify action was created
        assertNotNull(response.getAction());
        assertEquals(ActionType.GENERATE_TESTS, response.getAction().getType());
    }

    @Test
    public void testProcessCommand_OptimizeTests() {
        // Given
        String userInput = "Optimize test suite to reduce execution time";

        // When
        JarvisResponse response = jarvisCoreEngine.processCommand(userInput);

        // Then
        assertNotNull(response);
        assertNotNull(response.getMessage());
        assertNotEquals(JarvisResponseStatus.ERROR, response.getStatus());
        
        // Verify action was created
        assertNotNull(response.getAction());
        assertEquals(ActionType.OPTIMIZE_TESTS, response.getAction().getType());
    }

    @Test
    public void testProcessCommand_ComplexRequest() {
        // Given
        String userInput = "Run comprehensive security and integration tests for all services with high priority";

        // When
        JarvisResponse response = jarvisCoreEngine.processCommand(userInput);

        // Then
        assertNotNull(response);
        assertNotNull(response.getMessage());
        assertNotEquals(JarvisResponseStatus.ERROR, response.getStatus());
        
        // Verify the response contains expected information
        assertTrue(response.getMessage().contains("security") || response.getMessage().contains("integration"));
        
        // Verify action was created with high priority
        assertNotNull(response.getAction());
        assertEquals(Priority.HIGH, response.getAction().getPriority());
    }

    @Test
    public void testProcessCommand_UnknownRequest() {
        // Given
        String userInput = "This is a completely unknown request that should not be understood";

        // When
        JarvisResponse response = jarvisCoreEngine.processCommand(userInput);

        // Then
        assertNotNull(response);
        // Should still provide a response, even if it's a fallback
        assertNotNull(response.getMessage());
    }

    @Test
    public void testGetSystemStatus() {
        // When
        SystemStatus status = jarvisCoreEngine.getSystemStatus();

        // Then
        assertNotNull(status);
        // System status should be available even if no tests are running
    }

    @Test
    public void testGetLearningInsights() {
        // When
        LearningInsights insights = jarvisCoreEngine.getLearningInsights();

        // Then
        assertNotNull(insights);
        // Learning insights should be available even if no learning has occurred yet
    }

    @Test
    public void testProcessCommand_WithParameters() {
        // Given
        String userInput = "Run smoke tests on gateway-service with timeout 30 seconds";

        // When
        JarvisResponse response = jarvisCoreEngine.processCommand(userInput);

        // Then
        assertNotNull(response);
        assertNotNull(response.getMessage());
        assertNotEquals(JarvisResponseStatus.ERROR, response.getStatus());
        
        // Verify action was created with parameters
        assertNotNull(response.getAction());
        assertNotNull(response.getAction().getParameters());
        
        // Should contain service and timeout parameters
        Map<String, Object> params = response.getAction().getParameters();
        assertTrue(params.containsKey("services") || params.containsKey("timeout"));
    }

    @Test
    public void testProcessCommand_MultipleServices() {
        // Given
        String userInput = "Run unit tests for user-service, product-service, and order-service in parallel";

        // When
        JarvisResponse response = jarvisCoreEngine.processCommand(userInput);

        // Then
        assertNotNull(response);
        assertNotNull(response.getMessage());
        assertNotEquals(JarvisResponseStatus.ERROR, response.getStatus());
        
        // Verify action was created
        assertNotNull(response.getAction());
        assertNotNull(response.getAction().getParameters());
        
        // Should contain multiple services
        Map<String, Object> params = response.getAction().getParameters();
        if (params.containsKey("services")) {
            @SuppressWarnings("unchecked")
            List<String> services = (List<String>) params.get("services");
            assertTrue(services.size() >= 2, "Should contain multiple services");
        }
    }
}
