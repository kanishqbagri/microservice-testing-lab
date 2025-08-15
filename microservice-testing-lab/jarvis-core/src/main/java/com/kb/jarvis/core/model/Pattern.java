package com.kb.jarvis.core.model;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class Pattern {
    private String patternType;
    private String description;
    private double confidence;
    private List<String> examples;
    private LocalDateTime discoveredAt;
} 