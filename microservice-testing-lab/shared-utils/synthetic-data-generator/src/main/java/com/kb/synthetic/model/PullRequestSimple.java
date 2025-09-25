package com.kb.synthetic.model;

import com.kb.synthetic.model.enums.PRStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "synthetic_pull_requests")
public class PullRequestSimple {
    
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
    
    // ML Features
    @Column
    private String affectedServices;
    
    @Column
    private String changeTypes;
    
    @Column
    private Double riskScore;
    
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
    
    // Constructors
    public PullRequestSimple() {}
    
    public PullRequestSimple(String prNumber, String title, String description, String author, 
                           String targetBranch, String sourceBranch, PRStatus status) {
        this.prNumber = prNumber;
        this.title = title;
        this.description = description;
        this.author = author;
        this.targetBranch = targetBranch;
        this.sourceBranch = sourceBranch;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getPrNumber() { return prNumber; }
    public void setPrNumber(String prNumber) { this.prNumber = prNumber; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    
    public String getTargetBranch() { return targetBranch; }
    public void setTargetBranch(String targetBranch) { this.targetBranch = targetBranch; }
    
    public String getSourceBranch() { return sourceBranch; }
    public void setSourceBranch(String sourceBranch) { this.sourceBranch = sourceBranch; }
    
    public PRStatus getStatus() { return status; }
    public void setStatus(PRStatus status) { this.status = status; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getMergedAt() { return mergedAt; }
    public void setMergedAt(LocalDateTime mergedAt) { this.mergedAt = mergedAt; }
    
    public LocalDateTime getClosedAt() { return closedAt; }
    public void setClosedAt(LocalDateTime closedAt) { this.closedAt = closedAt; }
    
    public Integer getLinesAdded() { return linesAdded; }
    public void setLinesAdded(Integer linesAdded) { this.linesAdded = linesAdded; }
    
    public Integer getLinesDeleted() { return linesDeleted; }
    public void setLinesDeleted(Integer linesDeleted) { this.linesDeleted = linesDeleted; }
    
    public Integer getFilesChanged() { return filesChanged; }
    public void setFilesChanged(Integer filesChanged) { this.filesChanged = filesChanged; }
    
    public Double getTestCoverage() { return testCoverage; }
    public void setTestCoverage(Double testCoverage) { this.testCoverage = testCoverage; }
    
    public Double getCodeComplexity() { return codeComplexity; }
    public void setCodeComplexity(Double codeComplexity) { this.codeComplexity = codeComplexity; }
    
    public Integer getCyclomaticComplexity() { return cyclomaticComplexity; }
    public void setCyclomaticComplexity(Integer cyclomaticComplexity) { this.cyclomaticComplexity = cyclomaticComplexity; }
    
    public Double getPerformanceImpactScore() { return performanceImpactScore; }
    public void setPerformanceImpactScore(Double performanceImpactScore) { this.performanceImpactScore = performanceImpactScore; }
    
    public Long getAvgResponseTimeMs() { return avgResponseTimeMs; }
    public void setAvgResponseTimeMs(Long avgResponseTimeMs) { this.avgResponseTimeMs = avgResponseTimeMs; }
    
    public Double getErrorRateIncrease() { return errorRateIncrease; }
    public void setErrorRateIncrease(Double errorRateIncrease) { this.errorRateIncrease = errorRateIncrease; }
    
    public Double getMemoryUsageIncrease() { return memoryUsageIncrease; }
    public void setMemoryUsageIncrease(Double memoryUsageIncrease) { this.memoryUsageIncrease = memoryUsageIncrease; }
    
    public Double getCpuUsageIncrease() { return cpuUsageIncrease; }
    public void setCpuUsageIncrease(Double cpuUsageIncrease) { this.cpuUsageIncrease = cpuUsageIncrease; }
    
    public Map<String, Double> getServiceImpactScores() { return serviceImpactScores; }
    public void setServiceImpactScores(Map<String, Double> serviceImpactScores) { this.serviceImpactScores = serviceImpactScores; }
    
    public String getAffectedServices() { return affectedServices; }
    public void setAffectedServices(String affectedServices) { this.affectedServices = affectedServices; }
    
    public String getChangeTypes() { return changeTypes; }
    public void setChangeTypes(String changeTypes) { this.changeTypes = changeTypes; }
    
    public Double getRiskScore() { return riskScore; }
    public void setRiskScore(Double riskScore) { this.riskScore = riskScore; }
    
    public Boolean getHasPerformanceRegression() { return hasPerformanceRegression; }
    public void setHasPerformanceRegression(Boolean hasPerformanceRegression) { this.hasPerformanceRegression = hasPerformanceRegression; }
    
    public Boolean getHasSecurityVulnerability() { return hasSecurityVulnerability; }
    public void setHasSecurityVulnerability(Boolean hasSecurityVulnerability) { this.hasSecurityVulnerability = hasSecurityVulnerability; }
    
    public Boolean getHasBreakingChange() { return hasBreakingChange; }
    public void setHasBreakingChange(Boolean hasBreakingChange) { this.hasBreakingChange = hasBreakingChange; }
}
