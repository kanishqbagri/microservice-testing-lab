package com.kb.jarvis.core.demo;

import com.kb.jarvis.core.context.ComprehensiveContextService;
import com.kb.jarvis.core.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Comprehensive Context Demo
 * Demonstrates the Phase 1 implementation of comprehensive context understanding
 */
@Component
public class ComprehensiveContextDemo {
    
    private static final Logger log = LoggerFactory.getLogger(ComprehensiveContextDemo.class);
    
    @Autowired
    private ComprehensiveContextService comprehensiveContextService;
    
    /**
     * Run comprehensive demo showcasing all capabilities
     */
    public void runComprehensiveDemo() {
        log.info("🎯 Starting Comprehensive Context Understanding Demo");
        log.info("==================================================");
        
        // Demo 1: Simple single-service, single-test-type command
        demoSimpleCommand();
        
        // Demo 2: Multi-service, multi-test-type command
        demoMultiServiceMultiTest();
        
        // Demo 3: Complex command with dependencies
        demoComplexDependencyCommand();
        
        // Demo 4: Chaos testing with blast radius analysis
        demoChaosTestingCommand();
        
        // Demo 5: Performance testing with resource analysis
        demoPerformanceTestingCommand();
        
        // Demo 6: Security testing with risk assessment
        demoSecurityTestingCommand();
        
        log.info("🎉 Comprehensive Context Understanding Demo Completed!");
    }
    
    /**
     * Demo 1: Simple command
     */
    private void demoSimpleCommand() {
        log.info("\n📋 Demo 1: Simple Command Analysis");
        log.info("----------------------------------");
        
        String command = "Run unit tests for user service";
        log.info("Command: {}", command);
        
        ComprehensiveContext context = comprehensiveContextService.analyzeCommand(command);
        
        log.info("✅ Analysis Results:");
        log.info("  - Test Types: {}", context.getTestTypes());
        log.info("  - Services: {}", context.getServices());
        log.info("  - Actions: {}", context.getActions());
        log.info("  - Confidence: {:.2f}", context.getConfidence());
        log.info("  - Estimated Duration: {}", context.getEstimatedDuration());
        log.info("  - Risk Level: {}", context.getRiskAssessment().getOverallRiskLevel());
        log.info("  - Blast Radius: {}", context.getDependencies().getBlastRadius());
    }
    
    /**
     * Demo 2: Multi-service, multi-test-type command
     */
    private void demoMultiServiceMultiTest() {
        log.info("\n📋 Demo 2: Multi-Service, Multi-Test-Type Command");
        log.info("------------------------------------------------");
        
        String command = "Run unit and integration tests for user and order services, then analyze any failures";
        log.info("Command: {}", command);
        
        ComprehensiveContext context = comprehensiveContextService.analyzeCommand(command);
        
        log.info("✅ Analysis Results:");
        log.info("  - Test Types: {}", context.getTestTypes());
        log.info("  - Services: {}", context.getServices());
        log.info("  - Actions: {}", context.getActions());
        log.info("  - Confidence: {:.2f}", context.getConfidence());
        log.info("  - Execution Plan Steps: {}", context.getExecutionPlan().getSteps().size());
        log.info("  - Execution Strategy: {}", context.getExecutionPlan().getExecutionStrategy());
        log.info("  - Resource Requirements: CPU={}, Memory={}", 
                context.getResourceRequirements().getCpuRequirements(),
                context.getResourceRequirements().getMemoryRequirements());
        
        if (!context.getWarnings().isEmpty()) {
            log.info("  - Warnings: {}", context.getWarnings());
        }
        if (!context.getSuggestions().isEmpty()) {
            log.info("  - Suggestions: {}", context.getSuggestions());
        }
    }
    
    /**
     * Demo 3: Complex dependency command
     */
    private void demoComplexDependencyCommand() {
        log.info("\n📋 Demo 3: Complex Dependency Analysis");
        log.info("--------------------------------------");
        
        String command = "Run chaos tests on order service and monitor impact on dependent services";
        log.info("Command: {}", command);
        
        ComprehensiveContext context = comprehensiveContextService.analyzeCommand(command);
        
        log.info("✅ Analysis Results:");
        log.info("  - Test Types: {}", context.getTestTypes());
        log.info("  - Services: {}", context.getServices());
        log.info("  - Actions: {}", context.getActions());
        log.info("  - Affected Services: {}", context.getDependencies().getAffectedServices());
        log.info("  - Blast Radius: {}", context.getDependencies().getBlastRadius());
        log.info("  - Severity Level: {}", context.getDependencies().getSeverityLevel());
        log.info("  - Critical Path: {}", context.getDependencies().getCriticalPath());
        log.info("  - Isolation Points: {}", context.getDependencies().getIsolationPoints());
        log.info("  - Risk Factors: {}", context.getDependencies().getRiskFactors());
    }
    
