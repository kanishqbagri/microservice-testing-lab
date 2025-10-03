package com.kb.jarvis.core.service;

import com.kb.jarvis.core.model.*;
import com.kb.jarvis.core.ai.PRAnalysisAI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Service for analyzing Pull Requests and providing intelligent testing recommendations
 */
@Service
public class PRAnalysisService {
    
    private static final Logger log = LoggerFactory.getLogger(PRAnalysisService.class);
    
    @Autowired
    private PRAnalysisAI prAnalysisAI;
    
    // Service patterns for identifying affected services
    private static final Map<String, String> SERVICE_PATTERNS = Map.of(
        "user-service", "user|auth|login|profile|account",
        "order-service", "order|payment|transaction|checkout|cart",
        "product-service", "product|catalog|inventory|item",
        "notification-service", "notification|email|sms|alert|message",
        "gateway-service", "gateway|proxy|routing|api-gateway"
    );
    
    // Risk factors and their weights
    private static final Map<String, Double> RISK_FACTORS = Map.of(
        "breaking_changes", 0.9,
        "security_changes", 0.8,
        "database_changes", 0.7,
        "config_changes", 0.6,
        "large_changes", 0.5,
        "new_features", 0.4,
        "bug_fixes", 0.3,
        "refactoring", 0.2
    );
    
    /**
     * Analyze a Pull Request and provide comprehensive analysis
     */
    public PRAnalysis analyzePR(PRInfo prInfo) {
        log.info("Starting PR analysis for PR: {}", prInfo.getPrId());
        
        String analysisId = UUID.randomUUID().toString();
        
        // Parse PR information
        PRInfo parsedPR = parsePRInfo(prInfo);
        
        // Perform impact analysis
        ImpactAnalysis impactAnalysis = performImpactAnalysis(parsedPR);
        
        // Calculate risk assessment
        RiskAssessment riskAssessment = calculateRiskAssessment(parsedPR, impactAnalysis);
        
        // Generate test recommendations
        List<TestRecommendation> testRecommendations = generateTestRecommendations(parsedPR, impactAnalysis, riskAssessment);
        
        // Perform AI analysis for enhanced insights
        PRAnalysisAI.AIInsights aiInsights = prAnalysisAI.analyzeWithAI(parsedPR, parsedPR.getFileChanges());
        
        // Calculate confidence score (enhanced with AI)
        double confidenceScore = Math.max(calculateConfidenceScore(parsedPR, impactAnalysis, riskAssessment), 
                                         aiInsights.getAiConfidence());
        
        // Build comprehensive analysis
        PRAnalysis analysis = PRAnalysis.builder()
                .analysisId(analysisId)
                .prInfo(parsedPR)
                .impactAnalysis(impactAnalysis)
                .riskAssessment(riskAssessment)
                .testRecommendations(testRecommendations)
                .affectedServices(impactAnalysis.getAffectedServices())
                .affectedComponents(impactAnalysis.getAffectedComponents())
                .criticalPaths(identifyCriticalPaths(parsedPR, impactAnalysis))
                .potentialIssues(identifyPotentialIssues(parsedPR, impactAnalysis))
                .securityConcerns(identifySecurityConcerns(parsedPR))
                .performanceImplications(identifyPerformanceImplications(parsedPR))
                .metrics(calculateMetrics(parsedPR, impactAnalysis))
                .confidenceScore(confidenceScore)
                .analysisVersion("1.0")
                .analyzedAt(LocalDateTime.now())
                .analyzedBy("Jarvis AI")
                .insights(aiInsights.getInsights()) // Use AI-generated insights
                .metadata(createMetadata(parsedPR))
                .build();
        
        log.info("PR analysis completed for PR: {} with {} test recommendations", 
                prInfo.getPrId(), testRecommendations.size());
        
        return analysis;
    }
    
