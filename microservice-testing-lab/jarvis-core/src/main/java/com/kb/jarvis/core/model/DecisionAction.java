package com.kb.jarvis.core.model;

import lombok.Data;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class DecisionAction {
    private ActionType type;
    private String description;
    private TestParameters parameters;
    private String estimatedTime;
    private Priority priority;
    private ExecutionStrategy executionStrategy;
    private double confidence;
    private LocalDateTime timestamp;
    private Map<String, Object> metadata;
} 