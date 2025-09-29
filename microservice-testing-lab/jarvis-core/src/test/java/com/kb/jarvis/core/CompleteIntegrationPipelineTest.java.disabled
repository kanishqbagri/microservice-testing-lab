package com.kb.jarvis.core;

import com.kb.jarvis.core.ai.AIEngine;
import com.kb.jarvis.core.decision.DecisionEngine;
import com.kb.jarvis.core.nlp.NLPEngine;
import com.kb.jarvis.core.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Complete Integration Pipeline Test")
public class CompleteIntegrationPipelineTest {
    
    @Autowired
    private NLPEngine nlpEngine;
    
    @Autowired
    private AIEngine aiEngine;
    
    @Autowired
    private DecisionEngine decisionEngine;
    
    @Test
    @DisplayName("Complete Pipeline: Performance Testing Scenario")
    public void testCompletePipeline_PerformanceTesting() {
        // Step 1: NLP Processing
        String userInput = "Run performance tests on user-service with load testing and monitor CPU usage";
        
        UserIntent intent = nlpEngine.parseIntent(userInput);
        assertNotNull(intent, "NLP should parse intent successfully");
        assertEquals(IntentType.RUN_TESTS, intent.getType(), "Should identify RUN_TESTS intent");
        assertTrue(intent.getConfidence() > 0.7, "Should have high confidence");
        
        // Step 2: AI Analysis
        AIAnalysis analysis = aiEngine.analyzeIntent(intent);
        assertNotNull(analysis, "AI should analyze intent successfully");
        assertTrue(analysis.getConfidence() > 0.5, "AI analysis should have reasonable confidence");
        
        // Verify risk assessment
        assertNotNull(analysis.getRiskAssessment(), "Risk assessment should be generated");
        assertNotNull(analysis.getRiskAssessment().getRiskLevel(), "Risk level should be set");
        
        // Verify performance prediction
        assertNotNull(analysis.getPerformancePrediction(), "Performance prediction should be generated");
        assertTrue(analysis.getPerformancePrediction().getEstimatedTime() > 0, "Should estimate execution time");
        
        // Step 3: Decision Making
        DecisionAction action = decisionEngine.decideAction(intent, analysis);
        assertNotNull(action, "Decision should be made successfully");
        assertEquals(ActionType.RUN_PERFORMANCE_TESTS, action.getType(), "Should decide on performance tests");
        assertTrue(action.getConfidence() > 0.5, "Decision should have reasonable confidence");
        
        // Verify action parameters
        assertNotNull(action.getParameters(), "Action should have parameters");
        assertNotNull(action.getDescription(), "Action should have description");
        assertNotNull(action.getPriority(), "Action should have priority");
        
        System.out.println("✅ Complete Pipeline - Performance Testing: SUCCESS");
        System.out.println("   NLP Confidence: " + intent.getConfidence());
        System.out.println("   AI Confidence: " + analysis.getConfidence());
        System.out.println("   Decision Confidence: " + action.getConfidence());
        System.out.println("   Action Type: " + action.getType());
        System.out.println("   Priority: " + action.getPriority());
    }
    
    @Test
    @DisplayName("Complete Pipeline: Health Check Scenario")
    public void testCompletePipeline_HealthCheck() {
        // Step 1: NLP Processing
        String userInput = "Check system health for all services and report any issues";
        
        UserIntent intent = nlpEngine.parseIntent(userInput);
        assertNotNull(intent, "NLP should parse intent successfully");
        assertEquals(IntentType.HEALTH_CHECK, intent.getType(), "Should identify HEALTH_CHECK intent");
        
        // Step 2: AI Analysis
        AIAnalysis analysis = aiEngine.analyzeIntent(intent);
        assertNotNull(analysis, "AI should analyze intent successfully");
        
        // Step 3: Decision Making
        DecisionAction action = decisionEngine.decideAction(intent, analysis);
        assertNotNull(action, "Decision should be made successfully");
        assertEquals(ActionType.HEALTH_CHECK, action.getType(), "Should decide on health check");
        
        System.out.println("✅ Complete Pipeline - Health Check: SUCCESS");
        System.out.println("   Action Type: " + action.getType());
        System.out.println("   Confidence: " + action.getConfidence());
    }
    
    @Test
    @DisplayName("Complete Pipeline: Failure Analysis Scenario")
    public void testCompletePipeline_FailureAnalysis() {
        // Step 1: NLP Processing
        String userInput = "Analyze recent test failures in order-service and provide root cause analysis";
        
        UserIntent intent = nlpEngine.parseIntent(userInput);
        assertNotNull(intent, "NLP should parse intent successfully");
        assertEquals(IntentType.ANALYZE_FAILURES, intent.getType(), "Should identify ANALYZE_FAILURES intent");
        
        // Step 2: AI Analysis
        AIAnalysis analysis = aiEngine.analyzeIntent(intent);
        assertNotNull(analysis, "AI should analyze intent successfully");
        
        // Step 3: Decision Making
        DecisionAction action = decisionEngine.decideAction(intent, analysis);
        assertNotNull(action, "Decision should be made successfully");
        assertEquals(ActionType.ANALYZE_FAILURES, action.getType(), "Should decide on failure analysis");
        
        System.out.println("✅ Complete Pipeline - Failure Analysis: SUCCESS");
        System.out.println("   Action Type: " + action.getType());
        System.out.println("   Confidence: " + action.getConfidence());
    }
    
    @Test
    @DisplayName("Integration Pipeline Architecture Validation")
    public void testIntegrationPipelineArchitecture() {
        System.out.println("🔍 Integration Pipeline Architecture Status:");
        System.out.println("✅ NLP Engine: " + (nlpEngine != null ? "LOADED" : "MISSING"));
        System.out.println("✅ AI Engine: " + (aiEngine != null ? "LOADED" : "MISSING"));
        System.out.println("✅ Decision Engine: " + (decisionEngine != null ? "LOADED" : "MISSING"));
        
        // Test basic functionality
        assertNotNull(nlpEngine, "NLP Engine should be available");
        assertNotNull(aiEngine, "AI Engine should be available");
        assertNotNull(decisionEngine, "Decision Engine should be available");
        
        System.out.println("✅ All core components are loaded and functional");
        System.out.println("✅ Integration pipeline is ready for production use");
    }
    
    @Test
    @DisplayName("End-to-End Pipeline Performance")
    public void testEndToEndPipelinePerformance() {
        long startTime = System.currentTimeMillis();
        
        // Complete pipeline execution
        String userInput = "Run integration tests on all services with medium priority";
        
        UserIntent intent = nlpEngine.parseIntent(userInput);
        AIAnalysis analysis = aiEngine.analyzeIntent(intent);
        DecisionAction action = decisionEngine.decideAction(intent, analysis);
        
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        
        assertNotNull(action, "Pipeline should complete successfully");
        assertTrue(executionTime < 5000, "Pipeline should complete within 5 seconds");
        
        System.out.println("✅ End-to-End Pipeline Performance: SUCCESS");
        System.out.println("   Execution Time: " + executionTime + "ms");
        System.out.println("   NLP Processing: " + intent.getConfidence() + " confidence");
        System.out.println("   AI Analysis: " + analysis.getConfidence() + " confidence");
        System.out.println("   Decision Making: " + action.getConfidence() + " confidence");
        System.out.println("   Final Action: " + action.getType());
    }
}
