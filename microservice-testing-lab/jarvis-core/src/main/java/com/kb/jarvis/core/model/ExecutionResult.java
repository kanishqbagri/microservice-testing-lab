package com.kb.jarvis.core.model;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class ExecutionResult {
    private boolean success;
    private String message;
    private Map<String, Object> data;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private long duration;
    private List<String> errors;
    private List<String> warnings;
} 