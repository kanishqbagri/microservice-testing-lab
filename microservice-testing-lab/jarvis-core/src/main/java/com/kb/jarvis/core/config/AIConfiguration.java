package com.kb.jarvis.core.config;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Configuration for AI/LLM integration
 */
@Configuration
public class AIConfiguration {
    
    @Value("${jarvis.ai.openai.api-key:demo-key}")
    private String openAiApiKey;
    
    @Value("${jarvis.ai.openai.model:gpt-3.5-turbo}")
    private String openAiModel;
    
    @Value("${jarvis.ai.openai.base-url:https://api.openai.com}")
    private String openAiBaseUrl;
    
    @Value("${jarvis.ai.enabled:true}")
    private boolean aiEnabled;
    
    /**
     * Configure OpenAI Chat Model
     */
    @Bean
    @Primary
    public ChatModel chatModel() {
        if (!aiEnabled) {
            return createMockChatModel();
        }
        
        try {
            OpenAiApi openAiApi = new OpenAiApi(openAiBaseUrl, openAiApiKey);
            
            OpenAiChatOptions options = OpenAiChatOptions.builder()
                    .withModel(openAiModel)
                    .withTemperature(0.3) // Lower temperature for more consistent analysis
                    .withMaxTokens(4000) // Sufficient for detailed analysis
                    .build();
            
            return new OpenAiChatModel(openAiApi, options);
            
        } catch (Exception e) {
            // Fallback to mock model if OpenAI is not available
            return createMockChatModel();
        }
    }
    
    /**
     * Configure Chat Client
     */
    @Bean
    public ChatClient chatClient(ChatModel chatModel) {
        return ChatClient.builder(chatModel).build();
    }
    
    /**
     * Create a mock chat model for development/demo purposes
     */
    private ChatModel createMockChatModel() {
        return new MockChatModel();
    }
    
    /**
     * Mock Chat Model for development and demo purposes
     */
    private static class MockChatModel implements ChatModel {
        
        @Override
        public ChatResponse call(Prompt prompt) {
            String content = prompt.getContents();
            
            // Generate realistic mock responses based on the prompt content
            String mockResponse = generateMockResponse(content);
            
            return new ChatResponse() {
                @Override
                public Result getResult() {
                    return new Result() {
                        @Override
                        public Output getOutput() {
                            return new Output() {
                                @Override
                                public String getContent() {
                                    return mockResponse;
                                }
                            };
                        }
                    };
                }
            };
        }
        
