package com.kb.synthetic.model;

import com.kb.synthetic.model.enums.RunStatus;
import com.kb.synthetic.model.enums.RunType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "synthetic_system_runs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemRun {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pull_request_id")
    private PullRequest pullRequest;
    
    @Column(nullable = false)
    private String runId;
    
    @Column(nullable = false)
    private LocalDateTime startTime;
    
    @Column
    private LocalDateTime endTime;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RunStatus status;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RunType runType;
    
    // Overall System Metrics
    @Column
    private Double overallSuccessRate;
    
    @Column
    private Long totalRequests;
    
    @Column
    private Long successfulRequests;
    
    @Column
    private Long failedRequests;
    
    @Column
    private Double avgResponseTimeMs;
    
    @Column
    private Double p95ResponseTimeMs;
    
    @Column
    private Double p99ResponseTimeMs;
    
    @Column
    private Double throughputRps;
    
    // Service-specific Metrics
    @OneToMany(mappedBy = "systemRun", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ServiceMetrics> serviceMetrics;
    
    // E-commerce Flow Metrics
    @OneToMany(mappedBy = "systemRun", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EcommerceFlow> ecommerceFlows;
    
    // Resource Utilization
    @Column
    private Double avgCpuUsage;
    
    @Column
    private Double maxCpuUsage;
    
    @Column
    private Double avgMemoryUsage;
    
    @Column
    private Double maxMemoryUsage;
    
    @Column
    private Double avgDiskUsage;
    
    @Column
    private Double maxDiskUsage;
    
    // Error Analysis
    @ElementCollection
    @CollectionTable(name = "run_error_analysis", joinColumns = @JoinColumn(name = "run_id"))
    @MapKeyColumn(name = "error_type")
    @Column(name = "error_count")
    private Map<String, Integer> errorAnalysis;
    
    // Performance Degradation Indicators
    @Column
    private Boolean hasPerformanceRegression;
    
    @Column
    private Double performanceRegressionScore;
    
    @Column
    private String regressionDetails; // JSON with detailed regression info
    
    // ML Features
    @Column
    private String testScenario; // JSON with test scenario details
    
    @Column
    private Double loadIntensity; // 0.0 to 1.0
    
    @Column
    private String failurePattern; // JSON with failure pattern analysis
    
    @Column
    private Boolean isAnomalous;
    
    @Column
    private Double anomalyScore;
    
    @PrePersist
    protected void onCreate() {
        if (startTime == null) {
            startTime = LocalDateTime.now();
        }
    }
}