    /**
     * Parse and enrich PR information
     */
    private PRInfo parsePRInfo(PRInfo prInfo) {
        log.debug("Parsing PR info for PR: {}", prInfo.getPrId());
        
        // Analyze file changes to extract additional information
        if (prInfo.getFileChanges() != null) {
            for (FileChange fileChange : prInfo.getFileChanges()) {
                // Determine service name from file path
                String serviceName = determineServiceFromPath(fileChange.getFilePath());
                fileChange.setServiceName(serviceName);
                
                // Analyze change complexity
                ChangeComplexity complexity = analyzeChangeComplexity(fileChange);
                fileChange.setComplexity(complexity);
                
                // Extract keywords from changes
                List<String> keywords = extractKeywords(fileChange);
                fileChange.setKeywords(keywords);
            }
        }
        
        // Calculate summary statistics
        prInfo.setLinesAdded(calculateTotalLinesAdded(prInfo.getFileChanges()));
        prInfo.setLinesDeleted(calculateTotalLinesDeleted(prInfo.getFileChanges()));
        prInfo.setFilesChanged(prInfo.getFileChanges() != null ? prInfo.getFileChanges().size() : 0);
        
        // Identify affected services
        List<String> affectedServices = identifyAffectedServices(prInfo.getFileChanges());
        prInfo.setAffectedServices(affectedServices);
        
        return prInfo;
    }
    
    /**
     * Perform impact analysis on the PR
     */
    private ImpactAnalysis performImpactAnalysis(PRInfo prInfo) {
        log.debug("Performing impact analysis for PR: {}", prInfo.getPrId());
        
        List<String> affectedServices = prInfo.getAffectedServices();
        List<String> affectedComponents = new ArrayList<>();
        List<String> affectedModules = new ArrayList<>();
        List<String> affectedEndpoints = new ArrayList<>();
        List<String> affectedDatabases = new ArrayList<>();
        List<String> affectedConfigurations = new ArrayList<>();
        List<String> dependencies = new ArrayList<>();
        List<String> breakingChanges = new ArrayList<>();
        List<String> newFeatures = new ArrayList<>();
        List<String> bugFixes = new ArrayList<>();
        List<String> refactoring = new ArrayList<>();
        List<String> performanceChanges = new ArrayList<>();
        List<String> securityChanges = new ArrayList<>();
        
        // Analyze each file change
        if (prInfo.getFileChanges() != null) {
            for (FileChange fileChange : prInfo.getFileChanges()) {
                // Identify affected components
                if (fileChange.getComponentName() != null) {
                    affectedComponents.add(fileChange.getComponentName());
                }
                
                // Identify affected modules
                String module = extractModuleFromPath(fileChange.getFilePath());
                if (module != null) {
                    affectedModules.add(module);
                }
                
                // Identify affected endpoints (for API changes)
                if (fileChange.isJavaFile() && fileChange.getAddedLines() != null) {
                    List<String> endpoints = extractEndpoints(fileChange.getAddedLines());
                    affectedEndpoints.addAll(endpoints);
                }
                
                // Identify database changes
                if (isDatabaseChange(fileChange)) {
                    affectedDatabases.add(fileChange.getFileName());
                }
                
                // Identify configuration changes
                if (fileChange.isConfigFile()) {
                    affectedConfigurations.add(fileChange.getFileName());
                }
                
                // Categorize changes
                categorizeChanges(fileChange, breakingChanges, newFeatures, bugFixes, 
                                refactoring, performanceChanges, securityChanges);
            }
        }
        
        // Calculate overall impact level
        ImpactLevel overallImpact = calculateOverallImpact(affectedServices, breakingChanges, 
                                                          newFeatures, securityChanges);
        
        // Create service impact map
        Map<String, ImpactLevel> serviceImpacts = new HashMap<>();
        for (String service : affectedServices) {
            serviceImpacts.put(service, calculateServiceImpact(service, prInfo.getFileChanges()));
        }
        
        return ImpactAnalysis.builder()
                .overallImpact(overallImpact)
                .affectedServices(affectedServices)
                .affectedComponents(affectedComponents.stream().distinct().collect(Collectors.toList()))
                .affectedModules(affectedModules.stream().distinct().collect(Collectors.toList()))
                .affectedEndpoints(affectedEndpoints.stream().distinct().collect(Collectors.toList()))
                .affectedDatabases(affectedDatabases.stream().distinct().collect(Collectors.toList()))
                .affectedConfigurations(affectedConfigurations.stream().distinct().collect(Collectors.toList()))
                .dependencies(dependencies)
                .breakingChanges(breakingChanges)
                .newFeatures(newFeatures)
                .bugFixes(bugFixes)
                .refactoring(refactoring)
                .performanceChanges(performanceChanges)
                .securityChanges(securityChanges)
                .serviceImpacts(serviceImpacts)
                .summary(generateImpactSummary(overallImpact, affectedServices, breakingChanges))
                .build();
    }
    
