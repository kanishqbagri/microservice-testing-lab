package com.kb.synthetic.repository;

import com.kb.synthetic.model.PullRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PullRequestRepository extends JpaRepository<PullRequest, Long> {
    
    List<PullRequest> findByStatus(String status);
    
    List<PullRequest> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    
    List<PullRequest> findByHasPerformanceRegressionTrue();
    
    List<PullRequest> findByHasSecurityVulnerabilityTrue();
    
    List<PullRequest> findByHasBreakingChangeTrue();
    
    @Query("SELECT pr FROM PullRequest pr WHERE pr.riskScore > :threshold")
    List<PullRequest> findByHighRiskScore(@Param("threshold") Double threshold);
    
    @Query("SELECT pr FROM PullRequest pr WHERE pr.performanceImpactScore > :threshold")
    List<PullRequest> findByHighPerformanceImpact(@Param("threshold") Double threshold);
    
    @Query("SELECT pr FROM PullRequest pr WHERE pr.affectedServices LIKE %:serviceName%")
    List<PullRequest> findByAffectedService(@Param("serviceName") String serviceName);
    
    @Query("SELECT pr FROM PullRequest pr WHERE pr.changeTypes LIKE %:changeType%")
    List<PullRequest> findByChangeType(@Param("changeType") String changeType);
    
    @Query("SELECT COUNT(pr) FROM PullRequest pr WHERE pr.createdAt >= :since")
    Long countByCreatedAtAfter(@Param("since") LocalDateTime since);
    
    @Query("SELECT AVG(pr.performanceImpactScore) FROM PullRequest pr")
    Double getAveragePerformanceImpact();
    
    @Query("SELECT AVG(pr.riskScore) FROM PullRequest pr")
    Double getAverageRiskScore();
}
