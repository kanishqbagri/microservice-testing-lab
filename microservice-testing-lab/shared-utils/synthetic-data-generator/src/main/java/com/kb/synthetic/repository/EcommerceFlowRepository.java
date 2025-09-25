package com.kb.synthetic.repository;

import com.kb.synthetic.model.EcommerceFlow;
import com.kb.synthetic.model.SystemRun;
import com.kb.synthetic.model.enums.FlowStatus;
import com.kb.synthetic.model.enums.FlowType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EcommerceFlowRepository extends JpaRepository<EcommerceFlow, Long> {
    
    List<EcommerceFlow> findByFlowType(FlowType flowType);
    
    List<EcommerceFlow> findByStatus(FlowStatus status);
    
    List<EcommerceFlow> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);
    
    List<EcommerceFlow> findByConvertedTrue();
    
    List<EcommerceFlow> findByIsAnomalousTrue();
    
    @Query("SELECT ef FROM EcommerceFlow ef WHERE ef.userId = :userId")
    List<EcommerceFlow> findByUserId(@Param("userId") String userId);
    
    @Query("SELECT ef FROM EcommerceFlow ef WHERE ef.successRate < :threshold")
    List<EcommerceFlow> findByLowSuccessRate(@Param("threshold") Double threshold);
    
    @Query("SELECT ef FROM EcommerceFlow ef WHERE ef.abandonmentProbability > :threshold")
    List<EcommerceFlow> findByHighAbandonmentProbability(@Param("threshold") Double threshold);
    
    @Query("SELECT ef FROM EcommerceFlow ef WHERE ef.anomalyScore > :threshold")
    List<EcommerceFlow> findByHighAnomalyScore(@Param("threshold") Double threshold);
    
    @Query("SELECT ef FROM EcommerceFlow ef WHERE ef.cartValue > :threshold")
    List<EcommerceFlow> findByHighCartValue(@Param("threshold") Double threshold);
    
    @Query("SELECT ef FROM EcommerceFlow ef WHERE ef.finalOrderValue > :threshold")
    List<EcommerceFlow> findByHighOrderValue(@Param("threshold") Double threshold);
    
    @Query("SELECT ef FROM EcommerceFlow ef WHERE ef.userSegment = :segment")
    List<EcommerceFlow> findByUserSegment(@Param("segment") String segment);
    
    @Query("SELECT ef FROM EcommerceFlow ef WHERE ef.deviceType = :deviceType")
    List<EcommerceFlow> findByDeviceType(@Param("deviceType") String deviceType);
    
    @Query("SELECT ef FROM EcommerceFlow ef WHERE ef.location = :location")
    List<EcommerceFlow> findByLocation(@Param("location") String location);
    
    @Query("SELECT AVG(ef.successRate) FROM EcommerceFlow ef")
    Double getAverageSuccessRate();
    
    @Query("SELECT AVG(ef.conversionValue) FROM EcommerceFlow ef WHERE ef.converted = true")
    Double getAverageConversionValue();
    
    @Query("SELECT AVG(ef.cartValue) FROM EcommerceFlow ef")
    Double getAverageCartValue();
    
    @Query("SELECT AVG(ef.finalOrderValue) FROM EcommerceFlow ef")
    Double getAverageOrderValue();
    
    @Query("SELECT COUNT(ef) FROM EcommerceFlow ef WHERE ef.converted = true")
    Long countConvertedFlows();
    
    @Query("SELECT COUNT(ef) FROM EcommerceFlow ef WHERE ef.startTime >= :since")
    Long countByStartTimeAfter(@Param("since") LocalDateTime since);
    
    List<EcommerceFlow> findBySystemRun(SystemRun systemRun);
}
