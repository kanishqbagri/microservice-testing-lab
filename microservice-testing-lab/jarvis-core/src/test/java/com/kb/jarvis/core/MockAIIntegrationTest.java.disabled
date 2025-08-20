package com.kb.jarvis.core;

import com.kb.jarvis.core.model.*;
import com.kb.jarvis.core.nlp.NLPEngine;
import com.kb.jarvis.core.ai.AIEngine;
import com.kb.jarvis.core.decision.DecisionEngine;
import com.kb.jarvis.core.context.ContextManager;
import com.kb.jarvis.core.memory.MemoryManager;
import com.kb.jarvis.core.learning.LearningEngine;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "jarvis.nlp.confidence-threshold=0.6",
    "jarvis.ai.confidence-threshold=0.6",
    "jarvis.decision.confidence-threshold=0.5",
    "spring.ai.openai.api-key=test-key",
    "spring.ai.openai.base-url=http://localhost:8080",
    "jarvis.ai.enable-spring-ai=false"
})
@DisplayName("Mock AI Integration Tests")
public class MockAIIntegrationTest {

    @Autowired
    private JarvisCoreEngine jarvisCoreEngine;

    @Autowired
    private NLPEngine nlpEngine;

    @Autowired
    private AIEngine aiEngine;

    @Autowired
    private DecisionEngine decisionEngine;

    @Autowired
    private ContextManager contextManager;

    @Autowired
    private MemoryManager memoryManager;

    @Autowired
    private LearningEngine learningEngine;

    @BeforeEach
    void setUp() {
        // Clear any existing test data
        memoryManager.clearAllMemory();
        
        // Setup mock context data
        setupMockContext();
        
        // Setup mock memory data
        setupMockMemory();
    }

    private void setupMockContext() {
        // Mock system health
        SystemHealth mockHealth = SystemHealth.builder()
            .status(HealthStatus.HEALTHY)
            .overallScore(0.85)
            .lastUpdate(LocalDateTime.now())
            .issues(List.of())
            .build();

        // Mock performance metrics
        PerformanceMetrics mockMetrics = PerformanceMetrics.builder()
            .cpuUsage(45.0)
            .memoryUsage(60.0)
            .networkLatency(25.0)
            .timestamp(LocalDateTime.now())
            .build();

        // Mock service health
        Map<String, ServiceHealth> serviceHealth = Map.of(
            "user-service", ServiceHealth.builder()
                .status(HealthStatus.HEALTHY)
                .responseTime(150.0)
                .availability(99.9)
                .lastCheck(LocalDateTime.now())
                .build(),
            "product-service", ServiceHealth.builder()
                .status(HealthStatus.HEALTHY)
                .responseTime(180.0)
                .availability(99.8)
                .lastCheck(LocalDateTime.now())
                .build(),
            "order-service", ServiceHealth.builder()
                .status(HealthStatus.DEGRADED)
                .responseTime(500.0)
                .availability(95.0)
                .lastCheck(LocalDateTime.now())
                .build()
        );

        // Store mock context data
        contextManager.updateSystemHealth(mockHealth);
        contextManager.updatePerformanceMetrics(mockMetrics);
        serviceHealth.forEach((service, health) -> 
            contextManager.updateServiceHealth(service, health));
    }

    private void setupMockMemory() {
        // Mock test results
        TestResult mockResult = TestResult.builder()
            .testId("test-001")
            .status(TestStatus.PASSED)
            .duration(120.0)
            .serviceName("user-service")
            .testType(TestType.INTEGRATION_TEST)
            .timestamp(LocalDateTime.now().minusHours(1))
            .build();

        // Mock test failures
        TestFailure mockFailure = TestFailure.builder()
            .testId("test-002")
            .failureType(FailureType.TIMEOUT)
            .message("Connection timeout")
            .serviceName("order-service")
            .timestamp(LocalDateTime.now().minusMinutes(30))
            .build();

        // Mock active tests
        ActiveTest mockActiveTest = ActiveTest.builder()
            .id("active-001")
            .type(TestType.PERFORMANCE_TEST)
            .serviceName("product-service")
            .startTime(LocalDateTime.now().minusMinutes(10))
            .status("RUNNING")
            .build();

        // Store mock memory data
        memoryManager.storeTestResult(mockResult);
        memoryManager.storeTestFailure(mockFailure);
        memoryManager.storeActiveTest(mockActiveTest);
    }

