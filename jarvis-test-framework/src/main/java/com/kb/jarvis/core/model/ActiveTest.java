package com.kb.jarvis.core.model;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ActiveTest {
    private String testId;
    private String testName;
    private String service;
    private TestStatus status;
    private LocalDateTime startTime;
    private long duration;
    private double progress;
} 