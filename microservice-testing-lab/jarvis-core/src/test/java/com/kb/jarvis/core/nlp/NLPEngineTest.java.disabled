package com.kb.jarvis.core.nlp;

import com.kb.jarvis.core.model.UserIntent;
import com.kb.jarvis.core.model.IntentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

class NLPEngineTest {
    
    private NLPEngine nlpEngine;
    
    @BeforeEach
    void setUp() {
        nlpEngine = new NLPEngine();
    }
    
    @Test
    @DisplayName("Should parse RUN_TESTS intent correctly")
    void shouldParseRunTestsIntent() {
        // Given
        String input = "run tests for user-service";
        
        // When
        UserIntent intent = nlpEngine.parseIntent(input);
        
        // Then
        assertEquals(IntentType.RUN_TESTS, intent.getType());
        assertTrue(intent.getConfidence() > 0.5);
        assertNotNull(intent.getParameters());
        
        @SuppressWarnings("unchecked")
        Map<String, Object> params = (Map<String, Object>) intent.getParameters();
        assertTrue(params.containsKey("services"));
    }
    
    @Test
    @DisplayName("Should parse ANALYZE_FAILURES intent correctly")
    void shouldParseAnalyzeFailuresIntent() {
        // Given
        String input = "analyze failures in product-service";
        
        // When
        UserIntent intent = nlpEngine.parseIntent(input);
        
        // Then
        assertEquals(IntentType.ANALYZE_FAILURES, intent.getType());
        assertTrue(intent.getConfidence() > 0.5);
        assertNotNull(intent.getParameters());
        
        @SuppressWarnings("unchecked")
        Map<String, Object> params = (Map<String, Object>) intent.getParameters();
        assertTrue(params.containsKey("services"));
    }
    
    @Test
    @DisplayName("Should parse GENERATE_TESTS intent correctly")
    void shouldParseGenerateTestsIntent() {
        // Given
        String input = "generate new tests for order-service";
        
        // When
        UserIntent intent = nlpEngine.parseIntent(input);
        
        // Then
        assertEquals(IntentType.GENERATE_TESTS, intent.getType());
        assertTrue(intent.getConfidence() > 0.5);
    }
    
    @Test
    @DisplayName("Should parse OPTIMIZE_TESTS intent correctly")
    void shouldParseOptimizeTestsIntent() {
        // Given
        String input = "optimize test suite for notification-service";
        
        // When
        UserIntent intent = nlpEngine.parseIntent(input);
        
        // Then
        assertEquals(IntentType.OPTIMIZE_TESTS, intent.getType());
        assertTrue(intent.getConfidence() > 0.5);
    }
    
    @Test
    @DisplayName("Should parse HEALTH_CHECK intent correctly")
    void shouldParseHealthCheckIntent() {
        // Given
        String input = "check health status of gateway-service";
        
        // When
        UserIntent intent = nlpEngine.parseIntent(input);
        
        // Then
        assertEquals(IntentType.HEALTH_CHECK, intent.getType());
        assertTrue(intent.getConfidence() > 0.5);
    }
    
    @Test
    @DisplayName("Should extract multiple services correctly")
    void shouldExtractMultipleServices() {
        // Given
        String input = "run tests for user-service and product-service";
        
        // When
        UserIntent intent = nlpEngine.parseIntent(input);
        
        // Then
        @SuppressWarnings("unchecked")
        Map<String, Object> params = (Map<String, Object>) intent.getParameters();
        assertTrue(params.containsKey("services"));
        
        @SuppressWarnings("unchecked")
        var services = (java.util.List<String>) params.get("services");
        assertTrue(services.contains("user-service"));
        assertTrue(services.contains("product-service"));
    }
    
    @Test
    @DisplayName("Should extract test types correctly")
    void shouldExtractTestTypes() {
        // Given
        String input = "run unit tests and integration tests for user-service";
        
        // When
        UserIntent intent = nlpEngine.parseIntent(input);
        
        // Then
        @SuppressWarnings("unchecked")
        Map<String, Object> params = (Map<String, Object>) intent.getParameters();
        assertTrue(params.containsKey("testTypes"));
        
        @SuppressWarnings("unchecked")
        var testTypes = (java.util.List<String>) params.get("testTypes");
        assertTrue(testTypes.contains("UNIT_TEST"));
        assertTrue(testTypes.contains("INTEGRATION_TEST"));
    }
    
