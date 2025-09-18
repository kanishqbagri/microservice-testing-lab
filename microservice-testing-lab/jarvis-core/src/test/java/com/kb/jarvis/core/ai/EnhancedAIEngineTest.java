package com.kb.jarvis.core.ai;

import com.kb.jarvis.core.model.IntentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test for Enhanced AI Engine
 * Tests the complete pipeline from natural language input to executable actions
 */
@SpringBootTest
@ActiveProfiles("test")
public class EnhancedAIEngineTest {
    
    @Autowired
    private EnhancedAIEngine enhancedAIEngine;
    
    @Autowired
    private EnhancedNLPEngine enhancedNLPEngine;
    
    @Autowired
    private IntelligentActionMapper actionMapper;
    
    @Autowired
    private ContextAwareProcessor contextProcessor;
    
    @BeforeEach
    void setUp() {
        // Any setup needed for tests
    }
    
    @Test
    void testCompletePipeline_ChaosTestCommand() {
        // Test the main example: "Run chaos on Orders"
        String input = "Run chaos on Orders";
        
        EnhancedAIEngine.AIProcessingResult result = enhancedAIEngine.processInput(input);
        
        // Verify overall result
        assertNotNull(result);
        assertEquals(input, result.getOriginalInput());
        assertTrue(result.getProcessingConfidence() > 0.5);
        
        // Verify parsed intent
        assertNotNull(result.getParsedIntent());
        assertEquals(IntentType.RUN_TESTS, result.getParsedIntent().getType());
        assertTrue(result.getParsedIntent().getConfidence() > 0.7);
        
        // Verify contextual intent
        assertNotNull(result.getContextualIntent());
        assertTrue(result.getContextualIntent().getAffectedServices().contains("order-service"));
        assertTrue(result.getContextualIntent().getContextConfidence() > 0.5);
        
        // Verify executable action
        assertNotNull(result.getExecutableAction());
        assertEquals("RUN_CHAOS_TEST", result.getExecutableAction().getActionType());
        assertEquals("order-service", result.getExecutableAction().getServiceName());
        assertEquals("CHAOS_TEST", result.getExecutableAction().getTestType());
        
        // Verify suggestions and warnings
        assertNotNull(result.getSuggestions());
        assertFalse(result.getSuggestions().isEmpty());
        assertNotNull(result.getWarnings());
        assertTrue(result.getWarnings().stream().anyMatch(w -> w.contains("CHAOS")));
    }
    
    @Test
    void testNLPEngine_ServiceExtraction() {
        // Test service name extraction with various formats
        String[] testInputs = {
            "Run tests on user service",
            "Execute chaos testing for orders",
            "Check health of product-service",
            "Analyze failures in notification service"
        };
        
        for (String input : testInputs) {
            var intent = enhancedNLPEngine.parseIntent(input);
            assertNotNull(intent);
            assertTrue(intent.getConfidence() > 0.5);
            
            // Verify service extraction
            assertNotNull(intent.getParameters());
            assertTrue(intent.getParameters().containsKey("services") || 
                      intent.getParameters().containsKey("serviceName"));
        }
    }
    
    @Test
    void testNLPEngine_TestTypeExtraction() {
        // Test test type extraction
        String[] testInputs = {
            "Run unit tests",
            "Execute integration testing",
            "Start performance tests",
            "Launch chaos testing",
            "Run @apitest",
            "Execute @chaostest"
        };
        
        for (String input : testInputs) {
            var intent = enhancedNLPEngine.parseIntent(input);
            assertNotNull(intent);
            assertTrue(intent.getConfidence() > 0.5);
            
            // Verify test type extraction
            assertNotNull(intent.getParameters());
            assertTrue(intent.getParameters().containsKey("testTypes"));
        }
    }
    
    @Test
    void testActionMapper_ChaosTestMapping() {
        // Test action mapping for chaos test
        var intent = enhancedNLPEngine.parseIntent("Run chaos on Orders");
        var contextualIntent = contextProcessor.processWithContext(intent);
        var action = actionMapper.mapToAction(contextualIntent.getOriginalIntent());
        
        assertNotNull(action);
        assertEquals("RUN_CHAOS_TEST", action.getActionType());
        assertEquals("order-service", action.getServiceName());
        assertEquals("CHAOS_TEST", action.getTestType());
        assertTrue(action.getConfidence() > 0.5);
        assertNotNull(action.getParameters());
        assertTrue(action.getParameters().containsKey("chaosLevel"));
    }
    
    @Test
    void testActionMapper_PerformanceTestMapping() {
        // Test action mapping for performance test
        var intent = enhancedNLPEngine.parseIntent("Execute performance tests for user service");
        var contextualIntent = contextProcessor.processWithContext(intent);
        var action = actionMapper.mapToAction(contextualIntent.getOriginalIntent());
        
        assertNotNull(action);
        assertEquals("RUN_PERFORMANCE_TEST", action.getActionType());
        assertEquals("user-service", action.getServiceName());
        assertEquals("PERFORMANCE_TEST", action.getTestType());
        assertTrue(action.getConfidence() > 0.5);
        assertNotNull(action.getParameters());
        assertTrue(action.getParameters().containsKey("loadLevel"));
    }
    
