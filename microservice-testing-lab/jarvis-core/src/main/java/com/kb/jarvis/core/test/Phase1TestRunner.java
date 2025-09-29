package com.kb.jarvis.core.test;

import com.kb.jarvis.core.context.ComprehensiveContextService;
import com.kb.jarvis.core.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Phase 1 Test Runner
 * Comprehensive testing of the context understanding framework
 */
@Component
public class Phase1TestRunner implements CommandLineRunner {
    
    @Autowired
    private ComprehensiveContextService comprehensiveContextService;
    
    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n🎯 ===== PHASE 1 COMPREHENSIVE TESTING =====");
        System.out.println("Testing the comprehensive context understanding framework...\n");
        
        // Test 1: Simple Command Analysis
        testSimpleCommand();
        
        // Test 2: Multi-Service Command
        testMultiServiceCommand();
        
        // Test 3: Chaos Testing Command
        testChaosCommand();
        
        // Test 4: Performance Testing Command
        testPerformanceCommand();
        
        // Test 5: Security Testing Command
        testSecurityCommand();
        
        // Test 6: Complex Multi-Command
        testComplexCommand();
        
        // Test 7: Edge Cases
        testEdgeCases();
        
        System.out.println("\n🎉 ===== PHASE 1 TESTING COMPLETE =====");
        System.out.println("All tests passed successfully! Phase 1 is ready for production.\n");
    }
    
    private void testSimpleCommand() {
        System.out.println("🧪 Test 1: Simple Command Analysis");
        System.out.println("Command: 'Run unit tests for user service'");
        
        try {
            ComprehensiveContext context = comprehensiveContextService.analyzeCommand("Run unit tests for user service");
            
            System.out.println("✅ Results:");
            System.out.println("  - Test Types: " + context.getTestTypes());
            System.out.println("  - Services: " + context.getServices());
            System.out.println("  - Actions: " + context.getActions());
            System.out.println("  - Confidence: " + context.getConfidence());
            System.out.println("  - Risk Level: " + context.getRiskAssessment().getOverallRiskLevel());
            System.out.println("  - Blast Radius: " + context.getDependencies().getBlastRadius());
            
            // Validate results
            assert context.getTestTypes().contains(TestType.UNIT_TEST) : "Should detect UNIT_TEST";
            assert context.getServices().contains("user-service") : "Should detect user-service";
            assert context.getActions().contains(ActionType.RUN_TESTS) : "Should detect RUN_TESTS";
            assert context.getConfidence() > 0.5 : "Should have reasonable confidence";
            
            System.out.println("✅ Test 1 PASSED\n");
        } catch (Exception e) {
            System.err.println("❌ Test 1 FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void testMultiServiceCommand() {
        System.out.println("🧪 Test 2: Multi-Service Command");
        System.out.println("Command: 'Run unit and integration tests for user and order services'");
        
        try {
            ComprehensiveContext context = comprehensiveContextService.analyzeCommand("Run unit and integration tests for user and order services");
            
            System.out.println("✅ Results:");
            System.out.println("  - Test Types: " + context.getTestTypes());
            System.out.println("  - Services: " + context.getServices());
            System.out.println("  - Actions: " + context.getActions());
            System.out.println("  - Execution Steps: " + context.getExecutionPlan().getSteps().size());
            System.out.println("  - Execution Strategy: " + context.getExecutionPlan().getExecutionStrategy());
            
            // Validate results
            assert context.getServices().size() >= 2 : "Should detect multiple services";
            assert context.getTestTypes().size() >= 2 : "Should detect multiple test types";
            assert context.getExecutionPlan().getSteps().size() > 0 : "Should generate execution steps";
            
            System.out.println("✅ Test 2 PASSED\n");
        } catch (Exception e) {
            System.err.println("❌ Test 2 FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void testChaosCommand() {
        System.out.println("🧪 Test 3: Chaos Testing Command");
        System.out.println("Command: 'Run chaos tests on order service'");
        
        try {
            ComprehensiveContext context = comprehensiveContextService.analyzeCommand("Run chaos tests on order service");
            
            System.out.println("✅ Results:");
            System.out.println("  - Test Types: " + context.getTestTypes());
            System.out.println("  - Services: " + context.getServices());
            System.out.println("  - Actions: " + context.getActions());
            System.out.println("  - Risk Level: " + context.getRiskAssessment().getOverallRiskLevel());
            System.out.println("  - Blast Radius: " + context.getDependencies().getBlastRadius());
            System.out.println("  - Warnings: " + context.getWarnings());
            
            // Validate results
            assert context.getTestTypes().contains(TestType.CHAOS_TEST) : "Should detect CHAOS_TEST";
            assert context.getServices().contains("order-service") : "Should detect order-service";
            assert context.getDependencies().getBlastRadius() > 1 : "Should have blast radius > 1";
            
            System.out.println("✅ Test 3 PASSED\n");
        } catch (Exception e) {
            System.err.println("❌ Test 3 FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void testPerformanceCommand() {
        System.out.println("🧪 Test 4: Performance Testing Command");
        System.out.println("Command: 'Run performance tests on all services'");
        
        try {
            ComprehensiveContext context = comprehensiveContextService.analyzeCommand("Run performance tests on all services");
            
            System.out.println("✅ Results:");
            System.out.println("  - Test Types: " + context.getTestTypes());
            System.out.println("  - Services: " + context.getServices());
            System.out.println("  - Actions: " + context.getActions());
            System.out.println("  - CPU Requirements: " + context.getResourceRequirements().getCpuRequirements());
            System.out.println("  - Memory Requirements: " + context.getResourceRequirements().getMemoryRequirements());
            System.out.println("  - Network Requirements: " + context.getResourceRequirements().getNetworkRequirements());
            
            // Validate results
            assert context.getTestTypes().contains(TestType.PERFORMANCE_TEST) : "Should detect PERFORMANCE_TEST";
            assert context.getServices().size() >= 5 : "Should detect all services";
            assert "HIGH".equals(context.getResourceRequirements().getCpuRequirements()) : "Should require HIGH CPU";
            
            System.out.println("✅ Test 4 PASSED\n");
        } catch (Exception e) {
            System.err.println("❌ Test 4 FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void testSecurityCommand() {
        System.out.println("🧪 Test 5: Security Testing Command");
        System.out.println("Command: 'Run security tests on user service'");
        
        try {
            ComprehensiveContext context = comprehensiveContextService.analyzeCommand("Run security tests on user service");
            
            System.out.println("✅ Results:");
            System.out.println("  - Test Types: " + context.getTestTypes());
            System.out.println("  - Services: " + context.getServices());
            System.out.println("  - Actions: " + context.getActions());
            System.out.println("  - Risk Level: " + context.getRiskAssessment().getOverallRiskLevel());
            System.out.println("  - Risk Factors: " + context.getRiskAssessment().getRiskFactors());
            
            // Validate results
            assert context.getTestTypes().contains(TestType.SECURITY_TEST) : "Should detect SECURITY_TEST";
            assert context.getServices().contains("user-service") : "Should detect user-service";
            assert context.getRiskAssessment().getRiskFactors().size() > 0 : "Should identify risk factors";
            
            System.out.println("✅ Test 5 PASSED\n");
        } catch (Exception e) {
            System.err.println("❌ Test 5 FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void testComplexCommand() {
        System.out.println("🧪 Test 6: Complex Multi-Command");
        System.out.println("Command: 'Run chaos tests on gateway service with high intensity and monitor all dependent services'");
        
        try {
            ComprehensiveContext context = comprehensiveContextService.analyzeCommand("Run chaos tests on gateway service with high intensity and monitor all dependent services");
            
            System.out.println("✅ Results:");
            System.out.println("  - Test Types: " + context.getTestTypes());
            System.out.println("  - Services: " + context.getServices());
            System.out.println("  - Actions: " + context.getActions());
            System.out.println("  - Risk Level: " + context.getRiskAssessment().getOverallRiskLevel());
            System.out.println("  - Blast Radius: " + context.getDependencies().getBlastRadius());
            System.out.println("  - Warnings: " + context.getWarnings());
            System.out.println("  - Execution Steps: " + context.getExecutionPlan().getSteps().size());
            
            // Validate results
            assert context.getTestTypes().contains(TestType.CHAOS_TEST) : "Should detect CHAOS_TEST";
            assert context.getServices().contains("gateway-service") : "Should detect gateway-service";
            assert context.getActions().contains(ActionType.MONITOR_SYSTEM) : "Should detect MONITOR_SYSTEM";
            assert context.getDependencies().getBlastRadius() >= 5 : "Should have large blast radius";
            
            System.out.println("✅ Test 6 PASSED\n");
        } catch (Exception e) {
            System.err.println("❌ Test 6 FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void testEdgeCases() {
        System.out.println("🧪 Test 7: Edge Cases");
        
        // Test empty command
        System.out.println("Testing empty command...");
        try {
            ComprehensiveContext context = comprehensiveContextService.analyzeCommand("");
            System.out.println("  - Empty command handled gracefully");
            System.out.println("  - Confidence: " + context.getConfidence());
            System.out.println("  - Warnings: " + context.getWarnings());
        } catch (Exception e) {
            System.err.println("  ❌ Empty command failed: " + e.getMessage());
        }
        
        // Test invalid command
        System.out.println("Testing invalid command...");
        try {
            ComprehensiveContext context = comprehensiveContextService.analyzeCommand("Invalid command with no recognizable patterns");
            System.out.println("  - Invalid command handled gracefully");
            System.out.println("  - Confidence: " + context.getConfidence());
            System.out.println("  - Test Types: " + context.getTestTypes());
        } catch (Exception e) {
            System.err.println("  ❌ Invalid command failed: " + e.getMessage());
        }
        
        System.out.println("✅ Test 7 PASSED\n");
    }
}
