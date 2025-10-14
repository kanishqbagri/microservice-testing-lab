package com.kb.jarvis.core.ai;

import com.kb.jarvis.core.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * LLM-powered PR Analysis component
 * Uses real AI models for intelligent code analysis, pattern recognition, and impact assessment
 */
@Component
public class LLMPRAnalyzer {
    
    private static final Logger log = LoggerFactory.getLogger(LLMPRAnalyzer.class);
    
    @Autowired
    private ChatClient chatClient;
    
    /**
     * Analyze PR using LLM for intelligent insights
     */
    public LLMAnalysisResult analyzeWithLLM(PRInfo prInfo, List<FileChange> fileChanges) {
        log.info("Starting LLM analysis for PR: {}", prInfo.getPrId());
        
        try {
            // Step 1: Code Pattern Analysis using LLM
            CodePatternAnalysis patternAnalysis = analyzeCodePatternsWithLLM(fileChanges);
            
            // Step 2: Impact Analysis using LLM
            ImpactAnalysis impactAnalysis = analyzeImpactWithLLM(prInfo, fileChanges, patternAnalysis);
            
            // Step 3: Risk Assessment using LLM
            RiskAssessment riskAssessment = assessRiskWithLLM(prInfo, fileChanges, patternAnalysis);
            
            // Step 4: Generate intelligent recommendations
            List<TestRecommendation> recommendations = generateRecommendationsWithLLM(prInfo, fileChanges, patternAnalysis, impactAnalysis, riskAssessment);
            
            // Step 5: Generate insights and warnings
            List<String> insights = generateInsightsWithLLM(prInfo, fileChanges, patternAnalysis, impactAnalysis, riskAssessment);
            List<String> warnings = generateWarningsWithLLM(prInfo, fileChanges, patternAnalysis, impactAnalysis, riskAssessment);
            
            // Step 6: Calculate AI confidence
            double confidence = calculateLLMConfidence(fileChanges, patternAnalysis, impactAnalysis, riskAssessment);
            
            return LLMAnalysisResult.builder()
                    .patternAnalysis(patternAnalysis)
                    .impactAnalysis(impactAnalysis)
                    .riskAssessment(riskAssessment)
                    .testRecommendations(recommendations)
                    .insights(insights)
                    .warnings(warnings)
                    .confidence(confidence)
                    .analysisTimestamp(System.currentTimeMillis())
                    .build();
                    
        } catch (Exception e) {
            log.error("Error in LLM analysis: {}", e.getMessage(), e);
            return createFallbackAnalysis(prInfo, fileChanges);
        }
    }
    
    /**
     * Analyze code patterns using LLM
     */
    private CodePatternAnalysis analyzeCodePatternsWithLLM(List<FileChange> fileChanges) {
        log.info("Analyzing code patterns with LLM...");
        
        // Prepare code context for LLM
        String codeContext = buildCodeContext(fileChanges);
        
        String prompt = """
            You are an expert software architect and code reviewer. Analyze the following code changes and identify patterns, architectural concerns, and potential issues.
            
            Code Changes:
            {codeContext}
            
            Please provide a detailed analysis in the following JSON format:
            {
                "securityPatterns": ["list of security-related patterns found"],
                "performancePatterns": ["list of performance-related patterns found"],
                "architecturalPatterns": ["list of architectural patterns found"],
                "codeQualityIssues": ["list of code quality issues"],
                "complexityLevel": "LOW|MEDIUM|HIGH",
                "maintainabilityScore": 0.0-1.0,
                "technicalDebt": ["list of technical debt items"],
                "designPatterns": ["list of design patterns used"],
                "dependencies": ["list of new dependencies or changes"],
                "summary": "brief summary of the analysis"
            }
            
            Focus on:
            1. Security vulnerabilities and best practices
            2. Performance implications
            3. Code maintainability and readability
            4. Architectural consistency
            5. Design pattern usage
            6. Dependency management
            """;
        
        PromptTemplate template = new PromptTemplate(prompt);
        Prompt llmPrompt = template.create(Map.of("codeContext", codeContext));
        
        try {
            ChatResponse response = chatClient.call(llmPrompt);
            String analysis = response.getResult().getOutput().getContent();
            
            // Parse LLM response and create CodePatternAnalysis
            return parseCodePatternAnalysis(analysis);
            
        } catch (Exception e) {
            log.error("Error in LLM code pattern analysis: {}", e.getMessage(), e);
            return createFallbackPatternAnalysis(fileChanges);
        }
    }
    