    /**
     * Demo 4: Chaos testing command
     */
    private void demoChaosTestingCommand() {
        log.info("\n📋 Demo 4: Chaos Testing with Blast Radius Analysis");
        log.info("--------------------------------------------------");
        
        String command = "Execute chaos tests on gateway service with high intensity and monitor all dependent services";
        log.info("Command: {}", command);
        
        ComprehensiveContext context = comprehensiveContextService.analyzeCommand(command);
        
        log.info("✅ Analysis Results:");
        log.info("  - Test Types: {}", context.getTestTypes());
        log.info("  - Services: {}", context.getServices());
        log.info("  - Actions: {}", context.getActions());
        log.info("  - Blast Radius: {}", context.getDependencies().getBlastRadius());
        log.info("  - Severity Level: {}", context.getDependencies().getSeverityLevel());
        log.info("  - Risk Level: {}", context.getRiskAssessment().getOverallRiskLevel());
        log.info("  - Risk Factors: {}", context.getRiskAssessment().getRiskFactors());
        log.info("  - Mitigation Strategies: {}", context.getRiskAssessment().getMitigationStrategies());
        log.info("  - Warnings: {}", context.getWarnings());
    }
    
    /**
     * Demo 5: Performance testing command
     */
    private void demoPerformanceTestingCommand() {
        log.info("\n📋 Demo 5: Performance Testing with Resource Analysis");
        log.info("----------------------------------------------------");
        
        String command = "Run performance tests on all services with high load and parallel execution";
        log.info("Command: {}", command);
        
        ComprehensiveContext context = comprehensiveContextService.analyzeCommand(command);
        
        log.info("✅ Analysis Results:");
        log.info("  - Test Types: {}", context.getTestTypes());
        log.info("  - Services: {}", context.getServices());
        log.info("  - Actions: {}", context.getActions());
        log.info("  - Resource Requirements: CPU={}, Memory={}, Network={}", 
                context.getResourceRequirements().getCpuRequirements(),
                context.getResourceRequirements().getMemoryRequirements(),
                context.getResourceRequirements().getNetworkRequirements());
        log.info("  - External Dependencies: {}", context.getResourceRequirements().getExternalDependencies());
        log.info("  - Execution Strategy: {}", context.getExecutionPlan().getExecutionStrategy());
        log.info("  - Estimated Duration: {}", context.getEstimatedDuration());
    }
    
    /**
     * Demo 6: Security testing command
     */
    private void demoSecurityTestingCommand() {
        log.info("\n📋 Demo 6: Security Testing with Risk Assessment");
        log.info("-----------------------------------------------");
        
        String command = "Run security and penetration tests on user service and gateway service with comprehensive analysis";
        log.info("Command: {}", command);
        
        ComprehensiveContext context = comprehensiveContextService.analyzeCommand(command);
        
        log.info("✅ Analysis Results:");
        log.info("  - Test Types: {}", context.getTestTypes());
        log.info("  - Services: {}", context.getServices());
        log.info("  - Actions: {}", context.getActions());
        log.info("  - Risk Level: {}", context.getRiskAssessment().getOverallRiskLevel());
        log.info("  - Risk Factors: {}", context.getRiskAssessment().getRiskFactors());
        log.info("  - Risk Levels by Component: {}", context.getRiskAssessment().getRiskLevels());
        log.info("  - Mitigation Strategies: {}", context.getRiskAssessment().getMitigationStrategies());
        log.info("  - Impact Analysis: {}", context.getDependencies().getImpactAnalysis());
    }
    
    /**
     * Demo specific command patterns
     */
    public void demoCommandPatterns() {
        log.info("\n🎯 Command Pattern Analysis Demo");
        log.info("================================");
        
        List<String> commands = Arrays.asList(
            "Run unit tests for user service",
            "Execute integration tests for order and product services",
            "Run API tests on all services with parallel execution",
            "Perform chaos testing on gateway service with medium intensity",
            "Run performance tests on user service with high load",
            "Execute security tests on all services",
            "Generate unit tests for order service",
            "Analyze failures in product service",
            "Check health status of all services",
            "Run smoke tests on gateway and user services"
        );
        
        for (int i = 0; i < commands.size(); i++) {
            String command = commands.get(i);
            log.info("\n📋 Command {}: {}", i + 1, command);
            
            ComprehensiveContext context = comprehensiveContextService.analyzeCommand(command);
            
            log.info("  ✅ Parsed:");
            log.info("    - Test Types: {}", context.getTestTypes());
            log.info("    - Services: {}", context.getServices());
            log.info("    - Actions: {}", context.getActions());
            log.info("    - Confidence: {:.2f}", context.getConfidence());
            log.info("    - Risk Level: {}", context.getRiskAssessment().getOverallRiskLevel());
        }
    }
    
    /**
     * Demo error handling
     */
    public void demoErrorHandling() {
        log.info("\n🎯 Error Handling Demo");
        log.info("======================");
        
        List<String> errorCommands = Arrays.asList(
            "Run invalid test type for unknown service",
            "Execute tests with invalid parameters",
            "Run tests on non-existent service",
            ""
        );
        
        for (String command : errorCommands) {
            log.info("\n📋 Error Command: '{}'", command);
            
            ComprehensiveContext context = comprehensiveContextService.analyzeCommand(command);
            
            log.info("  ✅ Error Handling:");
            log.info("    - Confidence: {:.2f}", context.getConfidence());
            log.info("    - Warnings: {}", context.getWarnings());
            log.info("    - Fallback Actions: {}", context.getActions());
        }
    }
}
