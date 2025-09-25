package com.kb.synthetic.model;

import com.kb.synthetic.model.enums.StepStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "synthetic_flow_steps")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlowStep {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ecommerce_flow_id")
    private EcommerceFlow ecommerceFlow;
    
    @Column(nullable = false)
    private String stepId;
    
    @Column(nullable = false)
    private String stepName;
    
    @Column(nullable = false)
    private Integer stepOrder;
    
    @Column(nullable = false)
    private String serviceName;
    
    @Column(nullable = false)
    private String endpoint;
    
    @Column(nullable = false)
    private String httpMethod;
    
    @Column(nullable = false)
    private LocalDateTime startTime;
    
    @Column
    private LocalDateTime endTime;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StepStatus status;
    
    // Performance Metrics
    @Column
    private Long durationMs;
    
    @Column
    private Integer responseCode;
    
    @Column
    private Long responseSizeBytes;
    
    @Column
    private Double responseTimeMs;
    
    // Request/Response Data
    @Column(length = 5000)
    private String requestPayload; // JSON
    
    @Column(length = 10000)
    private String responsePayload; // JSON
    
    @Column(length = 2000)
    private String errorMessage;
    
    // Business Context
    @Column
    private String businessAction; // ADD_TO_CART, VALIDATE_PAYMENT, etc.
    
    @Column
    private String userContext; // JSON with user context
    
    @Column
    private String sessionContext; // JSON with session context
    
    // Error Analysis
    @Column
    private String errorType; // TIMEOUT, VALIDATION_ERROR, SERVICE_UNAVAILABLE, etc.
    
    @Column
    private String errorCategory; // CLIENT_ERROR, SERVER_ERROR, NETWORK_ERROR, etc.
    
    @Column
    private Boolean isRetryable;
    
    @Column
    private Integer retryAttempts;
    
    // ML Features
    @Column
    private String featureVector; // JSON with ML features
    
    @Column
    private Double failureProbability;
    
    @Column
    private String riskFactors; // JSON with risk factors
    
    @Column
    private Boolean isAnomalous;
    
    @Column
    private Double anomalyScore;
}