    /**
     * Analyze impact using LLM
     */
    private ImpactAnalysis analyzeImpactWithLLM(PRInfo prInfo, List<FileChange> fileChanges, CodePatternAnalysis patternAnalysis) {
        log.info("Analyzing impact with LLM...");
        
        String prompt = """
            You are an expert in software architecture and system design. Analyze the impact of these code changes on the overall system.
            
            PR Information:
            - Title: {title}
            - Description: {description}
            - Files Changed: {fileCount}
            
            Code Changes:
            {codeContext}
            
            Pattern Analysis:
            {patternAnalysis}
            
            Please provide a detailed impact analysis in the following JSON format:
            {
                "overallImpact": "LOW|MEDIUM|HIGH|CRITICAL",
                "affectedServices": ["list of services that will be affected"],
                "breakingChanges": ["list of breaking changes"],
                "newFeatures": ["list of new features introduced"],
                "modifiedFeatures": ["list of existing features modified"],
                "databaseChanges": ["list of database-related changes"],
                "apiChanges": ["list of API changes"],
                "configurationChanges": ["list of configuration changes"],
                "deploymentImpact": "LOW|MEDIUM|HIGH",
                "rollbackComplexity": "LOW|MEDIUM|HIGH",
                "testingScope": ["list of areas that need testing"],
                "monitoringNeeds": ["list of monitoring requirements"],
                "documentationNeeds": ["list of documentation that needs updating"],
                "summary": "brief summary of the impact analysis"
            }
            
            Consider:
            1. Service dependencies and interactions
            2. Data flow and storage changes
            3. API contracts and backward compatibility
            4. Configuration and environment changes
            5. Deployment and infrastructure impact
            6. User experience and business impact
            """;
        
        PromptTemplate template = new PromptTemplate(prompt);
        Prompt llmPrompt = template.create(Map.of(
            "title", prInfo.getTitle(),
            "description", prInfo.getDescription() != null ? prInfo.getDescription() : "No description provided",
            "fileCount", String.valueOf(fileChanges.size()),
            "codeContext", buildCodeContext(fileChanges),
            "patternAnalysis", patternAnalysis.getSummary()
        ));
        
        try {
            ChatResponse response = chatClient.call(llmPrompt);
            String analysis = response.getResult().getOutput().getContent();
            
            return parseImpactAnalysis(analysis);
            
        } catch (Exception e) {
            log.error("Error in LLM impact analysis: {}", e.getMessage(), e);
            return createFallbackImpactAnalysis(prInfo, fileChanges);
        }
    }
    
    /**
     * Assess risk using LLM
     */
    private RiskAssessment assessRiskWithLLM(PRInfo prInfo, List<FileChange> fileChanges, CodePatternAnalysis patternAnalysis) {
        log.info("Assessing risk with LLM...");
        
        String prompt = """
            You are a senior software engineer and risk assessment expert. Evaluate the risks associated with these code changes.
            
            PR Information:
            - Title: {title}
            - Author: {author}
            - Branch: {sourceBranch} -> {targetBranch}
            
            Code Changes:
            {codeContext}
            
            Pattern Analysis:
            {patternAnalysis}
            
            Please provide a comprehensive risk assessment in the following JSON format:
            {
                "overallRiskLevel": "LOW|MEDIUM|HIGH|CRITICAL",
                "riskScore": 0.0-1.0,
                "riskFactors": [
                    {
                        "factor": "description of risk factor",
                        "severity": "LOW|MEDIUM|HIGH|CRITICAL",
                        "probability": 0.0-1.0,
                        "impact": "description of potential impact",
                        "mitigation": "suggested mitigation strategy"
                    }
                ],
                "securityRisks": ["list of security risks"],
                "performanceRisks": ["list of performance risks"],
                "stabilityRisks": ["list of stability risks"],
                "businessRisks": ["list of business risks"],
                "technicalRisks": ["list of technical risks"],
                "mitigationStrategies": ["list of mitigation strategies"],
                "requiresApproval": true/false,
                "approvalLevel": "TEAM_LEAD|ARCHITECT|CTO",
                "summary": "brief summary of the risk assessment"
            }
            
            Evaluate risks in these areas:
            1. Security vulnerabilities and data protection
            2. Performance degradation and scalability
            3. System stability and reliability
            4. Business continuity and user experience
            5. Technical debt and maintainability
            6. Compliance and regulatory requirements
            """;
        
        PromptTemplate template = new PromptTemplate(prompt);
        Prompt llmPrompt = template.create(Map.of(
            "title", prInfo.getTitle(),
            "author", prInfo.getAuthor(),
            "sourceBranch", prInfo.getSourceBranch(),
            "targetBranch", prInfo.getTargetBranch(),
            "codeContext", buildCodeContext(fileChanges),
            "patternAnalysis", patternAnalysis.getSummary()
        ));
        
        try {
            ChatResponse response = chatClient.call(llmPrompt);
            String analysis = response.getResult().getOutput().getContent();
            
            return parseRiskAssessment(analysis);
            
        } catch (Exception e) {
            log.error("Error in LLM risk assessment: {}", e.getMessage(), e);
            return createFallbackRiskAssessment(prInfo, fileChanges);
        }
    }
    
