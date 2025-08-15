package com.kb.jarvis.core.repository;

import com.kb.jarvis.core.model.TestResult;
import com.kb.jarvis.core.model.TestStatus;
import com.kb.jarvis.core.model.TestType;
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
public interface TestResultRepository extends JpaRepository<TestResult, UUID> {
    
    // Find by service name
    List<TestResult> findByServiceName(String serviceName);
    
    // Find by service name and test type
    List<TestResult> findByServiceNameAndTestType(String serviceName, TestType testType);
    
    // Find by service name and status
    List<TestResult> findByServiceNameAndStatus(String serviceName, TestStatus status);
    
    // Find by test type
    List<TestResult> findByTestType(TestType testType);
    
    // Find by status
    List<TestResult> findByStatus(TestStatus status);
    
    // Find by date range
    List<TestResult> findByStartTimeBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // Find by service name and date range
    List<TestResult> findByServiceNameAndStartTimeBetween(String serviceName, LocalDateTime startDate, LocalDateTime endDate);
    
    // Find failed tests
    List<TestResult> findByStatusAndStartTimeAfter(TestStatus status, LocalDateTime after);
    
    // Find tests with execution time greater than threshold
    List<TestResult> findByExecutionTimeMsGreaterThan(Long threshold);
    
    // Find tests with confidence score less than threshold
    List<TestResult> findByConfidenceScoreLessThan(Double threshold);
    
    // Find by tags (using JSONB contains)
    @Query("SELECT t FROM TestResult t WHERE t.tags LIKE %:tag%")
    List<TestResult> findByTagContaining(@Param("tag") String tag);
    
    // Find recent tests for a service
    @Query("SELECT t FROM TestResult t WHERE t.serviceName = :serviceName ORDER BY t.startTime DESC")
    Page<TestResult> findRecentByServiceName(@Param("serviceName") String serviceName, Pageable pageable);
    
    // Find tests with errors
    @Query("SELECT t FROM TestResult t WHERE t.errorMessage IS NOT NULL AND t.errorMessage != ''")
    List<TestResult> findTestsWithErrors();
    
    // Find tests by risk level
    List<TestResult> findByRiskLevel(String riskLevel);
    
    // Count tests by status for a service
    @Query("SELECT COUNT(t) FROM TestResult t WHERE t.serviceName = :serviceName AND t.status = :status")
    Long countByServiceNameAndStatus(@Param("serviceName") String serviceName, @Param("status") TestStatus status);
    
    // Get average execution time by service and test type
    @Query("SELECT AVG(t.executionTimeMs) FROM TestResult t WHERE t.serviceName = :serviceName AND t.testType = :testType")
    Double getAverageExecutionTimeByServiceAndType(@Param("serviceName") String serviceName, @Param("testType") TestType testType);
    
    // Find tests with performance issues (execution time > average + 2*stddev)
    @Query("SELECT t FROM TestResult t WHERE t.serviceName = :serviceName AND t.executionTimeMs > " +
           "(SELECT AVG(tr.executionTimeMs) + 2 * STDDEV(tr.executionTimeMs) FROM TestResult tr WHERE tr.serviceName = :serviceName)")
    List<TestResult> findPerformanceAnomalies(@Param("serviceName") String serviceName);
    
    // Find tests with low confidence scores
    @Query("SELECT t FROM TestResult t WHERE t.confidenceScore < :threshold ORDER BY t.confidenceScore ASC")
    List<TestResult> findLowConfidenceTests(@Param("threshold") Double threshold);
    
    // Get test execution trends
    @Query("SELECT DATE(t.startTime) as date, COUNT(t) as count, AVG(t.executionTimeMs) as avgTime " +
           "FROM TestResult t WHERE t.serviceName = :serviceName AND t.startTime >= :startDate " +
           "GROUP BY DATE(t.startTime) ORDER BY date")
    List<Object[]> getTestExecutionTrends(@Param("serviceName") String serviceName, @Param("startDate") LocalDateTime startDate);
    
    // Find duplicate test patterns
    @Query("SELECT t.testName, COUNT(t) as count FROM TestResult t " +
           "WHERE t.serviceName = :serviceName GROUP BY t.testName HAVING COUNT(t) > 1")
    List<Object[]> findDuplicateTestPatterns(@Param("serviceName") String serviceName);
    
    // Find tests that frequently fail together
    @Query("SELECT t1.testName as test1, t2.testName as test2, COUNT(*) as failureCount " +
           "FROM TestResult t1 JOIN TestResult t2 ON t1.serviceName = t2.serviceName " +
           "WHERE t1.status = 'FAILED' AND t2.status = 'FAILED' " +
           "AND t1.startTime BETWEEN t2.startTime - INTERVAL '5 minutes' AND t2.startTime + INTERVAL '5 minutes' " +
           "AND t1.testName < t2.testName " +
           "GROUP BY t1.testName, t2.testName HAVING COUNT(*) > 1")
    List<Object[]> findFrequentlyFailingTogether();
}
