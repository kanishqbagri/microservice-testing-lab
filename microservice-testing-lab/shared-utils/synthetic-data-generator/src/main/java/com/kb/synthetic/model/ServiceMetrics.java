package com.kb.synthetic.model;

import com.kb.synthetic.model.enums.ServiceHealthStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "synthetic_service_metrics")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceMetrics {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "system_run_id")
    private SystemRun systemRun;
    
    @Column(nullable = false)
    private String serviceName;
    
    @Column(nullable = false)
    private String serviceVersion;
    
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    // Health Metrics
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceHealthStatus healthStatus;
    
    @Column
    private Double healthScore; // 0.0 to 1.0
    
    // Performance Metrics
    @Column
    private Long totalRequests;
    
    @Column
    private Long successfulRequests;
    
    @Column
    private Long failedRequests;
    
    @Column
    private Double successRate;
    
    @Column
    private Double errorRate;
    
    @Column
    private Double avgResponseTimeMs;
    
    @Column
    private Double minResponseTimeMs;
    
    @Column
    private Double maxResponseTimeMs;
    
    @Column
    private Double p50ResponseTimeMs;
    
    @Column
    private Double p95ResponseTimeMs;
    
    @Column
    private Double p99ResponseTimeMs;
    
    @Column
    private Double throughputRps;
    
    // Resource Metrics
    @Column
    private Double cpuUsagePercent;
    
    @Column
    private Double memoryUsageMB;
    
    @Column
    private Double memoryUsagePercent;
    
    @Column
    private Double diskUsageMB;
    
    @Column
    private Double diskUsagePercent;
    
    @Column
    private Double networkInMBps;
    
    @Column
    private Double networkOutMBps;
    
    // Error Analysis
    @ElementCollection
    @CollectionTable(name = "service_error_breakdown", joinColumns = @JoinColumn(name = "service_metrics_id"))
    @MapKeyColumn(name = "error_code")
    @Column(name = "error_count")
    private Map<String, Integer> errorBreakdown;
    
    // Dependency Metrics
    @Column
    private Integer upstreamDependencies;
    
    @Column
    private Integer downstreamDependencies;
    
    @Column
    private Double dependencyHealthScore;
    
    // Business Metrics (for e-commerce services)
    @Column
    private Long ordersProcessed;
    
    @Column
    private Long productsViewed;
    
    @Column
    private Long usersActive;
    
    @Column
    private Double revenueGenerated;
    
    // Anomaly Detection
    @Column
    private Boolean isAnomalous;
    
    @Column
    private Double anomalyScore;
    
    @Column
    private String anomalyType; // PERFORMANCE, ERROR_RATE, RESOURCE_USAGE, etc.
    
    @Column
    private String anomalyDetails; // JSON with anomaly details
    
    // ML Features
    @Column
    private String featureVector; // JSON with ML feature vector
    
    @Column
    private Double predictedFailureProbability;
    
    @Column
    private String riskFactors; // JSON with identified risk factors
}
