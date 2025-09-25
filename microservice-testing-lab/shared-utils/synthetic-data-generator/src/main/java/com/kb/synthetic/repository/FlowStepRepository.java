package com.kb.synthetic.repository;

import com.kb.synthetic.model.FlowStep;
import com.kb.synthetic.model.enums.StepStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FlowStepRepository extends JpaRepository<FlowStep, Long> {
    
    List<FlowStep> findByStatus(StepStatus status);
    
    List<FlowStep> findByServiceName(String serviceName);
    
    List<FlowStep> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);
    
    List<FlowStep> findByIsAnomalousTrue();
    
    @Query("SELECT fs FROM FlowStep fs WHERE fs.ecommerceFlow.id = :flowId")
    List<FlowStep> findByEcommerceFlowId(@Param("flowId") Long flowId);
    
    @Query("SELECT fs FROM FlowStep fs WHERE fs.stepName = :stepName")
    List<FlowStep> findByStepName(@Param("stepName") String stepName);
    
    @Query("SELECT fs FROM FlowStep fs WHERE fs.responseCode >= 400")
    List<FlowStep> findByErrorResponseCodes();
    
    @Query("SELECT fs FROM FlowStep fs WHERE fs.responseTimeMs > :threshold")
    List<FlowStep> findByHighResponseTime(@Param("threshold") Double threshold);
    
    @Query("SELECT fs FROM FlowStep fs WHERE fs.failureProbability > :threshold")
    List<FlowStep> findByHighFailureProbability(@Param("threshold") Double threshold);
    
    @Query("SELECT fs FROM FlowStep fs WHERE fs.anomalyScore > :threshold")
    List<FlowStep> findByHighAnomalyScore(@Param("threshold") Double threshold);
    
    @Query("SELECT fs FROM FlowStep fs WHERE fs.businessAction = :action")
    List<FlowStep> findByBusinessAction(@Param("action") String action);
    
    @Query("SELECT fs FROM FlowStep fs WHERE fs.errorType = :errorType")
    List<FlowStep> findByErrorType(@Param("errorType") String errorType);
    
    @Query("SELECT fs FROM FlowStep fs WHERE fs.errorCategory = :category")
    List<FlowStep> findByErrorCategory(@Param("category") String category);
    
    @Query("SELECT AVG(fs.responseTimeMs) FROM FlowStep fs WHERE fs.serviceName = :serviceName")
    Double getAverageResponseTimeByService(@Param("serviceName") String serviceName);
    
    @Query("SELECT AVG(fs.responseTimeMs) FROM FlowStep fs WHERE fs.stepName = :stepName")
    Double getAverageResponseTimeByStep(@Param("stepName") String stepName);
    
    @Query("SELECT COUNT(fs) FROM FlowStep fs WHERE fs.status = 'FAILED'")
    Long countFailedSteps();
    
    @Query("SELECT COUNT(fs) FROM FlowStep fs WHERE fs.startTime >= :since")
    Long countByStartTimeAfter(@Param("since") LocalDateTime since);
}
