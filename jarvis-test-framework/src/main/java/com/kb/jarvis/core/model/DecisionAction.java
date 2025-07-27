package com.kb.jarvis.core.model;

import lombok.Builder;
import lombok.Data;
import java.util.Map;

@Data
@Builder
public class DecisionAction {
    private ActionType type;
    private String description;
    private TestParameters parameters;
    private String estimatedTime;
    private Priority priority;
    private Map<String, Object> metadata;
} 