    /**
     * Calculate risk assessment for the PR
     */
    private RiskAssessment calculateRiskAssessment(PRInfo prInfo, ImpactAnalysis impactAnalysis) {
        log.debug("Calculating risk assessment for PR: {}", prInfo.getPrId());
        
        List<String> riskFactors = new ArrayList<>();
        Map<String, RiskLevel> riskLevels = new HashMap<>();
        List<String> mitigationStrategies = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        
        double riskScore = 0.0;
        
        // Analyze various risk factors
        if (impactAnalysis.hasBreakingChanges()) {
            riskFactors.add("Breaking changes detected");
            riskScore += RISK_FACTORS.get("breaking_changes");
            warnings.add("This PR contains breaking changes that may affect dependent services");
            mitigationStrategies.add("Run comprehensive integration tests");
            mitigationStrategies.add("Update API documentation");
        }
        
        if (impactAnalysis.hasSecurityChanges()) {
            riskFactors.add("Security-related changes detected");
            riskScore += RISK_FACTORS.get("security_changes");
            warnings.add("Security changes require thorough security testing");
            mitigationStrategies.add("Run security tests and penetration testing");
            mitigationStrategies.add("Review security implications with security team");
        }
        
        if (!impactAnalysis.getAffectedDatabases().isEmpty()) {
            riskFactors.add("Database changes detected");
            riskScore += RISK_FACTORS.get("database_changes");
            warnings.add("Database changes require careful testing and migration planning");
            mitigationStrategies.add("Test database migrations in staging environment");
            mitigationStrategies.add("Create database backup before deployment");
        }
        
        if (!impactAnalysis.getAffectedConfigurations().isEmpty()) {
            riskFactors.add("Configuration changes detected");
            riskScore += RISK_FACTORS.get("config_changes");
            warnings.add("Configuration changes may affect system behavior");
            mitigationStrategies.add("Test configuration changes in isolated environment");
        }
        
        if (prInfo.isLargePR()) {
            riskFactors.add("Large PR with significant changes");
            riskScore += RISK_FACTORS.get("large_changes");
            warnings.add("Large PR increases risk of introducing bugs");
            mitigationStrategies.add("Consider breaking down into smaller PRs");
            mitigationStrategies.add("Increase code review coverage");
        }
        
        if (impactAnalysis.hasNewFeatures()) {
            riskFactors.add("New features introduced");
            riskScore += RISK_FACTORS.get("new_features");
            mitigationStrategies.add("Test new features thoroughly");
            mitigationStrategies.add("Update user documentation");
        }
        
        // Calculate overall risk level
        RiskLevel overallRiskLevel = calculateRiskLevel(riskScore);
        
        // Set risk levels for different aspects
        riskLevels.put("overall", overallRiskLevel);
        riskLevels.put("breaking_changes", impactAnalysis.hasBreakingChanges() ? RiskLevel.HIGH : RiskLevel.LOW);
        riskLevels.put("security", impactAnalysis.hasSecurityChanges() ? RiskLevel.HIGH : RiskLevel.LOW);
        riskLevels.put("database", !impactAnalysis.getAffectedDatabases().isEmpty() ? RiskLevel.MEDIUM : RiskLevel.LOW);
        riskLevels.put("configuration", !impactAnalysis.getAffectedConfigurations().isEmpty() ? RiskLevel.MEDIUM : RiskLevel.LOW);
        
        return RiskAssessment.builder()
                .overallRiskLevel(overallRiskLevel)
                .riskFactors(riskFactors)
                .riskLevels(riskLevels)
                .mitigationStrategies(mitigationStrategies)
                .warnings(warnings)
                .confidence("HIGH")
                .riskMetrics(Map.of(
                    "risk_score", riskScore,
                    "total_factors", riskFactors.size(),
                    "high_risk_factors", riskFactors.stream().filter(f -> f.contains("breaking") || f.contains("security")).count()
                ))
                .build();
    }
    
