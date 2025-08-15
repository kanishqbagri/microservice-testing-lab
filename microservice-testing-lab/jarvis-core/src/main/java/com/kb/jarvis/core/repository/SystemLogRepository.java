package com.kb.jarvis.core.repository;

import com.kb.jarvis.core.model.LogLevel;
import com.kb.jarvis.core.model.SystemLog;
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
public interface SystemLogRepository extends JpaRepository<SystemLog, UUID> {
    
    // Find by log level
    List<SystemLog> findByLogLevel(LogLevel logLevel);
    
    // Find by component
    List<SystemLog> findByComponent(String component);
    
    // Find by service name
    List<SystemLog> findByServiceName(String serviceName);
    
    // Find by test name
    List<SystemLog> findByTestName(String testName);
    
    // Find by user ID
    List<SystemLog> findByUserId(String userId);
    
    // Find by session ID
    List<SystemLog> findBySessionId(String sessionId);
    
    // Find by request ID
    List<SystemLog> findByRequestId(String requestId);
    
    // Find by correlation ID
    List<SystemLog> findByCorrelationId(String correlationId);
    
    // Find by timestamp range
    List<SystemLog> findByTimestampBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    // Find by component and log level
    List<SystemLog> findByComponentAndLogLevel(String component, LogLevel logLevel);
    
    // Find by service name and timestamp range
    List<SystemLog> findByServiceNameAndTimestampBetween(String serviceName, LocalDateTime startTime, LocalDateTime endTime);
    
    // Find error logs
    List<SystemLog> findByLogLevelIn(List<LogLevel> errorLevels);
    
    // Find logs with exceptions
    @Query("SELECT s FROM SystemLog s WHERE s.exception IS NOT NULL AND s.exception != ''")
    List<SystemLog> findLogsWithExceptions();
    
    // Find logs with stack traces
    @Query("SELECT s FROM SystemLog s WHERE s.stackTrace IS NOT NULL AND s.stackTrace != ''")
    List<SystemLog> findLogsWithStackTraces();
    
    // Find recent logs
    @Query("SELECT s FROM SystemLog s ORDER BY s.timestamp DESC")
    Page<SystemLog> findRecentLogs(Pageable pageable);
    
    // Find logs by component and recent time
    @Query("SELECT s FROM SystemLog s WHERE s.component = :component AND s.timestamp >= :since ORDER BY s.timestamp DESC")
    List<SystemLog> findRecentLogsByComponent(@Param("component") String component, @Param("since") LocalDateTime since);
    
    // Find logs with context data
    @Query("SELECT s FROM SystemLog s WHERE s.contextData IS NOT NULL")
    List<SystemLog> findLogsWithContextData();
    
    // Find logs with performance metrics
    @Query("SELECT s FROM SystemLog s WHERE s.performanceMetrics IS NOT NULL")
    List<SystemLog> findLogsWithPerformanceMetrics();
    
    // Find logs by tags
    @Query("SELECT s FROM SystemLog s WHERE s.tags LIKE %:tag%")
    List<SystemLog> findByTagContaining(@Param("tag") String tag);
    
    // Find logs with parent log
    List<SystemLog> findByParentLogId(UUID parentLogId);
    
    // Count logs by level
    @Query("SELECT COUNT(s) FROM SystemLog s WHERE s.logLevel = :logLevel")
    Long countByLogLevel(@Param("logLevel") LogLevel logLevel);
    
    // Count logs by component
    @Query("SELECT COUNT(s) FROM SystemLog s WHERE s.component = :component")
    Long countByComponent(@Param("component") String component);
    
    // Count logs by service name
    @Query("SELECT COUNT(s) FROM SystemLog s WHERE s.serviceName = :serviceName")
    Long countByServiceName(@Param("serviceName") String serviceName);
    
    // Get log statistics by level
    @Query("SELECT s.logLevel, COUNT(s) FROM SystemLog s GROUP BY s.logLevel")
    List<Object[]> getLogStatisticsByLevel();
    
    // Get log statistics by component
    @Query("SELECT s.component, COUNT(s) FROM SystemLog s GROUP BY s.component")
    List<Object[]> getLogStatisticsByComponent();
    
    // Get log statistics by service
    @Query("SELECT s.serviceName, COUNT(s) FROM SystemLog s WHERE s.serviceName IS NOT NULL GROUP BY s.serviceName")
    List<Object[]> getLogStatisticsByService();
    
    // Find logs with specific context data
    @Query("SELECT s FROM SystemLog s WHERE s.contextData IS NOT NULL AND s.contextData ? :key")
    List<SystemLog> findLogsWithContextKey(@Param("key") String key);
    
    // Find logs with specific performance metric
    @Query("SELECT s FROM SystemLog s WHERE s.performanceMetrics IS NOT NULL AND s.performanceMetrics ? :metric")
    List<SystemLog> findLogsWithPerformanceMetric(@Param("metric") String metric);
    
    // Find logs for a specific request flow
    @Query("SELECT s FROM SystemLog s WHERE s.correlationId = :correlationId ORDER BY s.timestamp")
    List<SystemLog> findLogsByCorrelationId(@Param("correlationId") String correlationId);
    
    // Find logs for a specific test execution
    @Query("SELECT s FROM SystemLog s WHERE s.testName = :testName AND s.serviceName = :serviceName ORDER BY s.timestamp")
    List<SystemLog> findLogsForTest(@Param("testName") String testName, @Param("serviceName") String serviceName);
    
    // Find logs with high performance impact
    @Query("SELECT s FROM SystemLog s WHERE s.performanceMetrics IS NOT NULL AND s.performanceMetrics ? 'execution_time'")
    List<SystemLog> findLogsWithExecutionTime();
    
    // Find logs for user sessions
    @Query("SELECT s FROM SystemLog s WHERE s.sessionId IS NOT NULL ORDER BY s.sessionId, s.timestamp")
    List<SystemLog> findLogsByUserSessions();
    
    // Find logs for specific time periods
    @Query("SELECT s FROM SystemLog s WHERE s.timestamp >= :startTime AND s.timestamp <= :endTime ORDER BY s.timestamp DESC")
    List<SystemLog> findLogsInTimeRange(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
    
    // Find logs with specific error patterns
    @Query("SELECT s FROM SystemLog s WHERE s.exception LIKE %:pattern%")
    List<SystemLog> findLogsWithErrorPattern(@Param("pattern") String pattern);
    
    // Clean up old logs
    @Query("SELECT s FROM SystemLog s WHERE s.timestamp < :cutoffDate")
    List<SystemLog> findOldLogs(@Param("cutoffDate") LocalDateTime cutoffDate);
}
