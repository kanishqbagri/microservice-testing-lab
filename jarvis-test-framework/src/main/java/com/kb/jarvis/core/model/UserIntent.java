package com.kb.jarvis.core.model;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class UserIntent {
    private String rawInput;
    private IntentType type;
    private String description;
    private Map<String, Object> parameters;
    private double confidence;
    private LocalDateTime timestamp;
} 