    @Test
    @DisplayName("Should extract parameters correctly")
    void shouldExtractParameters() {
        // Given
        String input = "run tests with 30 seconds timeout and 3 retries";
        
        // When
        UserIntent intent = nlpEngine.parseIntent(input);
        
        // Then
        @SuppressWarnings("unchecked")
        Map<String, Object> params = (Map<String, Object>) intent.getParameters();
        assertTrue(params.containsKey("parameters"));
        
        @SuppressWarnings("unchecked")
        Map<String, String> parameters = (Map<String, String>) params.get("parameters");
        assertEquals("30 seconds", parameters.get("timeout"));
        assertEquals("3", parameters.get("retries"));
    }
    
    @Test
    @DisplayName("Should extract priority correctly")
    void shouldExtractPriority() {
        // Given
        String input = "urgently run critical tests for user-service";
        
        // When
        UserIntent intent = nlpEngine.parseIntent(input);
        
        // Then
        @SuppressWarnings("unchecked")
        Map<String, Object> params = (Map<String, Object>) intent.getParameters();
        assertEquals("HIGH", params.get("priority"));
    }
    
    @Test
    @DisplayName("Should handle synonyms correctly")
    void shouldHandleSynonyms() {
        // Given
        String input = "run tests for user service";
        
        // When
        UserIntent intent = nlpEngine.parseIntent(input);
        
        // Then
        @SuppressWarnings("unchecked")
        Map<String, Object> params = (Map<String, Object>) intent.getParameters();
        assertTrue(params.containsKey("services"));
        
        @SuppressWarnings("unchecked")
        var services = (java.util.List<String>) params.get("services");
        assertTrue(services.contains("user-service"));
    }
    
    @Test
    @DisplayName("Should handle unknown intent gracefully")
    void shouldHandleUnknownIntent() {
        // Given
        String input = "random text that doesn't match any patterns";
        
        // When
        UserIntent intent = nlpEngine.parseIntent(input);
        
        // Then
        assertEquals(IntentType.UNKNOWN, intent.getType());
        assertTrue(intent.getConfidence() < 0.5);
    }
    
    @Test
    @DisplayName("Should handle empty input gracefully")
    void shouldHandleEmptyInput() {
        // Given
        String input = "";
        
        // When
        UserIntent intent = nlpEngine.parseIntent(input);
        
        // Then
        assertEquals(IntentType.UNKNOWN, intent.getType());
        assertEquals("Unable to parse intent from input", intent.getDescription());
        assertEquals(0.0, intent.getConfidence());
    }
    
    @Test
    @DisplayName("Should handle null input gracefully")
    void shouldHandleNullInput() {
        // Given
        String input = null;
        
        // When
        UserIntent intent = nlpEngine.parseIntent(input);
        
        // Then
        assertEquals(IntentType.UNKNOWN, intent.getType());
        assertEquals("Unable to parse intent from input", intent.getDescription());
        assertEquals(0.0, intent.getConfidence());
    }
    
    @Test
    @DisplayName("Should generate appropriate descriptions")
    void shouldGenerateAppropriateDescriptions() {
        // Given
        String input = "run unit tests for user-service";
        
        // When
        UserIntent intent = nlpEngine.parseIntent(input);
        
        // Then
        assertTrue(intent.getDescription().contains("run tests"));
        assertTrue(intent.getDescription().contains("user-service"));
    }
    
    @Test
    @DisplayName("Should calculate confidence based on pattern matches")
    void shouldCalculateConfidenceBasedOnPatternMatches() {
        // Given
        String input = "run tests for user-service with high priority";
        
        // When
        UserIntent intent = nlpEngine.parseIntent(input);
        
        // Then
        assertTrue(intent.getConfidence() > 0.7); // Should have high confidence due to multiple matches
    }
    
    @Test
    @DisplayName("Should handle case insensitive matching")
    void shouldHandleCaseInsensitiveMatching() {
        // Given
        String input = "RUN TESTS FOR USER-SERVICE";
        
        // When
        UserIntent intent = nlpEngine.parseIntent(input);
        
        // Then
        assertEquals(IntentType.RUN_TESTS, intent.getType());
        assertTrue(intent.getConfidence() > 0.5);
    }
    