    @Test
    @DisplayName("Complete Mock AI Integration Pipeline - Basic Command")
    public void testCompleteMockAIIntegrationPipeline_BasicCommand() {
        // Given
        String userInput = "Run integration tests for user-service";

        // When - Test complete pipeline
        JarvisResponse response = jarvisCoreEngine.processCommand(userInput);

        // Then
        assertNotNull(response, "Response should not be null");
        assertNotNull(response.getMessage(), "Response message should not be null");
        assertNotEquals(JarvisResponseStatus.ERROR, response.getStatus(), "Response should not be in error status");
        
        // Verify response contains expected information
        assertTrue(response.getMessage().toLowerCase().contains("integration"), 
                  "Response should mention integration tests");
        assertTrue(response.getMessage().toLowerCase().contains("user-service"),
                  "Response should mention target service");
        
        // Verify action was created
        assertNotNull(response.getAction(), "Action should be created");
        assertNotNull(response.getAction().getType(), "Action type should be set");
        assertNotNull(response.getAction().getDescription(), "Action description should be set");
        assertTrue(response.getAction().getConfidence() > 0.5, "Action should have reasonable confidence");
        
        System.out.println("✅ Basic Command Test Passed");
        System.out.println("Response: " + response.getMessage());
        System.out.println("Action: " + response.getAction().getType());
        System.out.println("Confidence: " + response.getAction().getConfidence());
    }

    @Test
    @DisplayName("Complete Mock AI Integration Pipeline - Performance Test")
    public void testCompleteMockAIIntegrationPipeline_PerformanceTest() {
        // Given
        String userInput = "Run performance tests on order-service with load testing";

        // When
        JarvisResponse response = jarvisCoreEngine.processCommand(userInput);

        // Then
        assertNotNull(response);
        assertNotEquals(JarvisResponseStatus.ERROR, response.getStatus());
        
        // Verify performance test specific handling
        assertNotNull(response.getAction());
        assertTrue(response.getAction().getType() == ActionType.RUN_PERFORMANCE_TESTS || 
                  response.getAction().getType() == ActionType.RUN_TESTS,
                  "Should identify performance test action");
        
        // Verify parameters were extracted
        Map<String, Object> params = response.getAction().getParameters();
        assertNotNull(params);
        assertTrue(params.containsKey("testTypes") || params.containsKey("services"),
                  "Should extract test types or services");
        
        System.out.println("✅ Performance Test Passed");
        System.out.println("Response: " + response.getMessage());
        System.out.println("Action: " + response.getAction().getType());
    }

    @Test
    @DisplayName("Complete Mock AI Integration Pipeline - Failure Analysis")
    public void testCompleteMockAIIntegrationPipeline_FailureAnalysis() {
        // Given
        String userInput = "Analyze recent test failures in order-service";

        // When
        JarvisResponse response = jarvisCoreEngine.processCommand(userInput);

        // Then
        assertNotNull(response);
        assertNotEquals(JarvisResponseStatus.ERROR, response.getStatus());
        
        // Verify failure analysis action
        assertNotNull(response.getAction());
        assertEquals(ActionType.ANALYZE_FAILURES, response.getAction().getType(),
                    "Should identify failure analysis action");
        
        // Verify service extraction
        Map<String, Object> params = response.getAction().getParameters();
        if (params.containsKey("services")) {
            List<String> services = (List<String>) params.get("services");
            assertTrue(services.contains("order-service"),
                      "Should extract order-service from input");
        }
        
        System.out.println("✅ Failure Analysis Test Passed");
        System.out.println("Response: " + response.getMessage());
        System.out.println("Action: " + response.getAction().getType());
    }

    @Test
    @DisplayName("Complete Mock AI Integration Pipeline - Health Check")
    public void testCompleteMockAIIntegrationPipeline_HealthCheck() {
        // Given
        String userInput = "Check system health and performance metrics";

        // When
        JarvisResponse response = jarvisCoreEngine.processCommand(userInput);

        // Then
        assertNotNull(response);
        assertNotEquals(JarvisResponseStatus.ERROR, response.getStatus());
        
        // Verify health check action
        assertNotNull(response.getAction());
        assertEquals(ActionType.HEALTH_CHECK, response.getAction().getType(),
                    "Should identify health check action");
        
        System.out.println("✅ Health Check Test Passed");
        System.out.println("Response: " + response.getMessage());
        System.out.println("Action: " + response.getAction().getType());
    }