    /**
     * Generate test recommendations using LLM
     */
    private List<TestRecommendation> generateRecommendationsWithLLM(PRInfo prInfo, List<FileChange> fileChanges, 
                                                                   CodePatternAnalysis patternAnalysis, 
                                                                   ImpactAnalysis impactAnalysis, 
                                                                   RiskAssessment riskAssessment) {
        log.info("Generating test recommendations with LLM...");
        
        String prompt = """
            You are a senior QA engineer and testing strategist. Based on the code changes and analysis, recommend comprehensive testing strategies.
            
            PR Information:
            - Title: {title}
            - Risk Level: {riskLevel}
            - Impact Level: {impactLevel}
            
            Code Changes:
            {codeContext}
            
            Pattern Analysis:
            {patternAnalysis}
            
            Impact Analysis:
            {impactAnalysis}
            
            Risk Assessment:
            {riskAssessment}
            
            Please provide detailed test recommendations in the following JSON format:
            {
                "recommendations": [
                    {
                        "testType": "UNIT_TEST|INTEGRATION_TEST|E2E_TEST|PERFORMANCE_TEST|SECURITY_TEST|CONTRACT_TEST|CHAOS_TEST",
                        "description": "detailed description of the test",
                        "priority": 0.0-1.0,
                        "critical": true/false,
                        "estimatedTime": "estimated time to implement",
                        "tools": ["list of recommended tools"],
                        "scenarios": ["list of test scenarios"],
                        "acceptanceCriteria": ["list of acceptance criteria"],
                        "automationLevel": "MANUAL|SEMI_AUTOMATED|FULLY_AUTOMATED",
                        "environment": "LOCAL|STAGING|PRODUCTION",
                        "dependencies": ["list of dependencies"]
                    }
                ],
                "testingStrategy": "brief description of overall testing strategy",
                "testDataRequirements": ["list of test data requirements"],
                "environmentSetup": ["list of environment setup requirements"],
                "monitoringNeeds": ["list of monitoring requirements during testing"]
            }
            
            Consider:
            1. Test coverage for new functionality
            2. Regression testing for modified code
            3. Integration testing for service interactions
            4. Performance testing for scalability
            5. Security testing for vulnerabilities
            6. User acceptance testing for business value
            7. Chaos engineering for resilience
            """;
        
        PromptTemplate template = new PromptTemplate(prompt);
        Prompt llmPrompt = template.create(Map.of(
            "title", prInfo.getTitle(),
            "riskLevel", riskAssessment.getOverallRiskLevel().toString(),
            "impactLevel", impactAnalysis.getOverallImpact().toString(),
            "codeContext", buildCodeContext(fileChanges),
            "patternAnalysis", patternAnalysis.getSummary(),
            "impactAnalysis", impactAnalysis.getSummary(),
            "riskAssessment", riskAssessment.getSummary()
        ));
        
        try {
            ChatResponse response = chatClient.call(llmPrompt);
            String analysis = response.getResult().getOutput().getContent();
            
            return parseTestRecommendations(analysis);
            
        } catch (Exception e) {
            log.error("Error in LLM test recommendations: {}", e.getMessage(), e);
            return createFallbackTestRecommendations(prInfo, fileChanges);
        }
    }
    