    @Test
    @DisplayName("Should extract time constraints correctly")
    void shouldExtractTimeConstraints() {
        // Given
        String input = "run tests now for user-service";
        
        // When
        UserIntent intent = nlpEngine.parseIntent(input);
        
        // Then
        @SuppressWarnings("unchecked")
        Map<String, Object> params = (Map<String, Object>) intent.getParameters();
        assertEquals("IMMEDIATE", params.get("timeConstraint"));
    }
    
    @Test
    @DisplayName("Should extract test scope correctly")
    void shouldExtractTestScope() {
        // Given
        String input = "run all tests for user-service";
        
        // When
        UserIntent intent = nlpEngine.parseIntent(input);
        
        // Then
        @SuppressWarnings("unchecked")
        Map<String, Object> params = (Map<String, Object>) intent.getParameters();
        assertEquals("ALL", params.get("scope"));
    }
    
    @Test
    @DisplayName("Should recognize Java annotation-style test priorities P1")
    void shouldRecognizeP1Priority() {
        // Given
        String input = "run all the P1 tests for user service";
        
        // When
        UserIntent intent = nlpEngine.parseIntent(input);
        
        // Then
        @SuppressWarnings("unchecked")
        Map<String, Object> params = (Map<String, Object>) intent.getParameters();
        assertEquals("P1", params.get("scope"));
        assertEquals("P1", params.get("priority"));
    }
    
    @Test
    @DisplayName("Should recognize Java annotation-style test priorities P2")
    void shouldRecognizeP2Priority() {
        // Given
        String input = "run P2 tests for product-service";
        
        // When
        UserIntent intent = nlpEngine.parseIntent(input);
        
        // Then
        @SuppressWarnings("unchecked")
        Map<String, Object> params = (Map<String, Object>) intent.getParameters();
        assertEquals("P2", params.get("scope"));
        assertEquals("P2", params.get("priority"));
    }
    
    @Test
    @DisplayName("Should recognize Java annotation-style test priorities P3")
    void shouldRecognizeP3Priority() {
        // Given
        String input = "execute P3 integration tests for order-service";
        
        // When
        UserIntent intent = nlpEngine.parseIntent(input);
        
        // Then
        @SuppressWarnings("unchecked")
        Map<String, Object> params = (Map<String, Object>) intent.getParameters();
        assertEquals("P3", params.get("scope"));
        assertEquals("P3", params.get("priority"));
    }
    
    @Test
    @DisplayName("Should recognize Java annotation-style test priorities P0")
    void shouldRecognizeP0Priority() {
        // Given
        String input = "run P0 smoke tests for notification-service";
        
        // When
        UserIntent intent = nlpEngine.parseIntent(input);
        
        // Then
        @SuppressWarnings("unchecked")
        Map<String, Object> params = (Map<String, Object>) intent.getParameters();
        assertEquals("P0", params.get("scope"));
        assertEquals("P0", params.get("priority"));
    }
    
    @Test
    @DisplayName("Should recognize @Test annotation")
    void shouldRecognizeTestAnnotation() {
        // Given
        String input = "run @Test for user-service";
        
        // When
        UserIntent intent = nlpEngine.parseIntent(input);
        
        // Then
        @SuppressWarnings("unchecked")
        Map<String, Object> params = (Map<String, Object>) intent.getParameters();
        assertTrue(params.containsKey("testTypes"));
        
        @SuppressWarnings("unchecked")
        var testTypes = (java.util.List<String>) params.get("testTypes");
        assertTrue(testTypes.contains("UNIT_TEST"));
    }
    
    @Test
    @DisplayName("Should recognize @IntegrationTest annotation")
    void shouldRecognizeIntegrationTestAnnotation() {
        // Given
        String input = "run @IntegrationTest for product-service";
        
        // When
        UserIntent intent = nlpEngine.parseIntent(input);
        
        // Then
        @SuppressWarnings("unchecked")
        Map<String, Object> params = (Map<String, Object>) intent.getParameters();
        assertTrue(params.containsKey("testTypes"));
        
        @SuppressWarnings("unchecked")
        var testTypes = (java.util.List<String>) params.get("testTypes");
        assertTrue(testTypes.contains("INTEGRATION_TEST"));
    }
    
