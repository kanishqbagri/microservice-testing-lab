package com.kb.jarvis.core.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Analysis of code patterns and architectural concerns
 */
@Data
@Builder
public class CodePatternAnalysis {
    
    private List<String> securityPatterns;
    private List<String> performancePatterns;
    private List<String> architecturalPatterns;
    private List<String> codeQualityIssues;
    private ChangeComplexity complexityLevel;
    private double maintainabilityScore;
    private List<String> technicalDebt;
    private List<String> designPatterns;
    private List<String> dependencies;
    private String summary;
}

