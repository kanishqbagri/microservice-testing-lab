package com.kb.jarvis.core.service;

import com.kb.jarvis.core.model.TestResult;
import com.kb.jarvis.core.model.TestStatus;
import com.kb.jarvis.core.model.TestType;
import com.kb.jarvis.core.model.RiskLevel;
import com.kb.jarvis.core.repository.TestResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

// @Service
// @Transactional
public class TestResultService {

    @Autowired
    private TestResultRepository testResultRepository;

    /**
     * Save a test result to the database
     */
    public TestResult saveTestResult(TestResult testResult) {
        // Set timestamps if not already set
        if (testResult.getCreatedAt() == null) {
            testResult.setCreatedAt(LocalDateTime.now());
        }
        testResult.setUpdatedAt(LocalDateTime.now());
        
        return testResultRepository.save(testResult);
    }

    /**
     * Create and save a test result with builder pattern
     */
    public TestResult createAndSaveTestResult(String testName, String serviceName, TestType testType, 
                                            TestStatus status, Long executionTimeMs, 
                                            Map<String, Object> testParameters, Map<String, Object> testOutput,
                                            Map<String, Object> performanceMetrics, Map<String, Object> environmentInfo,
                                            Double confidenceScore, RiskLevel riskLevel, String tags) {
        
        TestResult testResult = TestResult.builder()
            .testName(testName)
            .serviceName(serviceName)
            .testType(testType)
            .status(status)
            .executionTimeMs(executionTimeMs)
            .startTime(LocalDateTime.now().minusNanos((executionTimeMs != null ? executionTimeMs : 0) * 1_000_000))
            .endTime(LocalDateTime.now())
            .testParameters(testParameters)
            .testOutput(testOutput)
            .performanceMetrics(performanceMetrics)
            .environmentInfo(environmentInfo)
            .confidenceScore(confidenceScore)
            .riskLevel(riskLevel)
            .tags(tags)
            .build();
        
        return saveTestResult(testResult);
    }

    /**
     * Find test result by ID
     */
    @Transactional(readOnly = true)
    public Optional<TestResult> findById(UUID id) {
        return testResultRepository.findById(id);
    }

    /**
     * Find all test results
     */
    @Transactional(readOnly = true)
    public List<TestResult> findAll() {
        return testResultRepository.findAll();
    }

    /**
     * Find test results by service name
     */
    @Transactional(readOnly = true)
    public List<TestResult> findByServiceName(String serviceName) {
        return testResultRepository.findByServiceName(serviceName);
    }

    /**
     * Find test results by service name and test type
     */
    @Transactional(readOnly = true)
    public List<TestResult> findByServiceNameAndTestType(String serviceName, TestType testType) {
        return testResultRepository.findByServiceNameAndTestType(serviceName, testType);
    }

    /**
     * Find test results by service name and status
     */
    @Transactional(readOnly = true)
    public List<TestResult> findByServiceNameAndStatus(String serviceName, TestStatus status) {
        return testResultRepository.findByServiceNameAndStatus(serviceName, status);
    }

    /**
     * Find test results by test type
     */
    @Transactional(readOnly = true)
    public List<TestResult> findByTestType(TestType testType) {
        return testResultRepository.findByTestType(testType);
    }

    /**
     * Find test results by status
     */
    @Transactional(readOnly = true)
    public List<TestResult> findByStatus(TestStatus status) {
        return testResultRepository.findByStatus(status);
    }

