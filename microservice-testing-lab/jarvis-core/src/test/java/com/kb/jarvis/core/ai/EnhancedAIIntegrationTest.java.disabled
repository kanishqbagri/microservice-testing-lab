package com.kb.jarvis.core.ai;

import com.kb.jarvis.core.controller.EnhancedAIController;
import com.kb.jarvis.core.model.IntentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for Enhanced AI Engine
 * Tests the complete integration with the existing Jarvis system
 */
@SpringBootTest
@ActiveProfiles("test")
public class EnhancedAIIntegrationTest {
    
    @Autowired
    private EnhancedAIEngine enhancedAIEngine;
    
    @Autowired
    private EnhancedAIController enhancedAIController;
    
    @Test
    void testEnhancedAIEngineIntegration() {
        // Test that the enhanced AI engine is properly integrated
        assertNotNull(enhancedAIEngine);
        assertNotNull(enhancedAIController);
    }
    
    @Test
    void testMainExampleCommand() {
        // Test the main example: "Run chaos on Orders"
        String input = "Run chaos on Orders";
        
        EnhancedAIEngine.AIProcessingResult result = enhancedAIEngine.processInput(input);
        
        // Verify the result structure
        assertNotNull(result);
        assertEquals(input, result.getOriginalInput());
        assertTrue(result.getProcessingConfidence() > 0.5);
        
        // Verify intent parsing
        assertNotNull(result.getParsedIntent());
        assertEquals(IntentType.RUN_TESTS, result.getParsedIntent().getType());
        
        // Verify action mapping
        assertNotNull(result.getExecutableAction());
        assertEquals("RUN_CHAOS_TEST", result.getExecutableAction().getActionType());
        assertEquals("order-service", result.getExecutableAction().getServiceName());
        assertEquals("CHAOS_TEST", result.getExecutableAction().getTestType());
        
        // Verify context processing
        assertNotNull(result.getContextualIntent());
        assertTrue(result.getContextualIntent().getAffectedServices().contains("order-service"));
        
        // Verify suggestions and warnings
        assertNotNull(result.getSuggestions());
        assertFalse(result.getSuggestions().isEmpty());
        assertNotNull(result.getWarnings());
        assertTrue(result.getWarnings().stream().anyMatch(w -> w.contains("CHAOS")));
    }
    
    @Test
    void testMultipleCommandTypes() {
        // Test various command types
        String[] commands = {
            "Run chaos on Orders",
            "Execute performance tests for user service",
            "Analyze failures in product service",
            "Check health status of gateway service"
        };
        
        for (String command : commands) {
            EnhancedAIEngine.AIProcessingResult result = enhancedAIEngine.processInput(command);
            
            assertNotNull(result, "Result should not be null for command: " + command);
            assertEquals(command, result.getOriginalInput());
            assertTrue(result.getProcessingConfidence() > 0.3, 
                "Processing confidence should be > 0.3 for command: " + command);
            
            // Verify basic structure
            assertNotNull(result.getParsedIntent());
            assertNotNull(result.getContextualIntent());
            assertNotNull(result.getExecutableAction());
            assertNotNull(result.getSuggestions());
            assertNotNull(result.getWarnings());
        }
    }
    
    @Test
    void testErrorHandling() {
        // Test error handling with invalid input
        String invalidInput = "completely invalid gibberish";
        
        EnhancedAIEngine.AIProcessingResult result = enhancedAIEngine.processInput(invalidInput);
        
        assertNotNull(result);
        assertEquals(invalidInput, result.getOriginalInput());
        assertTrue(result.getProcessingConfidence() < 0.5);
        
        // Should still provide suggestions for improvement
        assertNotNull(result.getSuggestions());
        assertFalse(result.getSuggestions().isEmpty());
    }
    
    @Test
    void testStatisticsEndpoint() {
        // Test that statistics can be retrieved
        EnhancedAIEngine.ProcessingStatistics stats = enhancedAIEngine.getProcessingStatistics();
        
        assertNotNull(stats);
        assertTrue(stats.getTotalProcessed() >= 0);
        assertTrue(stats.getSuccessfulProcessings() >= 0);
        assertTrue(stats.getAverageConfidence() >= 0.0 && stats.getAverageConfidence() <= 1.0);
        assertNotNull(stats.getMostCommonIntent());
        assertNotNull(stats.getAverageProcessingTime());
    }
    
    @Test
    void testExampleCommandsProcessing() {
        // Test processing of example commands
        var results = enhancedAIEngine.processExampleCommands();
        
        assertNotNull(results);
        assertFalse(results.isEmpty());
        
        // Verify each example was processed
        for (var entry : results.entrySet()) {
            String command = entry.getKey();
            EnhancedAIEngine.AIProcessingResult result = entry.getValue();
            
            assertNotNull(result, "Result should not be null for command: " + command);
            assertEquals(command, result.getOriginalInput());
            assertTrue(result.getProcessingConfidence() > 0.0, 
                "Processing confidence should be > 0 for command: " + command);
        }
    }
}