    /**
     * Generate test recommendations based on PR analysis
     */
    private List<TestRecommendation> generateTestRecommendations(PRInfo prInfo, ImpactAnalysis impactAnalysis, RiskAssessment riskAssessment) {
        log.debug("Generating test recommendations for PR: {}", prInfo.getPrId());
        
        List<TestRecommendation> recommendations = new ArrayList<>();
        
        // Generate recommendations for each affected service
        for (String service : impactAnalysis.getAffectedServices()) {
            recommendations.addAll(generateServiceTestRecommendations(service, prInfo, impactAnalysis, riskAssessment));
        }
        
        // Generate recommendations based on change types
        recommendations.addAll(generateChangeTypeRecommendations(prInfo, impactAnalysis, riskAssessment));
        
        // Generate recommendations based on risk factors
        recommendations.addAll(generateRiskBasedRecommendations(prInfo, impactAnalysis, riskAssessment));
        
        // Sort by priority
        recommendations.sort((a, b) -> Double.compare(b.getPriority(), a.getPriority()));
        
        return recommendations;
    }
    
    /**
     * Generate test recommendations for a specific service
     */
    private List<TestRecommendation> generateServiceTestRecommendations(String serviceName, PRInfo prInfo, ImpactAnalysis impactAnalysis, RiskAssessment riskAssessment) {
        List<TestRecommendation> recommendations = new ArrayList<>();
        
        // Unit tests - always recommended for affected services
        recommendations.add(TestRecommendation.builder()
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
                .build());
        
        // Integration tests - recommended for services with API changes
        if (hasAPIChanges(serviceName, prInfo.getFileChanges())) {
            recommendations.add(TestRecommendation.builder()
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
                    .build());
        }
        
        // Contract tests - recommended for services with breaking changes
        if (impactAnalysis.hasBreakingChanges() && impactAnalysis.affectsService(serviceName)) {
            recommendations.add(TestRecommendation.builder()
                    .recommendationId(UUID.randomUUID().toString())
                    .testType(TestType.CONTRACT_TEST)
                    .serviceName(serviceName)
                    .description("Run contract tests for " + serviceName)
                    .rationale("Breaking changes detected, contract validation required")
                    .priority(0.9)
                    .testScenarios(List.of("Validate API contracts", "Test backward compatibility", "Verify consumer contracts"))
                    .estimatedDuration("15-25 minutes")
                    .complexity("Medium")
                    .automated(true)
                    .critical(true)
                    .category("contract")
                    .tags(List.of("automated", "breaking", serviceName))
                    .build());
        }
        
        return recommendations;
    }
    
    /**
     * Generate recommendations based on change types
     */
    private List<TestRecommendation> generateChangeTypeRecommendations(PRInfo prInfo, ImpactAnalysis impactAnalysis, RiskAssessment riskAssessment) {
        List<TestRecommendation> recommendations = new ArrayList<>();
        
        // Security tests - for security changes
        if (impactAnalysis.hasSecurityChanges()) {
            recommendations.add(TestRecommendation.builder()
                    .recommendationId(UUID.randomUUID().toString())
                    .testType(TestType.SECURITY_TEST)
                    .description("Run security tests")
                    .rationale("Security-related changes detected")
                    .priority(0.95)
                    .testScenarios(List.of("Vulnerability scanning", "Authentication testing", "Authorization testing"))
                    .estimatedDuration("30-45 minutes")
                    .complexity("High")
                    .automated(false)
                    .critical(true)
                    .category("security")
                    .tags(List.of("security", "manual", "critical"))
                    .build());
        }
        
        // Performance tests - for performance-related changes
        if (impactAnalysis.hasPerformanceChanges()) {
            recommendations.add(TestRecommendation.builder()
                    .recommendationId(UUID.randomUUID().toString())
                    .testType(TestType.PERFORMANCE_TEST)
                    .description("Run performance tests")
                    .rationale("Performance-related changes detected")
                    .priority(0.7)
                    .testScenarios(List.of("Load testing", "Stress testing", "Performance benchmarking"))
                    .estimatedDuration("45-60 minutes")
                    .complexity("High")
                    .automated(true)
                    .critical(false)
                    .category("performance")
                    .tags(List.of("performance", "automated"))
                    .build());
        }
        
        return recommendations;
    }
    