    /**
     * Generate insights using LLM
     */
    private List<String> generateInsightsWithLLM(PRInfo prInfo, List<FileChange> fileChanges, 
                                                CodePatternAnalysis patternAnalysis, 
                                                ImpactAnalysis impactAnalysis, 
                                                RiskAssessment riskAssessment) {
        log.info("Generating insights with LLM...");
        
        String prompt = """
            You are a senior software architect and technical advisor. Provide key insights and recommendations based on this PR analysis.
            
            PR: {title}
            Risk Level: {riskLevel}
            Impact Level: {impactLevel}
            
            Analysis Summary:
            - Pattern Analysis: {patternSummary}
            - Impact Analysis: {impactSummary}
            - Risk Assessment: {riskSummary}
            
            Please provide 5-7 key insights in the following format:
            1. [Insight category]: [Detailed insight with actionable advice]
            2. [Insight category]: [Detailed insight with actionable advice]
            ...
            
            Focus on:
            - Architectural implications
            - Best practices and improvements
            - Potential issues and solutions
            - Performance considerations
            - Security recommendations
            - Maintainability suggestions
            - Business impact insights
            """;
        
        PromptTemplate template = new PromptTemplate(prompt);
        Prompt llmPrompt = template.create(Map.of(
            "title", prInfo.getTitle(),
            "riskLevel", riskAssessment.getOverallRiskLevel().toString(),
            "impactLevel", impactAnalysis.getOverallImpact().toString(),
            "patternSummary", patternAnalysis.getSummary(),
            "impactSummary", impactAnalysis.getSummary(),
            "riskSummary", riskAssessment.getSummary()
        ));
        
        try {
            ChatResponse response = chatClient.call(llmPrompt);
            String insights = response.getResult().getOutput().getContent();
            
            return Arrays.stream(insights.split("\n"))
                    .filter(line -> line.trim().matches("^\\d+\\..*"))
                    .map(line -> line.replaceFirst("^\\d+\\.\\s*", ""))
                    .collect(Collectors.toList());
            
        } catch (Exception e) {
            log.error("Error in LLM insights generation: {}", e.getMessage(), e);
            return createFallbackInsights(prInfo, fileChanges);
        }
    }
    
    /**
     * Generate warnings using LLM
     */
    private List<String> generateWarningsWithLLM(PRInfo prInfo, List<FileChange> fileChanges, 
                                                CodePatternAnalysis patternAnalysis, 
                                                ImpactAnalysis impactAnalysis, 
                                                RiskAssessment riskAssessment) {
        log.info("Generating warnings with LLM...");
        
        String prompt = """
            You are a senior software engineer and code reviewer. Identify potential issues and warnings for this PR.
            
            PR: {title}
            Risk Level: {riskLevel}
            Impact Level: {impactLevel}
            
            Analysis Summary:
            - Pattern Analysis: {patternSummary}
            - Impact Analysis: {impactSummary}
            - Risk Assessment: {riskSummary}
            
            Please provide specific warnings in the following format:
            - [Warning category]: [Specific warning with potential consequences]
            - [Warning category]: [Specific warning with potential consequences]
            ...
            
            Look for:
            - Security vulnerabilities
            - Performance issues
            - Breaking changes
            - Code quality problems
            - Missing tests
            - Configuration issues
            - Deployment concerns
            - Business logic errors
            """;
        
        PromptTemplate template = new PromptTemplate(prompt);
        Prompt llmPrompt = template.create(Map.of(
            "title", prInfo.getTitle(),
            "riskLevel", riskAssessment.getOverallRiskLevel().toString(),
            "impactLevel", impactAnalysis.getOverallImpact().toString(),
            "patternSummary", patternAnalysis.getSummary(),
            "impactSummary", impactAnalysis.getSummary(),
            "riskSummary", riskAssessment.getSummary()
        ));
        
        try {
            ChatResponse response = chatClient.call(llmPrompt);
            String warnings = response.getResult().getOutput().getContent();
            
            return Arrays.stream(warnings.split("\n"))
                    .filter(line -> line.trim().startsWith("-"))
                    .map(line -> line.replaceFirst("^-\\s*", ""))
                    .collect(Collectors.toList());
            
        } catch (Exception e) {
            log.error("Error in LLM warnings generation: {}", e.getMessage(), e);
            return createFallbackWarnings(prInfo, fileChanges);
        }
    }
    
    /**
     * Build code context for LLM analysis
     */
    private String buildCodeContext(List<FileChange> fileChanges) {
        StringBuilder context = new StringBuilder();
        
        for (FileChange change : fileChanges) {
            context.append("File: ").append(change.getFilePath()).append("\n");
            context.append("Change Type: ").append(change.getChangeType()).append("\n");
            context.append("Lines Added: ").append(change.getLinesAdded()).append("\n");
            context.append("Lines Deleted: ").append(change.getLinesDeleted()).append("\n");
            
            if (change.getKeywords() != null && !change.getKeywords().isEmpty()) {
                context.append("Keywords: ").append(String.join(", ", change.getKeywords())).append("\n");
            }
            
            if (change.getDiff() != null && !change.getDiff().isEmpty()) {
                context.append("Diff:\n").append(change.getDiff()).append("\n");
            }
            
            context.append("---\n");
        }
        
        return context.toString();
    }
    
