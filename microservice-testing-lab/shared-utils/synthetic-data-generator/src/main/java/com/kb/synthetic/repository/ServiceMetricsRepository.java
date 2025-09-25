package com.kb.synthetic.repository;

import com.kb.synthetic.model.ServiceMetrics;
import com.kb.synthetic.model.SystemRun;
import com.kb.synthetic.model.enums.ServiceHealthStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ServiceMetricsRepository extends JpaRepository<ServiceMetrics, Long> {
    
    List<ServiceMetrics> findByServiceName(String serviceName);
    
    List<ServiceMetrics> findByHealthStatus(ServiceHealthStatus healthStatus);
    
    List<ServiceMetrics> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    
    List<ServiceMetrics> findByIsAnomalousTrue();
    
    @Query("SELECT sm FROM ServiceMetrics sm WHERE sm.serviceName = :serviceName AND sm.timestamp >= :since")
    List<ServiceMetrics> findByServiceNameAndTimestampAfter(@Param("serviceName") String serviceName, @Param("since") LocalDateTime since);
    
    @Query("SELECT sm FROM ServiceMetrics sm WHERE sm.successRate < :threshold")
    List<ServiceMetrics> findByLowSuccessRate(@Param("threshold") Double threshold);
    
    @Query("SELECT sm FROM ServiceMetrics sm WHERE sm.avgResponseTimeMs > :threshold")
    List<ServiceMetrics> findByHighResponseTime(@Param("threshold") Double threshold);
    
    @Query("SELECT sm FROM ServiceMetrics sm WHERE sm.cpuUsagePercent > :threshold")
    List<ServiceMetrics> findByHighCpuUsage(@Param("threshold") Double threshold);
    
    @Query("SELECT sm FROM ServiceMetrics sm WHERE sm.memoryUsagePercent > :threshold")
    List<ServiceMetrics> findByHighMemoryUsage(@Param("threshold") Double threshold);
    
    @Query("SELECT sm FROM ServiceMetrics sm WHERE sm.anomalyScore > :threshold")
    List<ServiceMetrics> findByHighAnomalyScore(@Param("threshold") Double threshold);
    
    @Query("SELECT sm FROM ServiceMetrics sm WHERE sm.predictedFailureProbability > :threshold")
    List<ServiceMetrics> findByHighFailureProbability(@Param("threshold") Double threshold);
    
    @Query("SELECT AVG(sm.successRate) FROM ServiceMetrics sm WHERE sm.serviceName = :serviceName")
    Double getAverageSuccessRateByService(@Param("serviceName") String serviceName);
    
    @Query("SELECT AVG(sm.avgResponseTimeMs) FROM ServiceMetrics sm WHERE sm.serviceName = :serviceName")
    Double getAverageResponseTimeByService(@Param("serviceName") String serviceName);
    
    @Query("SELECT AVG(sm.cpuUsagePercent) FROM ServiceMetrics sm WHERE sm.serviceName = :serviceName")
    Double getAverageCpuUsageByService(@Param("serviceName") String serviceName);
    
    @Query("SELECT AVG(sm.memoryUsagePercent) FROM ServiceMetrics sm WHERE sm.serviceName = :serviceName")
    Double getAverageMemoryUsageByService(@Param("serviceName") String serviceName);
    
    @Query("SELECT COUNT(sm) FROM ServiceMetrics sm WHERE sm.serviceName = :serviceName AND sm.timestamp >= :since")
    Long countByServiceNameAndTimestampAfter(@Param("serviceName") String serviceName, @Param("since") LocalDateTime since);
    
    List<ServiceMetrics> findBySystemRun(SystemRun systemRun);
}