    @Test
    @DisplayName("Should recognize @ApiTest annotation")
    void shouldRecognizeApiTestAnnotation() {
        // Given
        String input = "execute @ApiTest for gateway-service";
        
        // When
        UserIntent intent = nlpEngine.parseIntent(input);
        
        // Then
        @SuppressWarnings("unchecked")
        Map<String, Object> params = (Map<String, Object>) intent.getParameters();
        assertTrue(params.containsKey("testTypes"));
        
        @SuppressWarnings("unchecked")
        var testTypes = (java.util.List<String>) params.get("testTypes");
        assertTrue(testTypes.contains("API_TEST"));
    }
    
    @Test
    @DisplayName("Should recognize @PerformanceTest annotation")
    void shouldRecognizePerformanceTestAnnotation() {
        // Given
        String input = "run @PerformanceTest for user-service";
        
        // When
        UserIntent intent = nlpEngine.parseIntent(input);
        
        // Then
        @SuppressWarnings("unchecked")
        Map<String, Object> params = (Map<String, Object>) intent.getParameters();
        assertTrue(params.containsKey("testTypes"));
        
        @SuppressWarnings("unchecked")
        var testTypes = (java.util.List<String>) params.get("testTypes");
        assertTrue(testTypes.contains("PERFORMANCE_TEST"));
    }
    
    @Test
    @DisplayName("Should recognize @SmokeTest annotation")
    void shouldRecognizeSmokeTestAnnotation() {
        // Given
        String input = "execute @SmokeTest for all services";
        
        // When
        UserIntent intent = nlpEngine.parseIntent(input);
        
        // Then
        @SuppressWarnings("unchecked")
        Map<String, Object> params = (Map<String, Object>) intent.getParameters();
        assertTrue(params.containsKey("testTypes"));
        
        @SuppressWarnings("unchecked")
        var testTypes = (java.util.List<String>) params.get("testTypes");
        assertTrue(testTypes.contains("SMOKE_TEST"));
    }
    
    @Test
    @DisplayName("Should recognize @E2ETest annotation")
    void shouldRecognizeE2ETestAnnotation() {
        // Given
        String input = "run @E2ETest for user-service";
        
        // When
        UserIntent intent = nlpEngine.parseIntent(input);
        
        // Then
        @SuppressWarnings("unchecked")
        Map<String, Object> params = (Map<String, Object>) intent.getParameters();
        assertTrue(params.containsKey("testTypes"));
        
        @SuppressWarnings("unchecked")
        var testTypes = (java.util.List<String>) params.get("testTypes");
        assertTrue(testTypes.contains("END_TO_END_TEST"));
    }
    
    @Test
    @DisplayName("Should recognize @ServiceTest annotation")
    void shouldRecognizeServiceTestAnnotation() {
        // Given
        String input = "execute @ServiceTest for order-service";
        
        // When
        UserIntent intent = nlpEngine.parseIntent(input);
        
        // Then
        @SuppressWarnings("unchecked")
        Map<String, Object> params = (Map<String, Object>) intent.getParameters();
        assertTrue(params.containsKey("testTypes"));
        
        @SuppressWarnings("unchecked")
        var testTypes = (java.util.List<String>) params.get("testTypes");
        assertTrue(testTypes.contains("SERVICE_TEST"));
    }
    
    @Test
    @DisplayName("Should recognize @ControllerTest annotation")
    void shouldRecognizeControllerTestAnnotation() {
        // Given
        String input = "run @ControllerTest for user-service";
        
        // When
        UserIntent intent = nlpEngine.parseIntent(input);
        
        // Then
        @SuppressWarnings("unchecked")
        Map<String, Object> params = (Map<String, Object>) intent.getParameters();
        assertTrue(params.containsKey("testTypes"));
        
        @SuppressWarnings("unchecked")
        var testTypes = (java.util.List<String>) params.get("testTypes");
        assertTrue(testTypes.contains("CONTROLLER_TEST"));
    }
    
    @Test
    @DisplayName("Should recognize @RepositoryTest annotation")
    void shouldRecognizeRepositoryTestAnnotation() {
        // Given
        String input = "execute @RepositoryTest for product-service";
        
        // When
        UserIntent intent = nlpEngine.parseIntent(input);
        
        // Then
        @SuppressWarnings("unchecked")
        Map<String, Object> params = (Map<String, Object>) intent.getParameters();
        assertTrue(params.containsKey("testTypes"));
        
        @SuppressWarnings("unchecked")
        var testTypes = (java.util.List<String>) params.get("testTypes");
        assertTrue(testTypes.contains("REPOSITORY_TEST"));
    }
    
