package com.kb.jarvis.core.model;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class PerformancePrediction {
    private double estimatedExecutionTime;
    private double successProbability;
    private List<String> potentialBottlenecks;
} 