    @Test
    void testContextProcessor_ServiceDependencies() {
        // Test service dependency analysis
        var intent = enhancedNLPEngine.parseIntent("Run tests on order service");
        var contextualIntent = contextProcessor.processWithContext(intent);
        
        assertNotNull(contextualIntent);
        assertNotNull(contextualIntent.getAffectedServices());
        
        // Order service should have dependencies
        assertTrue(contextualIntent.getAffectedServices().contains("order-service"));
        // Should also include dependencies like user-service, product-service
        assertTrue(contextualIntent.getAffectedServices().size() > 1);
    }
    
    @Test
    void testContextProcessor_RiskAssessment() {
        // Test risk assessment for different test types
        String[] highRiskInputs = {
            "Run chaos tests on all services",
            "Execute stress testing for order service"
        };
        
        for (String input : highRiskInputs) {
            var intent = enhancedNLPEngine.parseIntent(input);
            var contextualIntent = contextProcessor.processWithContext(intent);
            
            assertNotNull(contextualIntent.getExecutionContext());
            assertEquals("HIGH", contextualIntent.getExecutionContext().getRiskLevel());
        }
    }
    
    @Test
    void testExampleCommands() {
        // Test processing of example commands
        Map<String, EnhancedAIEngine.AIProcessingResult> results = 
            enhancedAIEngine.processExampleCommands();
        
        assertNotNull(results);
        assertFalse(results.isEmpty());
        
        // Verify each example was processed
        for (Map.Entry<String, EnhancedAIEngine.AIProcessingResult> entry : results.entrySet()) {
            String command = entry.getKey();
            EnhancedAIEngine.AIProcessingResult result = entry.getValue();
            
            assertNotNull(result, "Result should not be null for command: " + command);
            assertEquals(command, result.getOriginalInput());
            assertTrue(result.getProcessingConfidence() > 0.0, 
                "Processing confidence should be > 0 for command: " + command);
        }
    }
    
    @Test
    void testFuzzyMatching() {
        // Test fuzzy matching for service names
        String[] fuzzyInputs = {
            "Run tests on usr service",  // typo in "user"
            "Execute chaos for ordr",    // typo in "order"
            "Check health of prodct"     // typo in "product"
        };
        
        for (String input : fuzzyInputs) {
            var intent = enhancedNLPEngine.parseIntent(input);
            assertNotNull(intent);
            // Should still extract services despite typos
            assertTrue(intent.getConfidence() > 0.3);
        }
    }
    
    @Test
    void testConfidenceScoring() {
        // Test confidence scoring for different input qualities
        String[] highConfidenceInputs = {
            "Run chaos tests on order service",
            "Execute performance tests for user service",
            "Check health status of gateway service"
        };
        
        String[] lowConfidenceInputs = {
            "Do something",
            "Test stuff",
            "Run things"
        };
        
        for (String input : highConfidenceInputs) {
            var result = enhancedAIEngine.processInput(input);
            assertTrue(result.getProcessingConfidence() > 0.7, 
                "High confidence expected for: " + input);
        }
        
        for (String input : lowConfidenceInputs) {
            var result = enhancedAIEngine.processInput(input);
            assertTrue(result.getProcessingConfidence() < 0.7, 
                "Low confidence expected for: " + input);
        }
    }
    
    @Test
    void testParameterExtraction() {
        // Test parameter extraction (timeouts, retries, etc.)
        String[] parameterInputs = {
            "Run tests with 30 second timeout",
            "Execute chaos tests with 3 retries",
            "Start performance tests in parallel"
        };
        
        for (String input : parameterInputs) {
            var intent = enhancedNLPEngine.parseIntent(input);
            assertNotNull(intent.getParameters());
            
            // Should extract parameters
            assertTrue(intent.getParameters().containsKey("parameters"));
        }
    }
    
    @Test
    void testPriorityExtraction() {
        // Test priority extraction
        String[] priorityInputs = {
            "Run urgent chaos tests",
            "Execute high priority performance tests",
            "Start low priority smoke tests"
        };
        
        for (String input : priorityInputs) {
            var intent = enhancedNLPEngine.parseIntent(input);
            var contextualIntent = contextProcessor.processWithContext(intent);
            var action = actionMapper.mapToAction(contextualIntent.getOriginalIntent());
            
            assertNotNull(action.getPriority());
            assertTrue(action.getPriority().matches("HIGH|MEDIUM|LOW|NORMAL"));
        }
    }
    
    @Test
    void testErrorHandling() {
        // Test error handling for invalid inputs
        String[] invalidInputs = {
            "",
            null,
            "   ",
            "completely invalid gibberish that makes no sense"
        };
        
        for (String input : invalidInputs) {
            if (input == null) continue; // Skip null test as it's handled differently
            
            var result = enhancedAIEngine.processInput(input);
            assertNotNull(result);
            // Should handle gracefully with low confidence
            assertTrue(result.getProcessingConfidence() < 0.5);
        }
    }
}
