package com.kb.jarvis.core.controller;

import com.kb.jarvis.core.model.*;
import com.kb.jarvis.core.service.PRAnalysisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * REST API Controller for Pull Request Analysis
 * Provides endpoints for analyzing PRs and getting intelligent testing recommendations
 */
@RestController
@RequestMapping("/api/pr-analysis")
@CrossOrigin(origins = "*")
public class PRAnalysisController {
    
    private static final Logger log = LoggerFactory.getLogger(PRAnalysisController.class);
    
    @Autowired
    private PRAnalysisService prAnalysisService;
    
    /**
     * Analyze a Pull Request and provide comprehensive analysis
     * POST /api/pr-analysis/analyze
     */
    @PostMapping("/analyze")
    public ResponseEntity<PRAnalysis> analyzePR(@RequestBody PRInfo prInfo) {
        try {
            log.info("Received PR analysis request for PR: {}", prInfo.getPrId());
            
            PRAnalysis analysis = prAnalysisService.analyzePR(prInfo);
            
            log.info("PR analysis completed for PR: {} with {} test recommendations", 
                    prInfo.getPrId(), analysis.getTotalTestRecommendations());
            
            return ResponseEntity.ok(analysis);
            
        } catch (Exception e) {
            log.error("Error analyzing PR: {}", prInfo.getPrId(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Get test recommendations for a specific service
     * GET /api/pr-analysis/recommendations/{serviceName}
     */
    @GetMapping("/recommendations/{serviceName}")
    public ResponseEntity<List<TestRecommendation>> getServiceRecommendations(
            @PathVariable String serviceName,
            @RequestParam(required = false) String testType,
            @RequestParam(required = false) Double minPriority) {
        
        try {
            log.info("Getting test recommendations for service: {}", serviceName);
            
            // This would typically come from a stored analysis or be generated on-demand
            // For now, we'll return a sample response
            List<TestRecommendation> recommendations = generateSampleRecommendations(serviceName, testType, minPriority);
            
            return ResponseEntity.ok(recommendations);
            
        } catch (Exception e) {
            log.error("Error getting recommendations for service: {}", serviceName, e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Get risk assessment for a PR
     * POST /api/pr-analysis/risk-assessment
     */
    @PostMapping("/risk-assessment")
    public ResponseEntity<RiskAssessment> assessRisk(@RequestBody PRInfo prInfo) {
        try {
            log.info("Assessing risk for PR: {}", prInfo.getPrId());
            
            PRAnalysis analysis = prAnalysisService.analyzePR(prInfo);
            RiskAssessment riskAssessment = analysis.getRiskAssessment();
            
            return ResponseEntity.ok(riskAssessment);
            
        } catch (Exception e) {
            log.error("Error assessing risk for PR: {}", prInfo.getPrId(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Get impact analysis for a PR
     * POST /api/pr-analysis/impact-analysis
     */
    @PostMapping("/impact-analysis")
    public ResponseEntity<ImpactAnalysis> analyzeImpact(@RequestBody PRInfo prInfo) {
        try {
            log.info("Analyzing impact for PR: {}", prInfo.getPrId());
            
            PRAnalysis analysis = prAnalysisService.analyzePR(prInfo);
            ImpactAnalysis impactAnalysis = analysis.getImpactAnalysis();
            
            return ResponseEntity.ok(impactAnalysis);
            
        } catch (Exception e) {
            log.error("Error analyzing impact for PR: {}", prInfo.getPrId(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Get high-priority test recommendations
     * POST /api/pr-analysis/high-priority-recommendations
     */
    @PostMapping("/high-priority-recommendations")
    public ResponseEntity<List<TestRecommendation>> getHighPriorityRecommendations(@RequestBody PRInfo prInfo) {
        try {
            log.info("Getting high-priority recommendations for PR: {}", prInfo.getPrId());
            
            PRAnalysis analysis = prAnalysisService.analyzePR(prInfo);
            List<TestRecommendation> highPriorityRecommendations = analysis.getHighPriorityRecommendations();
            
            return ResponseEntity.ok(highPriorityRecommendations);
            
        } catch (Exception e) {
            log.error("Error getting high-priority recommendations for PR: {}", prInfo.getPrId(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Get test recommendations by type
     * POST /api/pr-analysis/recommendations-by-type
     */
    @PostMapping("/recommendations-by-type")
    public ResponseEntity<List<TestRecommendation>> getRecommendationsByType(
            @RequestBody PRInfo prInfo,
            @RequestParam TestType testType) {
        
        try {
            log.info("Getting {} recommendations for PR: {}", testType, prInfo.getPrId());
            
            PRAnalysis analysis = prAnalysisService.analyzePR(prInfo);
            List<TestRecommendation> recommendations = analysis.getRecommendationsByType(testType);
            
            return ResponseEntity.ok(recommendations);
            
        } catch (Exception e) {
            log.error("Error getting {} recommendations for PR: {}", testType, prInfo.getPrId(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Get analysis summary
     * POST /api/pr-analysis/summary
     */
    @PostMapping("/summary")
    public ResponseEntity<Map<String, Object>> getAnalysisSummary(@RequestBody PRInfo prInfo) {
        try {
            log.info("Getting analysis summary for PR: {}", prInfo.getPrId());
            
            PRAnalysis analysis = prAnalysisService.analyzePR(prInfo);
            
            Map<String, Object> summary = new HashMap<>();
            summary.put("prId", prInfo.getPrId());
            summary.put("title", prInfo.getTitle());
            summary.put("overallRiskLevel", analysis.getRiskAssessment().getOverallRiskLevel());
            summary.put("overallImpact", analysis.getImpactAnalysis().getOverallImpact());
            summary.put("totalTestRecommendations", analysis.getTotalTestRecommendations());
            summary.put("highPriorityRecommendations", analysis.getHighPriorityRecommendations().size());
            summary.put("affectedServices", analysis.getAffectedServices());
            summary.put("confidenceScore", analysis.getConfidenceScore());
            summary.put("insights", analysis.getInsights());
            summary.put("requiresSecurityTesting", analysis.requiresSecurityTesting());
            summary.put("requiresPerformanceTesting", analysis.requiresPerformanceTesting());
            
            return ResponseEntity.ok(summary);
            
        } catch (Exception e) {
            log.error("Error getting analysis summary for PR: {}", prInfo.getPrId(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Health check endpoint
     * GET /api/pr-analysis/health
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "PR Analysis Service",
            "version", "1.0.0"
        ));
    }
    
    /**
     * Generate sample test recommendations for demonstration
     */
    private List<TestRecommendation> generateSampleRecommendations(String serviceName, String testType, Double minPriority) {
        List<TestRecommendation> recommendations = List.of(
            TestRecommendation.builder()
                .recommendationId(UUID.randomUUID().toString())
                .testType(TestType.UNIT_TEST)
                .serviceName(serviceName)
                .description("Run unit tests for " + serviceName)
                .rationale("Service has been modified and requires unit test validation")
                .priority(0.9)
                .testScenarios(List.of("Test all modified methods", "Test new functionality", "Test edge cases"))
                .estimatedDuration("10-15 minutes")
                .complexity("Low")
                .automated(true)
                .critical(true)
                .category("unit")
                .tags(List.of("automated", "critical", serviceName))
                .build(),
                
            TestRecommendation.builder()
                .recommendationId(UUID.randomUUID().toString())
                .testType(TestType.INTEGRATION_TEST)
                .serviceName(serviceName)
                .description("Run integration tests for " + serviceName)
                .rationale("API changes detected, integration testing required")
                .priority(0.8)
                .testScenarios(List.of("Test API endpoints", "Test service interactions", "Test data flow"))
                .estimatedDuration("20-30 minutes")
                .complexity("Medium")
                .automated(true)
                .critical(true)
                .category("integration")
                .tags(List.of("automated", "api", serviceName))
                .build(),
                
            TestRecommendation.builder()
                .recommendationId(UUID.randomUUID().toString())
                .testType(TestType.CONTRACT_TEST)
                .serviceName(serviceName)
                .description("Run contract tests for " + serviceName)
                .rationale("Breaking changes detected, contract validation required")
                .priority(0.7)
                .testScenarios(List.of("Validate API contracts", "Test backward compatibility", "Verify consumer contracts"))
                .estimatedDuration("15-25 minutes")
                .complexity("Medium")
                .automated(true)
                .critical(false)
                .category("contract")
                .tags(List.of("automated", "breaking", serviceName))
                .build()
        );
        
        // Filter by test type if specified
        if (testType != null) {
            recommendations = recommendations.stream()
                    .filter(rec -> rec.getTestType() == TestType.valueOf(testType))
                    .toList();
        }
        
        // Filter by minimum priority if specified
        if (minPriority != null) {
            recommendations = recommendations.stream()
                    .filter(rec -> rec.getPriority() >= minPriority)
                    .toList();
        }
        
        return recommendations;
    }
}