    /**
     * Generate recommendations based on risk factors
     */
    private List<TestRecommendation> generateRiskBasedRecommendations(PRInfo prInfo, ImpactAnalysis impactAnalysis, RiskAssessment riskAssessment) {
        List<TestRecommendation> recommendations = new ArrayList<>();
        
        // End-to-end tests - for high-risk changes
        if (riskAssessment.getOverallRiskLevel() == RiskLevel.HIGH) {
            recommendations.add(TestRecommendation.builder()
                    .recommendationId(UUID.randomUUID().toString())
                    .testType(TestType.END_TO_END_TEST)
                    .description("Run end-to-end tests")
                    .rationale("High-risk changes require comprehensive testing")
                    .priority(0.8)
                    .testScenarios(List.of("Complete user workflows", "Cross-service interactions", "System integration"))
                    .estimatedDuration("60-90 minutes")
                    .complexity("High")
                    .automated(true)
                    .critical(true)
                    .category("e2e")
                    .tags(List.of("e2e", "automated", "high-risk"))
                    .build());
        }
        
        // Chaos tests - for critical services
        if (impactAnalysis.getAffectedServices().stream().anyMatch(s -> s.contains("gateway") || s.contains("user"))) {
            recommendations.add(TestRecommendation.builder()
                    .recommendationId(UUID.randomUUID().toString())
                    .testType(TestType.CHAOS_TEST)
                    .description("Run chaos tests for critical services")
                    .rationale("Critical services require resilience testing")
                    .priority(0.6)
                    .testScenarios(List.of("Service failure simulation", "Network latency testing", "Resource exhaustion"))
                    .estimatedDuration("30-45 minutes")
                    .complexity("High")
                    .automated(true)
                    .critical(false)
                    .category("chaos")
                    .tags(List.of("chaos", "resilience", "critical-services"))
                    .build());
        }
        
        return recommendations;
    }
    
    // Helper methods for analysis
    
    private String determineServiceFromPath(String filePath) {
        if (filePath == null) return null;
        
        for (Map.Entry<String, String> entry : SERVICE_PATTERNS.entrySet()) {
            if (filePath.contains(entry.getKey().replace("-service", ""))) {
                return entry.getKey();
            }
        }
        return null;
    }
    
    private ChangeComplexity analyzeChangeComplexity(FileChange fileChange) {
        int totalChanges = fileChange.getTotalLinesChanged();
        
        if (totalChanges > 200) return ChangeComplexity.CRITICAL;
        if (totalChanges > 100) return ChangeComplexity.HIGH;
        if (totalChanges > 50) return ChangeComplexity.MEDIUM;
        return ChangeComplexity.LOW;
    }
    
    private List<String> extractKeywords(FileChange fileChange) {
        List<String> keywords = new ArrayList<>();
        
        if (fileChange.getAddedLines() != null) {
            for (String line : fileChange.getAddedLines()) {
                if (line.contains("@RestController") || line.contains("@Controller")) {
                    keywords.add("api");
                }
                if (line.contains("@Service")) {
                    keywords.add("service");
                }
                if (line.contains("@Repository")) {
                    keywords.add("database");
                }
                if (line.contains("security") || line.contains("auth")) {
                    keywords.add("security");
                }
                if (line.contains("performance") || line.contains("cache")) {
                    keywords.add("performance");
                }
            }
        }
        
        return keywords.stream().distinct().collect(Collectors.toList());
    }
    
    private int calculateTotalLinesAdded(List<FileChange> fileChanges) {
        if (fileChanges == null) return 0;
        return fileChanges.stream().mapToInt(FileChange::getLinesAdded).sum();
    }
    
    private int calculateTotalLinesDeleted(List<FileChange> fileChanges) {
        if (fileChanges == null) return 0;
        return fileChanges.stream().mapToInt(FileChange::getLinesDeleted).sum();
    }
    
    private List<String> identifyAffectedServices(List<FileChange> fileChanges) {
        if (fileChanges == null) return new ArrayList<>();
        
        return fileChanges.stream()
                .map(FileChange::getServiceName)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }
    
    private String extractModuleFromPath(String filePath) {
        if (filePath == null) return null;
        
        String[] parts = filePath.split("/");
        if (parts.length > 2) {
            return parts[parts.length - 2]; // Second to last part is usually the module
        }
        return null;
    }
    
    private List<String> extractEndpoints(List<String> lines) {
        List<String> endpoints = new ArrayList<>();
        Pattern endpointPattern = Pattern.compile("@(GetMapping|PostMapping|PutMapping|DeleteMapping|RequestMapping)\\s*\\([^)]*\"([^\"]+)\"");
        
        for (String line : lines) {
            java.util.regex.Matcher matcher = endpointPattern.matcher(line);
            if (matcher.find()) {
                endpoints.add(matcher.group(2));
            }
        }
        
        return endpoints;
    }
    
