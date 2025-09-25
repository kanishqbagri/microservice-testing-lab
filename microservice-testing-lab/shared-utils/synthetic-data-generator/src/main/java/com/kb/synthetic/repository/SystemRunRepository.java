package com.kb.synthetic.repository;

import com.kb.synthetic.model.PullRequest;
import com.kb.synthetic.model.SystemRun;
import com.kb.synthetic.model.enums.RunStatus;
import com.kb.synthetic.model.enums.RunType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SystemRunRepository extends JpaRepository<SystemRun, Long> {
    
    List<SystemRun> findByStatus(RunStatus status);
    
    List<SystemRun> findByRunType(RunType runType);
    
    List<SystemRun> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);
    
    List<SystemRun> findByHasPerformanceRegressionTrue();
    
    List<SystemRun> findByIsAnomalousTrue();
    
    @Query("SELECT sr FROM SystemRun sr WHERE sr.overallSuccessRate < :threshold")
    List<SystemRun> findByLowSuccessRate(@Param("threshold") Double threshold);
    
    @Query("SELECT sr FROM SystemRun sr WHERE sr.avgResponseTimeMs > :threshold")
    List<SystemRun> findByHighResponseTime(@Param("threshold") Double threshold);
    
    @Query("SELECT sr FROM SystemRun sr WHERE sr.performanceRegressionScore > :threshold")
    List<SystemRun> findByHighPerformanceRegression(@Param("threshold") Double threshold);
    
    @Query("SELECT sr FROM SystemRun sr WHERE sr.anomalyScore > :threshold")
    List<SystemRun> findByHighAnomalyScore(@Param("threshold") Double threshold);
    
    @Query("SELECT sr FROM SystemRun sr WHERE sr.loadIntensity > :threshold")
    List<SystemRun> findByHighLoadIntensity(@Param("threshold") Double threshold);
    
    @Query("SELECT AVG(sr.overallSuccessRate) FROM SystemRun sr")
    Double getAverageSuccessRate();
    
    @Query("SELECT AVG(sr.avgResponseTimeMs) FROM SystemRun sr")
    Double getAverageResponseTime();
    
    @Query("SELECT AVG(sr.throughputRps) FROM SystemRun sr")
    Double getAverageThroughput();
    
    @Query("SELECT COUNT(sr) FROM SystemRun sr WHERE sr.startTime >= :since")
    Long countByStartTimeAfter(@Param("since") LocalDateTime since);
    
    List<SystemRun> findByPullRequest(PullRequest pullRequest);
}