    @Test
    @DisplayName("Should recognize @RestTest annotation")
    void shouldRecognizeRestTestAnnotation() {
        // Given
        String input = "run @RestTest for gateway-service";
        
        // When
        UserIntent intent = nlpEngine.parseIntent(input);
        
        // Then
        @SuppressWarnings("unchecked")
        Map<String, Object> params = (Map<String, Object>) intent.getParameters();
        assertTrue(params.containsKey("testTypes"));
        
        @SuppressWarnings("unchecked")
        var testTypes = (java.util.List<String>) params.get("testTypes");
        assertTrue(testTypes.contains("REST_TEST"));
    }
    
    @Test
    @DisplayName("Should recognize multiple annotation types")
    void shouldRecognizeMultipleAnnotationTypes() {
        // Given
        String input = "run @Test and @IntegrationTest for user-service";
        
        // When
        UserIntent intent = nlpEngine.parseIntent(input);
        
        // Then
        @SuppressWarnings("unchecked")
        Map<String, Object> params = (Map<String, Object>) intent.getParameters();
        assertTrue(params.containsKey("testTypes"));
        
        @SuppressWarnings("unchecked")
        var testTypes = (java.util.List<String>) params.get("testTypes");
        assertTrue(testTypes.contains("UNIT_TEST"));
        assertTrue(testTypes.contains("INTEGRATION_TEST"));
    }
    
    @Test
    @DisplayName("Should recognize priority with annotation")
    void shouldRecognizePriorityWithAnnotation() {
        // Given
        String input = "run P1 @Test for user-service";
        
        // When
        UserIntent intent = nlpEngine.parseIntent(input);
        
        // Then
        @SuppressWarnings("unchecked")
        Map<String, Object> params = (Map<String, Object>) intent.getParameters();
        assertEquals("P1", params.get("scope"));
        assertEquals("P1", params.get("priority"));
        
        @SuppressWarnings("unchecked")
        var testTypes = (java.util.List<String>) params.get("testTypes");
        assertTrue(testTypes.contains("UNIT_TEST"));
    }
    
    @Test
    @DisplayName("Should recognize priority 1 format")
    void shouldRecognizePriority1Format() {
        // Given
        String input = "run priority 1 tests for user-service";
        
        // When
        UserIntent intent = nlpEngine.parseIntent(input);
        
        // Then
        @SuppressWarnings("unchecked")
        Map<String, Object> params = (Map<String, Object>) intent.getParameters();
        assertEquals("P1", params.get("scope"));
        assertEquals("P1", params.get("priority"));
    }
    
    @Test
    @DisplayName("Should recognize priority 2 format")
    void shouldRecognizePriority2Format() {
        // Given
        String input = "execute priority 2 integration tests for product-service";
        
        // When
        UserIntent intent = nlpEngine.parseIntent(input);
        
        // Then
        @SuppressWarnings("unchecked")
        Map<String, Object> params = (Map<String, Object>) intent.getParameters();
        assertEquals("P2", params.get("scope"));
        assertEquals("P2", params.get("priority"));
        
        @SuppressWarnings("unchecked")
        var testTypes = (java.util.List<String>) params.get("testTypes");
        assertTrue(testTypes.contains("INTEGRATION_TEST"));
    }
    
    @Test
    @DisplayName("Should recognize case insensitive annotations")
    void shouldRecognizeCaseInsensitiveAnnotations() {
        // Given
        String input = "run @TEST and @INTEGRATIONTEST for user-service";
        
        // When
        UserIntent intent = nlpEngine.parseIntent(input);
        
        // Then
        @SuppressWarnings("unchecked")
        Map<String, Object> params = (Map<String, Object>) intent.getParameters();
        assertTrue(params.containsKey("testTypes"));
        
        @SuppressWarnings("unchecked")
        var testTypes = (java.util.List<String>) params.get("testTypes");
        assertTrue(testTypes.contains("UNIT_TEST"));
        assertTrue(testTypes.contains("INTEGRATION_TEST"));
    }
}