    /**
     * Calculate LLM confidence score
     */
    private double calculateLLMConfidence(List<FileChange> fileChanges, CodePatternAnalysis patternAnalysis, 
                                        ImpactAnalysis impactAnalysis, RiskAssessment riskAssessment) {
        // Base confidence on analysis completeness and consistency
        double baseConfidence = 0.7;
        
        // Increase confidence based on analysis quality
        if (patternAnalysis.getSummary() != null && !patternAnalysis.getSummary().isEmpty()) {
            baseConfidence += 0.1;
        }
        
        if (impactAnalysis.getSummary() != null && !impactAnalysis.getSummary().isEmpty()) {
            baseConfidence += 0.1;
        }
        
        if (riskAssessment.getSummary() != null && !riskAssessment.getSummary().isEmpty()) {
            baseConfidence += 0.1;
        }
        
        return Math.min(1.0, baseConfidence);
    }
    
    // Parsing methods for LLM responses
    private CodePatternAnalysis parseCodePatternAnalysis(String analysis) {
        // TODO: Implement JSON parsing for LLM response
        // For now, return a basic analysis
        return CodePatternAnalysis.builder()
                .summary("LLM analysis completed")
                .complexityLevel(ChangeComplexity.MEDIUM)
                .build();
    }
    
    private ImpactAnalysis parseImpactAnalysis(String analysis) {
        // TODO: Implement JSON parsing for LLM response
        return ImpactAnalysis.builder()
                .overallImpact(ImpactLevel.MEDIUM)
                .summary("LLM impact analysis completed")
                .build();
    }
    
    private RiskAssessment parseRiskAssessment(String analysis) {
        // TODO: Implement JSON parsing for LLM response
        return RiskAssessment.builder()
                .overallRiskLevel(RiskLevel.MEDIUM)
                .summary("LLM risk assessment completed")
                .build();
    }
    
    private List<TestRecommendation> parseTestRecommendations(String analysis) {
        // TODO: Implement JSON parsing for LLM response
        return createFallbackTestRecommendations(null, null);
    }
    
    // Fallback methods
    private LLMAnalysisResult createFallbackAnalysis(PRInfo prInfo, List<FileChange> fileChanges) {
        return LLMAnalysisResult.builder()
                .patternAnalysis(createFallbackPatternAnalysis(fileChanges))
                .impactAnalysis(createFallbackImpactAnalysis(prInfo, fileChanges))
                .riskAssessment(createFallbackRiskAssessment(prInfo, fileChanges))
                .testRecommendations(createFallbackTestRecommendations(prInfo, fileChanges))
                .insights(createFallbackInsights(prInfo, fileChanges))
                .warnings(createFallbackWarnings(prInfo, fileChanges))
                .confidence(0.5)
                .analysisTimestamp(System.currentTimeMillis())
                .build();
    }
    
    private CodePatternAnalysis createFallbackPatternAnalysis(List<FileChange> fileChanges) {
        return CodePatternAnalysis.builder()
                .summary("Fallback pattern analysis")
                .complexityLevel(ChangeComplexity.MEDIUM)
                .build();
    }
    
    private ImpactAnalysis createFallbackImpactAnalysis(PRInfo prInfo, List<FileChange> fileChanges) {
        return ImpactAnalysis.builder()
                .overallImpact(ImpactLevel.MEDIUM)
                .summary("Fallback impact analysis")
                .build();
    }
    
    private RiskAssessment createFallbackRiskAssessment(PRInfo prInfo, List<FileChange> fileChanges) {
        return RiskAssessment.builder()
                .overallRiskLevel(RiskLevel.MEDIUM)
                .summary("Fallback risk assessment")
                .build();
    }
    
    private List<TestRecommendation> createFallbackTestRecommendations(PRInfo prInfo, List<FileChange> fileChanges) {
        return Arrays.asList(
            TestRecommendation.builder()
                    .testType("UNIT_TEST")
                    .description("Add unit tests for new functionality")
                    .priority(0.8)
                    .build()
        );
    }
    
    private List<String> createFallbackInsights(PRInfo prInfo, List<FileChange> fileChanges) {
        return Arrays.asList(
            "Code changes require thorough testing",
            "Consider impact on existing functionality"
        );
    }
    
    private List<String> createFallbackWarnings(PRInfo prInfo, List<FileChange> fileChanges) {
        return Arrays.asList(
            "Review code changes carefully before merge",
            "Ensure adequate test coverage"
        );
    }
}

