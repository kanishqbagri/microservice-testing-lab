package com.kb.jarvis.core.model;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class Optimization {
    private String area;
    private String description;
    private double expectedImprovement;
    private List<String> actions;
    private Priority priority;
} 