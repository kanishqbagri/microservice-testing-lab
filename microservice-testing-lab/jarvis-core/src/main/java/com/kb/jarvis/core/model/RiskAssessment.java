package com.kb.jarvis.core.model;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class RiskAssessment {
    private RiskLevel level;
    private List<String> riskFactors;
    private String mitigationStrategy;
} 