    private boolean isDatabaseChange(FileChange fileChange) {
        return fileChange.getFilePath() != null && 
               (fileChange.getFilePath().contains("sql") || 
                fileChange.getFilePath().contains("migration") ||
                fileChange.getFilePath().contains("schema"));
    }
    
    private void categorizeChanges(FileChange fileChange, List<String> breakingChanges, List<String> newFeatures, 
                                  List<String> bugFixes, List<String> refactoring, List<String> performanceChanges, 
                                  List<String> securityChanges) {
        
        if (fileChange.getKeywords() != null) {
            if (fileChange.getKeywords().contains("security")) {
                securityChanges.add(fileChange.getFileName());
            }
            if (fileChange.getKeywords().contains("performance")) {
                performanceChanges.add(fileChange.getFileName());
            }
        }
        
        // Simple heuristics for categorization
        if (fileChange.getChangeType() == ChangeType.ADDED) {
            newFeatures.add(fileChange.getFileName());
        } else if (fileChange.getChangeType() == ChangeType.MODIFIED) {
            if (fileChange.getLinesDeleted() > fileChange.getLinesAdded()) {
                refactoring.add(fileChange.getFileName());
            } else {
                bugFixes.add(fileChange.getFileName());
            }
        }
    }
    
    private ImpactLevel calculateOverallImpact(List<String> affectedServices, List<String> breakingChanges, 
                                              List<String> newFeatures, List<String> securityChanges) {
        if (!breakingChanges.isEmpty() || !securityChanges.isEmpty()) {
            return ImpactLevel.CRITICAL;
        }
        if (affectedServices.size() > 2 || !newFeatures.isEmpty()) {
            return ImpactLevel.HIGH;
        }
        if (affectedServices.size() > 1) {
            return ImpactLevel.MEDIUM;
        }
        return ImpactLevel.LOW;
    }
    
    private ImpactLevel calculateServiceImpact(String serviceName, List<FileChange> fileChanges) {
        if (fileChanges == null) return ImpactLevel.NONE;
        
        long changesForService = fileChanges.stream()
                .filter(fc -> serviceName.equals(fc.getServiceName()))
                .count();
        
        if (changesForService > 5) return ImpactLevel.HIGH;
        if (changesForService > 2) return ImpactLevel.MEDIUM;
        if (changesForService > 0) return ImpactLevel.LOW;
        return ImpactLevel.NONE;
    }
    
    private RiskLevel calculateRiskLevel(double riskScore) {
        if (riskScore >= 0.7) return RiskLevel.HIGH;
        if (riskScore >= 0.4) return RiskLevel.MEDIUM;
        return RiskLevel.LOW;
    }
    
    private boolean hasAPIChanges(String serviceName, List<FileChange> fileChanges) {
        if (fileChanges == null) return false;
        
        return fileChanges.stream()
                .anyMatch(fc -> serviceName.equals(fc.getServiceName()) && 
                               fc.getKeywords() != null && 
                               fc.getKeywords().contains("api"));
    }
    
    private List<String> identifyCriticalPaths(PRInfo prInfo, ImpactAnalysis impactAnalysis) {
        List<String> criticalPaths = new ArrayList<>();
        
        if (impactAnalysis.getAffectedServices().contains("gateway-service")) {
            criticalPaths.add("API Gateway routing");
        }
        if (impactAnalysis.getAffectedServices().contains("user-service")) {
            criticalPaths.add("User authentication flow");
        }
        if (impactAnalysis.getAffectedServices().contains("order-service")) {
            criticalPaths.add("Order processing workflow");
        }
        
        return criticalPaths;
    }
    
    private List<String> identifyPotentialIssues(PRInfo prInfo, ImpactAnalysis impactAnalysis) {
        List<String> issues = new ArrayList<>();
        
        if (prInfo.isLargePR()) {
            issues.add("Large PR may be difficult to review thoroughly");
        }
        if (impactAnalysis.hasBreakingChanges()) {
            issues.add("Breaking changes may affect dependent services");
        }
        if (!impactAnalysis.getAffectedConfigurations().isEmpty()) {
            issues.add("Configuration changes may require environment updates");
        }
        
        return issues;
    }
    