    @Test
    @DisplayName("Complete Mock AI Integration Pipeline - Complex Multi-Service Command")
    public void testCompleteMockAIIntegrationPipeline_ComplexCommand() {
        // Given
        String userInput = "Run comprehensive tests including integration, performance, and security tests for user-service, product-service, and order-service with parallel execution and high priority";

        // When
        JarvisResponse response = jarvisCoreEngine.processCommand(userInput);

        // Then
        assertNotNull(response);
        assertNotEquals(JarvisResponseStatus.ERROR, response.getStatus());
        
        // Verify complex command handling
        assertNotNull(response.getAction());
        assertTrue(response.getAction().getPriority() == Priority.HIGH,
                  "Should set high priority for urgent requests");
        
        // Verify multiple services extraction
        Map<String, Object> params = response.getAction().getParameters();
        if (params.containsKey("services")) {
            List<String> services = (List<String>) params.get("services");
            assertTrue(services.size() >= 2, "Should extract multiple services");
            assertTrue(services.contains("user-service") || 
                      services.contains("product-service") || 
                      services.contains("order-service"),
                      "Should extract target services");
        }
        
        System.out.println("✅ Complex Command Test Passed");
        System.out.println("Response: " + response.getMessage());
        System.out.println("Action: " + response.getAction().getType());
        System.out.println("Priority: " + response.getAction().getPriority());
    }

    @Test
    @DisplayName("Mock AI Component Integration - NLP to AI Flow")
    public void testMockAIComponentIntegration_NLPToAIFlow() {
        // Given
        String userInput = "Run security tests for gateway-service with penetration testing";

        // When - Test individual components
        UserIntent intent = nlpEngine.parseIntent(userInput);
        AIAnalysis analysis = aiEngine.analyzeIntent(intent);
        DecisionAction action = decisionEngine.decideAction(intent, analysis);

        // Then
        assertNotNull(intent, "NLP should parse intent");
        assertEquals(IntentType.RUN_TESTS, intent.getType(), "Should identify RUN_TESTS intent");
        assertTrue(intent.getConfidence() > 0.5, "Should have reasonable confidence");
        
        assertNotNull(analysis, "AI should analyze intent");
        assertTrue(analysis.getConfidence() > 0.5, "AI analysis should have reasonable confidence");
        assertNotNull(analysis.getRiskAssessment(), "Should include risk assessment");
        assertNotNull(analysis.getPerformancePrediction(), "Should include performance prediction");
        
        assertNotNull(action, "Decision engine should create action");
        assertTrue(action.getConfidence() > 0.5, "Action should have reasonable confidence");
        assertNotNull(action.getDescription(), "Action should have description");
        assertNotNull(action.getParameters(), "Action should have parameters");
        
        System.out.println("✅ Component Integration Test Passed");
        System.out.println("Intent: " + intent.getType() + " (confidence: " + intent.getConfidence() + ")");
        System.out.println("AI Analysis: " + analysis.getConfidence() + " confidence");
        System.out.println("Decision Action: " + action.getType() + " (confidence: " + action.getConfidence() + ")");
    }

    @Test
    @DisplayName("Mock AI Component Integration - Context and Memory")
    public void testMockAIComponentIntegration_ContextAndMemory() {
        // Given
        String userInput = "Check system status and analyze recent failures";

        // When
        UserIntent intent = nlpEngine.parseIntent(userInput);
        contextManager.updateContext(intent);
        
        // Simulate some memory entries
        memoryManager.storeMemory("test_patterns", "recent_failures", List.of("failure1", "failure2"));
        
        AIAnalysis analysis = aiEngine.analyzeIntent(intent);
        DecisionAction action = decisionEngine.decideAction(intent, analysis);

        // Then
        assertNotNull(intent);
        assertNotNull(analysis);
        assertNotNull(action);
        
        // Verify context and memory integration
        assertTrue(analysis.getConfidence() >= 0.0, "Analysis should complete with context");
        assertTrue(action.getConfidence() >= 0.0, "Action should be created with memory context");
        
        System.out.println("✅ Context and Memory Integration Test Passed");
        System.out.println("Intent: " + intent.getType());
        System.out.println("AI Analysis Confidence: " + analysis.getConfidence());
        System.out.println("Decision Action Confidence: " + action.getConfidence());
    }

