package com.kb.jarvis.core.model;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class Trend {
    private String metric;
    private TrendDirection direction;
    private double changeRate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
} 