    private List<String> identifySecurityConcerns(PRInfo prInfo) {
        List<String> concerns = new ArrayList<>();
        
        if (prInfo.getFileChanges() != null) {
            for (FileChange fileChange : prInfo.getFileChanges()) {
                if (fileChange.getKeywords() != null && fileChange.getKeywords().contains("security")) {
                    concerns.add("Security-related changes in " + fileChange.getFileName());
                }
            }
        }
        
        return concerns;
    }
    
    private List<String> identifyPerformanceImplications(PRInfo prInfo) {
        List<String> implications = new ArrayList<>();
        
        if (prInfo.getFileChanges() != null) {
            for (FileChange fileChange : prInfo.getFileChanges()) {
                if (fileChange.getKeywords() != null && fileChange.getKeywords().contains("performance")) {
                    implications.add("Performance-related changes in " + fileChange.getFileName());
                }
            }
        }
        
        return implications;
    }
    
    private Map<String, Object> calculateMetrics(PRInfo prInfo, ImpactAnalysis impactAnalysis) {
        Map<String, Object> metrics = new HashMap<>();
        
        metrics.put("total_files_changed", prInfo.getFilesChanged());
        metrics.put("total_lines_added", prInfo.getLinesAdded());
        metrics.put("total_lines_deleted", prInfo.getLinesDeleted());
        metrics.put("total_lines_changed", prInfo.getTotalLinesChanged());
        metrics.put("affected_services_count", impactAnalysis.getAffectedServices().size());
        metrics.put("affected_components_count", impactAnalysis.getAffectedComponents().size());
        metrics.put("breaking_changes_count", impactAnalysis.getBreakingChanges().size());
        metrics.put("new_features_count", impactAnalysis.getNewFeatures().size());
        metrics.put("security_changes_count", impactAnalysis.getSecurityChanges().size());
        
        return metrics;
    }
    
    private double calculateConfidenceScore(PRInfo prInfo, ImpactAnalysis impactAnalysis, RiskAssessment riskAssessment) {
        double confidence = 0.8; // Base confidence
        
        // Adjust based on data quality
        if (prInfo.getFileChanges() != null && !prInfo.getFileChanges().isEmpty()) {
            confidence += 0.1;
        }
        if (impactAnalysis.getAffectedServices() != null && !impactAnalysis.getAffectedServices().isEmpty()) {
            confidence += 0.05;
        }
        if (riskAssessment.getRiskFactors() != null && !riskAssessment.getRiskFactors().isEmpty()) {
            confidence += 0.05;
        }
        
        return Math.min(confidence, 1.0);
    }
    
    private List<String> generateInsights(PRInfo prInfo, ImpactAnalysis impactAnalysis, RiskAssessment riskAssessment) {
        List<String> insights = new ArrayList<>();
        
        insights.add("PR affects " + impactAnalysis.getAffectedServices().size() + " services");
        insights.add("Overall impact level: " + impactAnalysis.getOverallImpact().getDisplayName());
        insights.add("Risk level: " + riskAssessment.getOverallRiskLevel().getDisplayName());
        
        if (impactAnalysis.hasBreakingChanges()) {
            insights.add("Breaking changes detected - requires careful deployment planning");
        }
        if (impactAnalysis.hasSecurityChanges()) {
            insights.add("Security changes require additional security testing");
        }
        if (prInfo.isLargePR()) {
            insights.add("Large PR - consider breaking into smaller changes");
        }
        
        return insights;
    }
    
    private Map<String, Object> createMetadata(PRInfo prInfo) {
        Map<String, Object> metadata = new HashMap<>();
        
        metadata.put("analysis_timestamp", LocalDateTime.now());
        metadata.put("pr_size_category", prInfo.isLargePR() ? "large" : "normal");
        metadata.put("change_types", prInfo.getFileChanges() != null ? 
                prInfo.getFileChanges().stream().map(FileChange::getChangeType).distinct().collect(Collectors.toList()) : 
                List.of());
        
        return metadata;
    }
    
    private String generateImpactSummary(ImpactLevel overallImpact, List<String> affectedServices, List<String> breakingChanges) {
        StringBuilder summary = new StringBuilder();
        summary.append("Overall impact: ").append(overallImpact.getDisplayName()).append(". ");
        summary.append("Affects ").append(affectedServices.size()).append(" services: ").append(String.join(", ", affectedServices)).append(". ");
        
        if (!breakingChanges.isEmpty()) {
            summary.append("Contains ").append(breakingChanges.size()).append(" breaking changes. ");
        }
        
        return summary.toString();
    }
}