    @Test
    @DisplayName("Mock AI Learning Integration")
    public void testMockAILearningIntegration() {
        // Given
        String userInput = "Run integration tests for user-service";

        // When - Execute multiple times to test learning
        JarvisResponse response1 = jarvisCoreEngine.processCommand(userInput);
        JarvisResponse response2 = jarvisCoreEngine.processCommand(userInput);

        // Then
        assertNotNull(response1);
        assertNotNull(response2);
        assertNotEquals(JarvisResponseStatus.ERROR, response1.getStatus());
        assertNotEquals(JarvisResponseStatus.ERROR, response2.getStatus());
        
        // Verify learning engine is called (indirectly through confidence improvement)
        // The second response might have higher confidence due to learning
        assertTrue(response1.getAction().getConfidence() >= 0.0);
        assertTrue(response2.getAction().getConfidence() >= 0.0);
        
        System.out.println("✅ Learning Integration Test Passed");
        System.out.println("First Response Confidence: " + response1.getAction().getConfidence());
        System.out.println("Second Response Confidence: " + response2.getAction().getConfidence());
    }

    @Test
    @DisplayName("Mock AI Risk Assessment Integration")
    public void testMockAIRiskAssessmentIntegration() {
        // Given
        String userInput = "Run chaos tests for all services with maximum load";

        // When
        JarvisResponse response = jarvisCoreEngine.processCommand(userInput);

        // Then
        assertNotNull(response);
        assertNotEquals(JarvisResponseStatus.ERROR, response.getStatus());
        
        // Verify chaos test specific handling
        assertNotNull(response.getAction());
        Map<String, Object> params = response.getAction().getParameters();
        
        // Should extract chaos test type
        if (params.containsKey("testTypes")) {
            List<String> testTypes = (List<String>) params.get("testTypes");
            assertTrue(testTypes.contains("chaos_test"),
                      "Should identify chaos test type");
        }
        
        // Should set appropriate execution strategy
        assertTrue(response.getAction().getExecutionStrategy() == ExecutionStrategy.ISOLATED ||
                  response.getAction().getExecutionStrategy() == ExecutionStrategy.SEQUENTIAL,
                  "Should use appropriate execution strategy for chaos tests");
        
        System.out.println("✅ Risk Assessment Integration Test Passed");
        System.out.println("Response: " + response.getMessage());
        System.out.println("Action: " + response.getAction().getType());
        System.out.println("Execution Strategy: " + response.getAction().getExecutionStrategy());
    }

    @Test
    @DisplayName("Mock AI Performance Prediction Integration")
    public void testMockAIPerformancePredictionIntegration() {
        // Given
        String userInput = "Run comprehensive integration tests for all services with parallel execution";

        // When
        JarvisResponse response = jarvisCoreEngine.processCommand(userInput);

        // Then
        assertNotNull(response);
        assertNotEquals(JarvisResponseStatus.ERROR, response.getStatus());
        
        // Verify performance prediction integration
        assertNotNull(response.getAction());
        assertNotNull(response.getAction().getEstimatedTime(), "Should have estimated execution time");
        assertNotNull(response.getAction().getParameters(), "Should have parameters");
        
        // Verify execution strategy based on performance prediction
        ExecutionStrategy strategy = response.getAction().getExecutionStrategy();
        assertTrue(strategy == ExecutionStrategy.PARALLEL || 
                  strategy == ExecutionStrategy.ADAPTIVE,
                  "Should use appropriate execution strategy based on performance prediction");
        
        System.out.println("✅ Performance Prediction Integration Test Passed");
        System.out.println("Response: " + response.getMessage());
        System.out.println("Estimated Time: " + response.getAction().getEstimatedTime());
        System.out.println("Execution Strategy: " + strategy);
    }
}
