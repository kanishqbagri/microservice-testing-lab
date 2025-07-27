package com.kb.jarvis.core.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Recommendation {
    private String description;
    private RecommendationType type;
    private double priority;
    private String rationale;
} 