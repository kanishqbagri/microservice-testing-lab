package com.kb.jarvis.core.repository;

import com.kb.jarvis.core.model.LearningData;
import com.kb.jarvis.core.model.LearningDataType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface LearningDataRepository extends JpaRepository<LearningData, UUID> {
    
    // Find by data type
    List<LearningData> findByDataType(LearningDataType dataType);
    
    // Find by service name
    List<LearningData> findByServiceName(String serviceName);
    
    // Find by service name and data type
    List<LearningData> findByServiceNameAndDataType(String serviceName, LearningDataType dataType);
    
    // Find by model version
    List<LearningData> findByModelVersion(String modelVersion);
    
    // Find training data
    List<LearningData> findByTrainingUsedTrue();
    
    // Find validation data
    List<LearningData> findByValidationUsedTrue();
    
    // Find by confidence score threshold
    List<LearningData> findByConfidenceScoreGreaterThan(Double threshold);
    
    // Find by date range
    List<LearningData> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // Find by service name and date range
    List<LearningData> findByServiceNameAndCreatedAtBetween(String serviceName, LocalDateTime startDate, LocalDateTime endDate);
    
    // Find data for specific label
    List<LearningData> findByLabel(String label);
    
    // Find data by service name and label
    List<LearningData> findByServiceNameAndLabel(String serviceName, String label);
    
    // Find recent learning data
    @Query("SELECT l FROM LearningData l WHERE l.serviceName = :serviceName ORDER BY l.createdAt DESC")
    Page<LearningData> findRecentByServiceName(@Param("serviceName") String serviceName, Pageable pageable);
    
    // Find high-quality training data (high confidence, used for training)
    @Query("SELECT l FROM LearningData l WHERE l.trainingUsed = true AND l.confidenceScore >= :minConfidence")
    List<LearningData> findHighQualityTrainingData(@Param("minConfidence") Double minConfidence);
    
    // Find data for specific pattern type
    @Query("SELECT l FROM LearningData l WHERE l.testPattern IS NOT NULL AND l.dataType = :dataType")
    List<LearningData> findByPatternType(@Param("dataType") LearningDataType dataType);
    
    // Find data with specific features
    @Query("SELECT l FROM LearningData l WHERE l.featureVector IS NOT NULL AND l.confidenceScore >= :minConfidence")
    List<LearningData> findDataWithFeatures(@Param("minConfidence") Double minConfidence);
    
    // Count data by type for a service
    @Query("SELECT COUNT(l) FROM LearningData l WHERE l.serviceName = :serviceName AND l.dataType = :dataType")
    Long countByServiceNameAndDataType(@Param("serviceName") String serviceName, @Param("dataType") LearningDataType dataType);
    
    // Get average confidence score by service and data type
    @Query("SELECT AVG(l.confidenceScore) FROM LearningData l WHERE l.serviceName = :serviceName AND l.dataType = :dataType")
    Double getAverageConfidenceByServiceAndType(@Param("serviceName") String serviceName, @Param("dataType") LearningDataType dataType);
    
    // Find data for model training (not used for validation)
    @Query("SELECT l FROM LearningData l WHERE l.trainingUsed = true AND l.validationUsed = false AND l.confidenceScore >= :minConfidence")
    List<LearningData> findTrainingData(@Param("minConfidence") Double minConfidence);
    
    // Find data for model validation
    @Query("SELECT l FROM LearningData l WHERE l.validationUsed = true AND l.confidenceScore >= :minConfidence")
    List<LearningData> findValidationData(@Param("minConfidence") Double minConfidence);
    
    // Find data for specific model version
    @Query("SELECT l FROM LearningData l WHERE l.modelVersion = :modelVersion ORDER BY l.createdAt DESC")
    List<LearningData> findByModelVersionOrdered(@Param("modelVersion") String modelVersion);
    
    // Find data with specific metadata
    @Query("SELECT l FROM LearningData l WHERE l.metadata IS NOT NULL")
    List<LearningData> findDataWithMetadata();
    
    // Get learning data statistics
    @Query("SELECT l.dataType, COUNT(l), AVG(l.confidenceScore) FROM LearningData l " +
           "WHERE l.serviceName = :serviceName GROUP BY l.dataType")
    List<Object[]> getLearningDataStatistics(@Param("serviceName") String serviceName);
    
    // Find data for anomaly detection training
    @Query("SELECT l FROM LearningData l WHERE l.dataType = 'ANOMALY_DETECTION' AND l.confidenceScore >= :minConfidence")
    List<LearningData> findAnomalyDetectionData(@Param("minConfidence") Double minConfidence);
    
    // Find data for pattern recognition training
    @Query("SELECT l FROM LearningData l WHERE l.dataType = 'PATTERN_RECOGNITION' AND l.testPattern IS NOT NULL")
    List<LearningData> findPatternRecognitionData();
    
    // Find data for prediction model training
    @Query("SELECT l FROM LearningData l WHERE l.dataType = 'PREDICTION_MODEL' AND l.featureVector IS NOT NULL")
    List<LearningData> findPredictionModelData();
    
    // Clean up old learning data
    @Query("SELECT l FROM LearningData l WHERE l.createdAt < :cutoffDate")
    List<LearningData> findOldData(@Param("cutoffDate") LocalDateTime cutoffDate);
}
