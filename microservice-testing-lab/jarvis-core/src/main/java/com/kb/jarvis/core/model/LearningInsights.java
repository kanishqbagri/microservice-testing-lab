package com.kb.jarvis.core.model;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class LearningInsights {
    private List<Pattern> patterns;
    private List<Trend> trends;
    private List<Optimization> optimizations;
    private Map<String, Double> predictions;
} 