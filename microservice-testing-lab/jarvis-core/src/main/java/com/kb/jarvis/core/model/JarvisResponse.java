package com.kb.jarvis.core.model;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class JarvisResponse {
    private String message;
    private JarvisResponseStatus status;
    private DecisionAction action;
    private Map<String, Object> data;
    private LocalDateTime timestamp;
} 