    /**
     * Find test results by date range
     */
    @Transactional(readOnly = true)
    public List<TestResult> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return testResultRepository.findByStartTimeBetween(startDate, endDate);
    }

    /**
     * Find failed tests
     */
    @Transactional(readOnly = true)
    public List<TestResult> findFailedTests(LocalDateTime after) {
        return testResultRepository.findByStatusAndStartTimeAfter(TestStatus.FAILED, after);
    }

    /**
     * Find tests with execution time greater than threshold
     */
    @Transactional(readOnly = true)
    public List<TestResult> findSlowTests(Long thresholdMs) {
        return testResultRepository.findByExecutionTimeMsGreaterThan(thresholdMs);
    }

    /**
     * Find tests with low confidence scores
     */
    @Transactional(readOnly = true)
    public List<TestResult> findLowConfidenceTests(Double threshold) {
        return testResultRepository.findByConfidenceScoreLessThan(threshold);
    }

    /**
     * Find tests with errors
     */
    @Transactional(readOnly = true)
    public List<TestResult> findTestsWithErrors() {
        return testResultRepository.findTestsWithErrors();
    }

    /**
     * Find performance anomalies for a service
     */
    @Transactional(readOnly = true)
    public List<TestResult> findPerformanceAnomalies(String serviceName) {
        return testResultRepository.findPerformanceAnomalies(serviceName);
    }

    /**
     * Get recent test results for a service
     */
    @Transactional(readOnly = true)
    public Page<TestResult> getRecentTestResults(String serviceName, Pageable pageable) {
        return testResultRepository.findRecentByServiceName(serviceName, pageable);
    }

    /**
     * Count tests by status for a service
     */
    @Transactional(readOnly = true)
    public Long countByServiceNameAndStatus(String serviceName, TestStatus status) {
        return testResultRepository.countByServiceNameAndStatus(serviceName, status);
    }

    /**
     * Get average execution time by service and test type
     */
    @Transactional(readOnly = true)
    public Double getAverageExecutionTime(String serviceName, TestType testType) {
        return testResultRepository.getAverageExecutionTimeByServiceAndType(serviceName, testType);
    }

    /**
     * Get test statistics for a service
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getServiceStatistics(String serviceName) {
        Long totalTests = testResultRepository.countByServiceNameAndStatus(serviceName, TestStatus.PASSED) +
                         testResultRepository.countByServiceNameAndStatus(serviceName, TestStatus.FAILED) +
                         testResultRepository.countByServiceNameAndStatus(serviceName, TestStatus.SKIPPED);
        
        Long passedTests = testResultRepository.countByServiceNameAndStatus(serviceName, TestStatus.PASSED);
        Long failedTests = testResultRepository.countByServiceNameAndStatus(serviceName, TestStatus.FAILED);
        
        double successRate = totalTests > 0 ? (double) passedTests / totalTests * 100 : 0;
        
        return Map.of(
            "totalTests", totalTests,
            "passedTests", passedTests,
            "failedTests", failedTests,
            "successRate", successRate,
            "serviceName", serviceName
        );
    }

    /**
     * Get test statistics by test type
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getTestTypeStatistics(TestType testType) {
        List<TestResult> allTests = testResultRepository.findByTestType(testType);
        List<TestResult> passedTests = testResultRepository.findByStatus(TestStatus.PASSED).stream()
            .filter(t -> t.getTestType() == testType)
            .toList();
        List<TestResult> failedTests = testResultRepository.findByStatus(TestStatus.FAILED).stream()
            .filter(t -> t.getTestType() == testType)
            .toList();
        
        double successRate = allTests.size() > 0 ? (double) passedTests.size() / allTests.size() * 100 : 0;
        
        // Calculate average execution time
        double avgExecutionTime = allTests.stream()
            .filter(t -> t.getExecutionTimeMs() != null)
            .mapToLong(TestResult::getExecutionTimeMs)
            .average()
            .orElse(0.0);
        
        return Map.of(
            "testType", testType.toString(),
            "totalTests", allTests.size(),
            "passedTests", passedTests.size(),
            "failedTests", failedTests.size(),
            "successRate", successRate,
            "averageExecutionTime", avgExecutionTime
        );
    }

    /**
     * Delete test result by ID
     */
    public void deleteTestResult(UUID id) {
        testResultRepository.deleteById(id);
    }

    /**
     * Delete all test results for a service
     */
    public void deleteTestResultsByService(String serviceName) {
        List<TestResult> results = testResultRepository.findByServiceName(serviceName);
        testResultRepository.deleteAll(results);
    }

    /**
     * Delete test results older than specified date
     */
    public void deleteOldTestResults(LocalDateTime before) {
        List<TestResult> oldResults = testResultRepository.findByStartTimeBetween(
            LocalDateTime.of(1970, 1, 1, 0, 0), before);
        testResultRepository.deleteAll(oldResults);
    }

    /**
     * Update test result
     */
    public TestResult updateTestResult(TestResult testResult) {
        testResult.setUpdatedAt(LocalDateTime.now());
        return testResultRepository.save(testResult);
    }

    /**
     * Get total count of test results
     */
    @Transactional(readOnly = true)
    public long getTotalCount() {
        return testResultRepository.count();
    }

    /**
     * Get count of passed tests
     */
    @Transactional(readOnly = true)
    public long getPassedCount() {
        return testResultRepository.findByStatus(TestStatus.PASSED).size();
    }

    /**
     * Get count of failed tests
     */
    @Transactional(readOnly = true)
    public long getFailedCount() {
        return testResultRepository.findByStatus(TestStatus.FAILED).size();
    }
}
