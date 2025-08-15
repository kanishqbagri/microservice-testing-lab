package com.kb.jarvis.core.model;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class TestFailure {
    private String testId;
    private String testName;
    private String service;
    private String errorMessage;
    private String stackTrace;
    private LocalDateTime failureTime;
    private FailureType type;
    private double severity;
} 