        private String generateMockResponse(String prompt) {
            if (prompt.contains("security") || prompt.contains("authentication") || prompt.contains("jwt")) {
                return """
                {
                    "securityPatterns": ["JWT token generation", "Authentication endpoint", "Password validation"],
                    "performancePatterns": ["Token validation overhead", "Database authentication queries"],
                    "architecturalPatterns": ["REST API design", "Service layer pattern", "Controller pattern"],
                    "codeQualityIssues": ["Hardcoded secret key", "Missing input validation", "No rate limiting"],
                    "complexityLevel": "MEDIUM",
                    "maintainabilityScore": 0.7,
                    "technicalDebt": ["Hardcoded configuration", "Missing error handling"],
                    "designPatterns": ["MVC", "Service Layer", "Repository Pattern"],
                    "dependencies": ["Spring Security", "JWT library", "BCrypt"],
                    "summary": "Authentication system implementation with JWT tokens. Good architectural patterns but needs security hardening."
                }
                """;
            } else if (prompt.contains("impact") || prompt.contains("breaking")) {
                return """
                {
                    "overallImpact": "MEDIUM",
                    "affectedServices": ["user-service", "gateway-service"],
                    "breakingChanges": ["New authentication endpoint", "Modified user model"],
                    "newFeatures": ["JWT authentication", "Token validation"],
                    "modifiedFeatures": ["User login flow", "API security"],
                    "databaseChanges": ["User table modifications"],
                    "apiChanges": ["New /api/auth/login endpoint", "New /api/auth/validate endpoint"],
                    "configurationChanges": ["JWT secret configuration", "Security settings"],
                    "deploymentImpact": "MEDIUM",
                    "rollbackComplexity": "LOW",
                    "testingScope": ["Authentication flow", "Token validation", "API integration"],
                    "monitoringNeeds": ["Authentication success rate", "Token validation errors"],
                    "documentationNeeds": ["API documentation", "Authentication guide"],
                    "summary": "Medium impact changes affecting user authentication across multiple services."
                }
                """;
            } else if (prompt.contains("risk")) {
                return """
                {
                    "overallRiskLevel": "MEDIUM",
                    "riskScore": 0.6,
                    "riskFactors": [
                        {
                            "factor": "Hardcoded JWT secret key",
                            "severity": "HIGH",
                            "probability": 0.8,
                            "impact": "Security vulnerability if secret is exposed",
                            "mitigation": "Move secret to environment variables or secure configuration"
                        },
                        {
                            "factor": "Missing input validation",
                            "severity": "MEDIUM",
                            "probability": 0.6,
                            "impact": "Potential injection attacks or data corruption",
                            "mitigation": "Add comprehensive input validation and sanitization"
                        }
                    ],
                    "securityRisks": ["Hardcoded secrets", "Missing input validation", "No rate limiting"],
                    "performanceRisks": ["Token validation overhead", "Database query performance"],
                    "stabilityRisks": ["Authentication service dependency", "Token expiration handling"],
                    "businessRisks": ["User login failures", "Security breach potential"],
                    "technicalRisks": ["Code maintainability", "Testing coverage"],
                    "mitigationStrategies": ["Environment-based configuration", "Input validation", "Rate limiting", "Comprehensive testing"],
                    "requiresApproval": true,
                    "approvalLevel": "TEAM_LEAD",
                    "summary": "Medium risk changes requiring security review and additional testing."
                }
                """;
            } else if (prompt.contains("test") || prompt.contains("recommendation")) {
                return """
                {
                    "recommendations": [
                        {
                            "testType": "UNIT_TEST",
                            "description": "Test JWT token generation and validation logic",
                            "priority": 0.9,
                            "critical": true,
                            "estimatedTime": "2-3 hours",
                            "tools": ["JUnit", "Mockito", "Spring Boot Test"],
                            "scenarios": ["Valid token generation", "Invalid token handling", "Token expiration"],
                            "acceptanceCriteria": ["All authentication methods tested", "Edge cases covered"],
                            "automationLevel": "FULLY_AUTOMATED",
                            "environment": "LOCAL",
                            "dependencies": ["Test database", "Mock services"]
                        },
                        {
                            "testType": "INTEGRATION_TEST",
                            "description": "Test authentication flow end-to-end",
                            "priority": 0.8,
                            "critical": true,
                            "estimatedTime": "4-5 hours",
                            "tools": ["TestContainers", "RestAssured", "Spring Boot Test"],
                            "scenarios": ["Login flow", "Token validation", "Protected endpoint access"],
                            "acceptanceCriteria": ["Complete authentication flow works", "Error handling verified"],
                            "automationLevel": "FULLY_AUTOMATED",
                            "environment": "STAGING",
                            "dependencies": ["User service", "Database", "JWT configuration"]
                        },
                        {
                            "testType": "SECURITY_TEST",
                            "description": "Security testing for authentication vulnerabilities",
                            "priority": 0.9,
                            "critical": true,
                            "estimatedTime": "3-4 hours",
                            "tools": ["OWASP ZAP", "Burp Suite", "Custom security tests"],
                            "scenarios": ["SQL injection attempts", "JWT manipulation", "Brute force attacks"],
                            "acceptanceCriteria": ["No security vulnerabilities found", "Rate limiting works"],
                            "automationLevel": "SEMI_AUTOMATED",
                            "environment": "STAGING",
                            "dependencies": ["Security testing tools", "Test environment"]
                        }
                    ],
                    "testingStrategy": "Comprehensive testing approach covering unit, integration, and security aspects",
                    "testDataRequirements": ["Test user accounts", "Valid JWT tokens", "Invalid tokens"],
                    "environmentSetup": ["JWT configuration", "Test database", "Mock external services"],
                    "monitoringNeeds": ["Authentication metrics", "Error rates", "Performance metrics"]
                }
                """;
            } else {
                return """
                {
                    "insights": [
                        "Architecture: Well-structured authentication system following REST principles",
                        "Security: JWT implementation is standard but needs configuration hardening",
                        "Performance: Token validation is efficient but consider caching for high traffic",
                        "Maintainability: Code is clean but needs better error handling",
                        "Testing: Comprehensive test coverage needed for security-critical functionality"
                    ],
                    "warnings": [
                        "Security: Hardcoded JWT secret poses security risk",
                        "Configuration: Missing environment-based configuration",
                        "Error Handling: Incomplete error handling for edge cases",
                        "Documentation: API documentation needs updating"
                    ]
                }
                """;
            }
        }
    }
}
