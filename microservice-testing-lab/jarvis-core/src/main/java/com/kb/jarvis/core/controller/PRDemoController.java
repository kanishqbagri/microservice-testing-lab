package com.kb.jarvis.core.controller;

import com.kb.jarvis.core.model.*;
import com.kb.jarvis.core.service.PRAnalysisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Demo Controller for PR Analysis
 * Provides sample PRs and demonstrates the analysis capabilities
 */
@RestController
@RequestMapping("/api/pr-demo")
@CrossOrigin(origins = "*")
public class PRDemoController {
    
    private static final Logger log = LoggerFactory.getLogger(PRDemoController.class);
    
    @Autowired
    private PRAnalysisService prAnalysisService;
    
    /**
     * Get sample PRs for demonstration
     * GET /api/pr-demo/sample-prs
     */
    @GetMapping("/sample-prs")
    public ResponseEntity<List<PRInfo>> getSamplePRs() {
        try {
            log.info("Generating sample PRs for demonstration");
            
            List<PRInfo> samplePRs = List.of(
                createSamplePR1(),
                createSamplePR2(),
                createSamplePR3()
            );
            
            return ResponseEntity.ok(samplePRs);
            
        } catch (Exception e) {
            log.error("Error generating sample PRs", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Analyze a sample PR
     * GET /api/pr-demo/analyze-sample/{prType}
     */
    @GetMapping("/analyze-sample/{prType}")
    public ResponseEntity<PRAnalysis> analyzeSamplePR(@PathVariable String prType) {
        try {
            log.info("Analyzing sample PR of type: {}", prType);
            
            PRInfo samplePR = getSamplePRByType(prType);
            if (samplePR == null) {
                return ResponseEntity.badRequest().build();
            }
            
            PRAnalysis analysis = prAnalysisService.analyzePR(samplePR);
            
            return ResponseEntity.ok(analysis);
            
        } catch (Exception e) {
            log.error("Error analyzing sample PR of type: {}", prType, e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Get comprehensive demo analysis
     * GET /api/pr-demo/comprehensive-demo
     */
    @GetMapping("/comprehensive-demo")
    public ResponseEntity<Map<String, Object>> getComprehensiveDemo() {
        try {
            log.info("Generating comprehensive PR analysis demo");
            
            // Analyze different types of PRs
            PRAnalysis securityPR = prAnalysisService.analyzePR(createSamplePR1());
            PRAnalysis breakingChangePR = prAnalysisService.analyzePR(createSamplePR2());
            PRAnalysis newFeaturePR = prAnalysisService.analyzePR(createSamplePR3());
            
            Map<String, Object> demo = Map.of(
                "securityPR", Map.of(
                    "analysis", securityPR,
                    "summary", Map.of(
                        "riskLevel", securityPR.getRiskAssessment().getOverallRiskLevel(),
                        "impactLevel", securityPR.getImpactAnalysis().getOverallImpact(),
                        "testRecommendations", securityPR.getTotalTestRecommendations(),
                        "securityConcerns", securityPR.getSecurityConcerns()
                    )
                ),
                "breakingChangePR", Map.of(
                    "analysis", breakingChangePR,
                    "summary", Map.of(
                        "riskLevel", breakingChangePR.getRiskAssessment().getOverallRiskLevel(),
                        "impactLevel", breakingChangePR.getImpactAnalysis().getOverallImpact(),
                        "testRecommendations", breakingChangePR.getTotalTestRecommendations(),
                        "breakingChanges", breakingChangePR.getImpactAnalysis().getBreakingChanges()
                    )
                ),
                "newFeaturePR", Map.of(
                    "analysis", newFeaturePR,
                    "summary", Map.of(
                        "riskLevel", newFeaturePR.getRiskAssessment().getOverallRiskLevel(),
                        "impactLevel", newFeaturePR.getImpactAnalysis().getOverallImpact(),
                        "testRecommendations", newFeaturePR.getTotalTestRecommendations(),
                        "newFeatures", newFeaturePR.getImpactAnalysis().getNewFeatures()
                    )
                ),
                "comparison", Map.of(
                    "highestRisk", getHighestRiskPR(securityPR, breakingChangePR, newFeaturePR),
                    "mostTestRecommendations", getMostTestRecommendations(securityPR, breakingChangePR, newFeaturePR),
                    "insights", List.of(
                        "Security changes require the highest priority testing",
                        "Breaking changes need comprehensive integration testing",
                        "New features should include end-to-end testing"
                    )
                )
            );
            
            return ResponseEntity.ok(demo);
            
        } catch (Exception e) {
            log.error("Error generating comprehensive demo", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Create sample PR 1 - Security changes
     */
    private PRInfo createSamplePR1() {
        return PRInfo.builder()
                .prId("PR-001")
                .title("Add JWT authentication and authorization")
                .description("Implement JWT-based authentication with role-based authorization for user service")
                .author("john.doe")
                .sourceBranch("feature/jwt-auth")
                .targetBranch("main")
                .createdAt(LocalDateTime.now().minusDays(2))
                .updatedAt(LocalDateTime.now().minusHours(1))
                .status("open")
                .reviewers(List.of("jane.smith", "bob.wilson"))
                .labels(List.of("security", "authentication", "user-service"))
                .repository("microservice-testing-lab")
                .organization("kb-org")
                .fileChanges(List.of(
                    FileChange.builder()
                        .filePath("user-service/src/main/java/com/kb/user/controller/AuthController.java")
                        .changeType(ChangeType.MODIFIED)
                        .linesAdded(45)
                        .linesDeleted(12)
                        .serviceName("user-service")
                        .componentName("authentication")
                        .keywords(List.of("security", "jwt", "auth"))
                        .complexity(ChangeComplexity.HIGH)
                        .build(),
                    FileChange.builder()
                        .filePath("user-service/src/main/java/com/kb/user/security/JwtUtil.java")
                        .changeType(ChangeType.ADDED)
                        .linesAdded(120)
                        .linesDeleted(0)
                        .serviceName("user-service")
                        .componentName("security")
                        .keywords(List.of("security", "jwt", "token"))
                        .complexity(ChangeComplexity.HIGH)
                        .build(),
                    FileChange.builder()
                        .filePath("user-service/src/main/resources/application.yml")
                        .changeType(ChangeType.MODIFIED)
                        .linesAdded(8)
                        .linesDeleted(2)
                        .serviceName("user-service")
                        .componentName("configuration")
                        .keywords(List.of("config", "jwt"))
                        .complexity(ChangeComplexity.LOW)
                        .build()
                ))
                .build();
    }
    
    /**
     * Create sample PR 2 - Breaking changes
     */
    private PRInfo createSamplePR2() {
        return PRInfo.builder()
                .prId("PR-002")
                .title("Refactor order service API - breaking changes")
                .description("Restructure order service API endpoints and data models for better consistency")
                .author("alice.johnson")
                .sourceBranch("feature/order-api-refactor")
                .targetBranch("main")
                .createdAt(LocalDateTime.now().minusDays(3))
                .updatedAt(LocalDateTime.now().minusHours(2))
                .status("open")
                .reviewers(List.of("charlie.brown", "diana.prince"))
                .labels(List.of("breaking-change", "api", "order-service"))
                .repository("microservice-testing-lab")
                .organization("kb-org")
                .fileChanges(List.of(
                    FileChange.builder()
                        .filePath("order-service/src/main/java/com/kb/order/controller/OrderController.java")
                        .changeType(ChangeType.MODIFIED)
                        .linesAdded(85)
                        .linesDeleted(120)
                        .serviceName("order-service")
                        .componentName("api")
                        .keywords(List.of("api", "breaking", "endpoint"))
                        .complexity(ChangeComplexity.CRITICAL)
                        .build(),
                    FileChange.builder()
                        .filePath("order-service/src/main/java/com/kb/order/model/Order.java")
                        .changeType(ChangeType.MODIFIED)
                        .linesAdded(25)
                        .linesDeleted(15)
                        .serviceName("order-service")
                        .componentName("model")
                        .keywords(List.of("model", "breaking", "field"))
                        .complexity(ChangeComplexity.HIGH)
                        .build(),
                    FileChange.builder()
                        .filePath("order-service/src/main/java/com/kb/order/dto/OrderRequest.java")
                        .changeType(ChangeType.DELETED)
                        .linesAdded(0)
                        .linesDeleted(45)
                        .serviceName("order-service")
                        .componentName("dto")
                        .keywords(List.of("dto", "breaking"))
                        .complexity(ChangeComplexity.MEDIUM)
                        .build()
                ))
                .build();
    }
    
    /**
     * Create sample PR 3 - New features
     */
    private PRInfo createSamplePR3() {
        return PRInfo.builder()
                .prId("PR-003")
                .title("Add product recommendation engine")
                .description("Implement AI-powered product recommendation system with caching and performance optimization")
                .author("mike.chen")
                .sourceBranch("feature/product-recommendations")
                .targetBranch("main")
                .createdAt(LocalDateTime.now().minusDays(1))
                .updatedAt(LocalDateTime.now().minusMinutes(30))
                .status("open")
                .reviewers(List.of("sarah.connor", "alex.murphy"))
                .labels(List.of("feature", "ai", "product-service", "performance"))
                .repository("microservice-testing-lab")
                .organization("kb-org")
                .fileChanges(List.of(
                    FileChange.builder()
                        .filePath("product-service/src/main/java/com/kb/product/service/RecommendationService.java")
                        .changeType(ChangeType.ADDED)
                        .linesAdded(200)
                        .linesDeleted(0)
                        .serviceName("product-service")
                        .componentName("recommendation")
                        .keywords(List.of("ai", "recommendation", "algorithm"))
                        .complexity(ChangeComplexity.HIGH)
                        .build(),
                    FileChange.builder()
                        .filePath("product-service/src/main/java/com/kb/product/controller/RecommendationController.java")
                        .changeType(ChangeType.ADDED)
                        .linesAdded(80)
                        .linesDeleted(0)
                        .serviceName("product-service")
                        .componentName("api")
                        .keywords(List.of("api", "recommendation", "endpoint"))
                        .complexity(ChangeComplexity.MEDIUM)
                        .build(),
                    FileChange.builder()
                        .filePath("product-service/src/main/java/com/kb/product/cache/RecommendationCache.java")
                        .changeType(ChangeType.ADDED)
                        .linesAdded(150)
                        .linesDeleted(0)
                        .serviceName("product-service")
                        .componentName("cache")
                        .keywords(List.of("cache", "performance", "redis"))
                        .complexity(ChangeComplexity.MEDIUM)
                        .build(),
                    FileChange.builder()
                        .filePath("product-service/src/main/resources/application.yml")
                        .changeType(ChangeType.MODIFIED)
                        .linesAdded(15)
                        .linesDeleted(3)
                        .serviceName("product-service")
                        .componentName("configuration")
                        .keywords(List.of("config", "cache", "redis"))
                        .complexity(ChangeComplexity.LOW)
                        .build()
                ))
                .build();
    }
    
    /**
     * Get sample PR by type
     */
    private PRInfo getSamplePRByType(String prType) {
        return switch (prType.toLowerCase()) {
            case "security" -> createSamplePR1();
            case "breaking" -> createSamplePR2();
            case "feature" -> createSamplePR3();
            default -> null;
        };
    }
    
    /**
     * Get the PR with highest risk
     */
    private String getHighestRiskPR(PRAnalysis pr1, PRAnalysis pr2, PRAnalysis pr3) {
        RiskLevel risk1 = pr1.getRiskAssessment().getOverallRiskLevel();
        RiskLevel risk2 = pr2.getRiskAssessment().getOverallRiskLevel();
        RiskLevel risk3 = pr3.getRiskAssessment().getOverallRiskLevel();
        
        if (risk1 == RiskLevel.HIGH || risk1 == RiskLevel.CRITICAL) return "Security PR";
        if (risk2 == RiskLevel.HIGH || risk2 == RiskLevel.CRITICAL) return "Breaking Change PR";
        if (risk3 == RiskLevel.HIGH || risk3 == RiskLevel.CRITICAL) return "New Feature PR";
        
        return "All PRs have similar risk levels";
    }
    
    /**
     * Get the PR with most test recommendations
     */
    private String getMostTestRecommendations(PRAnalysis pr1, PRAnalysis pr2, PRAnalysis pr3) {
        int count1 = pr1.getTotalTestRecommendations();
        int count2 = pr2.getTotalTestRecommendations();
        int count3 = pr3.getTotalTestRecommendations();
        
        if (count1 >= count2 && count1 >= count3) return "Security PR (" + count1 + " recommendations)";
        if (count2 >= count1 && count2 >= count3) return "Breaking Change PR (" + count2 + " recommendations)";
        return "New Feature PR (" + count3 + " recommendations)";
    }
}
