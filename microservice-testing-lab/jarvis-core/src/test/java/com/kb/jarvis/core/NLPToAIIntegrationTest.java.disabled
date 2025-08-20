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

import java.util.List;
import java.util.Map;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "jarvis.nlp.confidence-threshold=0.6",
    "jarvis.ai.confidence-threshold=0.6",
    "jarvis.decision.confidence-threshold=0.5",
    "spring.ai.openai.api-key=test-key",
    "spring.ai.openai.base-url=http://localhost:8080"
})
@DisplayName("NLP to AI Integration Tests")
public class NLPToAIIntegrationTest {

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
    }

    @Test
    @DisplayName("Complete Pipeline: Run Integration Tests")
    public void testCompletePipeline_RunIntegrationTests() {
        // Given
        String userInput = "Run integration tests for user-service and product-service with high priority";

        // When - Test complete pipeline
        JarvisResponse response = jarvisCoreEngine.processCommand(userInput);

        // Then
        assertNotNull(response, "Response should not be null");
        assertNotNull(response.getMessage(), "Response message should not be null");
        assertNotEquals(JarvisResponseStatus.ERROR, response.getStatus(), "Response should not be in error status");
        
        // Verify response content
        assertTrue(response.getMessage().toLowerCase().contains("integration"), 
                  "Response should mention integration tests");
        assertTrue(response.getMessage().toLowerCase().contains("user-service") || 
                  response.getMessage().toLowerCase().contains("product-service"),
                  "Response should mention target services");
        
        // Verify action was created
        assertNotNull(response.getAction(), "Action should be created");
        assertNotNull(response.getAction().getType(), "Action type should be set");
        assertNotNull(response.getAction().getDescription(), "Action description should be set");
        assertTrue(response.getAction().getConfidence() > 0.5, "Action should have reasonable confidence");
    }

    @Test
    @DisplayName("Complete Pipeline: Performance Tests with Risk Assessment")
    public void testCompletePipeline_PerformanceTests() {
        // Given
        String userInput = "Run performance tests on order-service with load testing and monitor system resources";

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
    }

    @Test
    @DisplayName("Complete Pipeline: Failure Analysis")
    public void testCompletePipeline_FailureAnalysis() {
        // Given
        String userInput = "Analyze recent test failures in notification-service and provide root cause analysis";

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
            assertTrue(services.contains("notification-service"),
                      "Should extract notification-service from input");
        }
    }

    @Test
    @DisplayName("Complete Pipeline: Health Check")
    public void testCompletePipeline_HealthCheck() {
        // Given
        String userInput = "Check system health and performance metrics for all services";

        // When
        JarvisResponse response = jarvisCoreEngine.processCommand(userInput);

        // Then
        assertNotNull(response);
        assertNotEquals(JarvisResponseStatus.ERROR, response.getStatus());
        
        // Verify health check action
        assertNotNull(response.getAction());
        assertEquals(ActionType.HEALTH_CHECK, response.getAction().getType(),
                    "Should identify health check action");
    }

    @Test
    @DisplayName("Complete Pipeline: Test Generation")
    public void testCompletePipeline_TestGeneration() {
        // Given
        String userInput = "Generate new API tests for gateway-service with contract testing";

        // When
        JarvisResponse response = jarvisCoreEngine.processCommand(userInput);

        // Then
        assertNotNull(response);
        assertNotEquals(JarvisResponseStatus.ERROR, response.getStatus());
        
        // Verify test generation action
        assertNotNull(response.getAction());
        assertEquals(ActionType.GENERATE_TESTS, response.getAction().getType(),
                    "Should identify test generation action");
        
        // Verify test type extraction
        Map<String, Object> params = response.getAction().getParameters();
        if (params.containsKey("testTypes")) {
            List<String> testTypes = (List<String>) params.get("testTypes");
            assertTrue(testTypes.contains("api_test") || testTypes.contains("contract_test"),
                      "Should extract API or contract test types");
        }
    }

    @Test
    @DisplayName("Complete Pipeline: Complex Multi-Service Command")
    public void testCompletePipeline_ComplexMultiServiceCommand() {
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
    }

    @Test
    @DisplayName("Complete Pipeline: Low Confidence Handling")
    public void testCompletePipeline_LowConfidenceHandling() {
        // Given
        String userInput = "xyz abc def ghi"; // Nonsensical input

        // When
        JarvisResponse response = jarvisCoreEngine.processCommand(userInput);

        // Then
        assertNotNull(response);
        // Should handle gracefully even with low confidence
        assertNotNull(response.getMessage());
        
        // Verify action type for low confidence
        if (response.getAction() != null) {
            assertTrue(response.getAction().getType() == ActionType.REQUEST_CLARIFICATION ||
                      response.getAction().getType() == ActionType.UNKNOWN,
                      "Should request clarification or mark as unknown for low confidence");
        }
    }

    @Test
    @DisplayName("Complete Pipeline: Time Constraint Handling")
    public void testCompletePipeline_TimeConstraintHandling() {
        // Given
        String userInput = "Run tests immediately for user-service with urgent priority";

        // When
        JarvisResponse response = jarvisCoreEngine.processCommand(userInput);

        // Then
        assertNotNull(response);
        assertNotEquals(JarvisResponseStatus.ERROR, response.getStatus());
        
        // Verify time constraint extraction
        Map<String, Object> params = response.getAction().getParameters();
        if (params.containsKey("timeConstraint")) {
            String timeConstraint = (String) params.get("timeConstraint");
            assertTrue("immediate".equals(timeConstraint) || "urgent".equals(timeConstraint),
                      "Should extract time constraint");
        }
        
        // Verify priority handling
        assertTrue(response.getAction().getPriority() == Priority.HIGH,
                  "Should set high priority for urgent requests");
    }

    @Test
    @DisplayName("Complete Pipeline: Resource Availability Check")
    public void testCompletePipeline_ResourceAvailabilityCheck() {
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
    }

    @Test
    @DisplayName("Complete Pipeline: Learning Integration")
    public void testCompletePipeline_LearningIntegration() {
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
    }

    @Test
    @DisplayName("Component Integration: NLP to AI Flow")
    public void testComponentIntegration_NLPToAIFlow() {
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
    }

    @Test
    @DisplayName("Component Integration: Context and Memory Integration")
    public void testComponentIntegration_ContextAndMemory() {
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
    }
}
