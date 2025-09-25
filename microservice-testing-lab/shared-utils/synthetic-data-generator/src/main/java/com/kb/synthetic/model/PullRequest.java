package com.kb.synthetic.model;

import com.kb.synthetic.model.enums.PRStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "synthetic_pull_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PullRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String prNumber;
    
    @Column(nullable = false)
    private String title;
    
    @Column(length = 2000)
    private String description;
    
    @Column(nullable = false)
    private String author;
    
    @Column(nullable = false)
    private String targetBranch;
    
    @Column(nullable = false)
    private String sourceBranch;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PRStatus status;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column
    private LocalDateTime mergedAt;
    
    @Column
    private LocalDateTime closedAt;
    
    // Code Quality Metrics
    @Column
    private Integer linesAdded;
    
    @Column
    private Integer linesDeleted;
    
    @Column
    private Integer filesChanged;
    
    @Column
    private Double testCoverage;
    
    @Column
    private Double codeComplexity;
    
    @Column
    private Integer cyclomaticComplexity;
    
    // Performance Impact Metrics
    @Column
    private Double performanceImpactScore;
    
    @Column
    private Long avgResponseTimeMs;
    
    @Column
    private Double errorRateIncrease;
    
    @Column
    private Double memoryUsageIncrease;
    
    @Column
    private Double cpuUsageIncrease;
    
    // Service Impact
    @ElementCollection
    @CollectionTable(name = "pr_service_impact", joinColumns = @JoinColumn(name = "pr_id"))
    @MapKeyColumn(name = "service_name")
    @Column(name = "impact_score")
    private Map<String, Double> serviceImpactScores;
    
    // Test Results
    @OneToMany(mappedBy = "pullRequest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SystemRun> systemRuns;
    
    // ML Features
    @Column
    private String affectedServices; // JSON array of service names
    
    @Column
    private String changeTypes; // JSON array: [API_CHANGE, DB_CHANGE, CONFIG_CHANGE, etc.]
    
    @Column
    private Double riskScore; // Calculated risk score for ML
    
    @Column
    private Boolean hasPerformanceRegression;
    
    @Column
    private Boolean hasSecurityVulnerability;
    
    @Column
    private Boolean hasBreakingChange;
    
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
