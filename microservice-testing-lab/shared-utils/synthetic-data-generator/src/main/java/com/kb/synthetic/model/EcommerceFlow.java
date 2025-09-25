package com.kb.synthetic.model;

import com.kb.synthetic.model.enums.FlowType;
import com.kb.synthetic.model.enums.FlowStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "synthetic_ecommerce_flows")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EcommerceFlow {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "system_run_id")
    private SystemRun systemRun;
    
    @Column(nullable = false)
    private String flowId;
    
    @Column(nullable = false)
    private String userId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FlowType flowType;
    
    @Column(nullable = false)
    private LocalDateTime startTime;
    
    @Column
    private LocalDateTime endTime;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FlowStatus status;
    
    // Flow Steps
    @OneToMany(mappedBy = "ecommerceFlow", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FlowStep> flowSteps;
    
    // Performance Metrics
    @Column
    private Long totalDurationMs;
    
    @Column
    private Double avgStepDurationMs;
    
    @Column
    private Integer totalSteps;
    
    @Column
    private Integer successfulSteps;
    
    @Column
    private Integer failedSteps;
    
    @Column
    private Double successRate;
    
    // Business Metrics
    @Column
    private Integer itemsInCart;
    
    @Column
    private Double cartValue;
    
    @Column
    private Double finalOrderValue;
    
    @Column
    private String paymentMethod;
    
    @Column
    private Boolean paymentSuccessful;
    
    @Column
    private String shippingAddress;
    
    @Column
    private String billingAddress;
    
    // Error Analysis
    @ElementCollection
    @CollectionTable(name = "flow_error_analysis", joinColumns = @JoinColumn(name = "flow_id"))
    @MapKeyColumn(name = "error_type")
    @Column(name = "error_count")
    private Map<String, Integer> errorAnalysis;
    
    @Column
    private String failurePoint; // Which step failed
    
    @Column
    private String failureReason; // Why it failed
    
    // User Behavior
    @Column
    private String userSegment; // NEW_USER, RETURNING_USER, VIP_USER, etc.
    
    @Column
    private String deviceType; // MOBILE, DESKTOP, TABLET
    
    @Column
    private String browserType;
    
    @Column
    private String location; // Geographic location
    
    @Column
    private Double sessionDurationMinutes;
    
    // Conversion Metrics
    @Column
    private Boolean converted;
    
    @Column
    private Double conversionValue;
    
    @Column
    private String conversionType; // PURCHASE, SIGNUP, SUBSCRIPTION, etc.
    
    // ML Features
    @Column
    private String userBehaviorPattern; // JSON with behavior analysis
    
    @Column
    private Double abandonmentProbability;
    
    @Column
    private String riskFactors; // JSON with risk factors
    
    @Column
    private Boolean isAnomalous;
    
    @Column
    private Double anomalyScore;
    
    @Column
    private String anomalyType; // PERFORMANCE, BEHAVIOR, CONVERSION, etc.
}
