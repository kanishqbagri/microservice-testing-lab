package com.kb.jarvis.core.controller;

import com.kb.jarvis.core.model.TestResult;
import com.kb.jarvis.core.model.TestStatus;
import com.kb.jarvis.core.model.TestType;
import com.kb.jarvis.core.model.RiskLevel;
import com.kb.jarvis.core.service.TestResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

// @RestController
// @RequestMapping("/api/dashboard")
// @CrossOrigin(origins = "*")
public class DashboardController {

    @Autowired
    private TestResultService testResultService;

    @GetMapping("/overview")
    public ResponseEntity<Map<String, Object>> getDashboardOverview() {
        Map<String, Object> overview = new HashMap<>();
        
        // Basic statistics
        overview.put("totalTests", testResultService.getTotalCount());
        overview.put("passedTests", testResultService.getPassedCount());
        overview.put("failedTests", testResultService.getFailedCount());
        overview.put("successRate", calculateSuccessRate());
        
        // Recent activity (last 24 hours)
        LocalDateTime last24Hours = LocalDateTime.now().minusHours(24);
        List<TestResult> recentTests = testResultService.findByDateRange(last24Hours, LocalDateTime.now());
        overview.put("recentTests", recentTests.size());
        overview.put("recentPassed", recentTests.stream().filter(t -> t.getStatus() == TestStatus.PASSED).count());
        overview.put("recentFailed", recentTests.stream().filter(t -> t.getStatus() == TestStatus.FAILED).count());
        
        // Services overview
        List<String> services = Arrays.asList("user-service", "product-service", "order-service", "notification-service", "gateway-service");
        Map<String, Map<String, Object>> serviceStats = new HashMap<>();
        for (String service : services) {
            Map<String, Object> stats = testResultService.getServiceStatistics(service);
            serviceStats.put(service, stats);
        }
        overview.put("serviceStats", serviceStats);
        
        // Test types overview
        Map<String, Map<String, Object>> testTypeStats = new HashMap<>();
        for (TestType testType : TestType.values()) {
            Map<String, Object> stats = testResultService.getTestTypeStatistics(testType);
            testTypeStats.put(testType.toString(), stats);
        }
        overview.put("testTypeStats", testTypeStats);
        
        overview.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.ok(overview);
    }

    @GetMapping("/test-results")
    public ResponseEntity<Map<String, Object>> getTestResults(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String service,
            @RequestParam(required = false) String testType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        Map<String, Object> response = new HashMap<>();
        
        // Get filtered test results
        List<TestResult> allTests = testResultService.findAll();
        
        // Apply filters
        List<TestResult> filteredTests = allTests.stream()
            .filter(test -> service == null || test.getServiceName().equals(service))
            .filter(test -> testType == null || test.getTestType().toString().equals(testType))
            .filter(test -> status == null || test.getStatus().toString().equals(status))
            .filter(test -> startDate == null || test.getStartTime().isAfter(startDate))
            .filter(test -> endDate == null || test.getStartTime().isBefore(endDate))
            .sorted((a, b) -> b.getStartTime().compareTo(a.getStartTime()))
            .collect(Collectors.toList());
        
        // Pagination
        int total = filteredTests.size();
        int start = page * size;
        int end = Math.min(start + size, total);
        
        List<TestResult> paginatedTests = start < total ? filteredTests.subList(start, end) : new ArrayList<>();
        
        response.put("tests", paginatedTests.stream().map(this::convertToMap).collect(Collectors.toList()));
        response.put("totalElements", total);
        response.put("totalPages", (int) Math.ceil((double) total / size));
        response.put("currentPage", page);
        response.put("size", size);
        response.put("hasNext", end < total);
        response.put("hasPrevious", page > 0);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/trends")
    public ResponseEntity<Map<String, Object>> getTestTrends(
            @RequestParam(defaultValue = "7") int days) {
        
        Map<String, Object> trends = new HashMap<>();
        
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(days);
        
        // Get test results for the period
        List<TestResult> tests = testResultService.findByDateRange(startDate, endDate);
        
        // Group by date
        Map<String, Map<String, Long>> dailyStats = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        for (TestResult test : tests) {
            String date = test.getStartTime().format(formatter);
            dailyStats.computeIfAbsent(date, k -> new HashMap<>())
                .merge(test.getStatus().toString(), 1L, Long::sum);
        }
        
        // Create trend data
        List<Map<String, Object>> trendData = new ArrayList<>();
        for (int i = days - 1; i >= 0; i--) {
            String date = endDate.minusDays(i).format(formatter);
            Map<String, Object> dayData = new HashMap<>();
            dayData.put("date", date);
            dayData.put("passed", dailyStats.getOrDefault(date, new HashMap<>()).getOrDefault("PASSED", 0L));
            dayData.put("failed", dailyStats.getOrDefault(date, new HashMap<>()).getOrDefault("FAILED", 0L));
            dayData.put("skipped", dailyStats.getOrDefault(date, new HashMap<>()).getOrDefault("SKIPPED", 0L));
            dayData.put("total", dailyStats.getOrDefault(date, new HashMap<>()).values().stream().mapToLong(Long::longValue).sum());
            trendData.add(dayData);
        }
        
        trends.put("trendData", trendData);
        trends.put("period", days + " days");
        trends.put("startDate", startDate);
        trends.put("endDate", endDate);
        
        return ResponseEntity.ok(trends);
    }

    @GetMapping("/performance-metrics")
    public ResponseEntity<Map<String, Object>> getPerformanceMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        
        List<TestResult> allTests = testResultService.findAll();
        
        // Overall performance metrics
        double avgExecutionTime = allTests.stream()
            .filter(t -> t.getExecutionTimeMs() != null)
            .mapToLong(TestResult::getExecutionTimeMs)
            .average()
            .orElse(0.0);
        
        long maxExecutionTime = allTests.stream()
            .filter(t -> t.getExecutionTimeMs() != null)
            .mapToLong(TestResult::getExecutionTimeMs)
            .max()
            .orElse(0L);
        
        long minExecutionTime = allTests.stream()
            .filter(t -> t.getExecutionTimeMs() != null)
            .mapToLong(TestResult::getExecutionTimeMs)
            .min()
            .orElse(0L);
        
        metrics.put("averageExecutionTime", avgExecutionTime);
        metrics.put("maxExecutionTime", maxExecutionTime);
        metrics.put("minExecutionTime", minExecutionTime);
        
        // Performance by service
        Map<String, Map<String, Object>> servicePerformance = new HashMap<>();
        List<String> services = Arrays.asList("user-service", "product-service", "order-service", "notification-service", "gateway-service");
        
        for (String service : services) {
            List<TestResult> serviceTests = testResultService.findByServiceName(service);
            Map<String, Object> serviceMetrics = new HashMap<>();
            
            double serviceAvgTime = serviceTests.stream()
                .filter(t -> t.getExecutionTimeMs() != null)
                .mapToLong(TestResult::getExecutionTimeMs)
                .average()
                .orElse(0.0);
            
            long serviceMaxTime = serviceTests.stream()
                .filter(t -> t.getExecutionTimeMs() != null)
                .mapToLong(TestResult::getExecutionTimeMs)
                .max()
                .orElse(0L);
            
            serviceMetrics.put("averageTime", serviceAvgTime);
            serviceMetrics.put("maxTime", serviceMaxTime);
            serviceMetrics.put("totalTests", serviceTests.size());
            servicePerformance.put(service, serviceMetrics);
        }
        
        metrics.put("servicePerformance", servicePerformance);
        
        // Performance by test type
        Map<String, Map<String, Object>> testTypePerformance = new HashMap<>();
        for (TestType testType : TestType.values()) {
            List<TestResult> typeTests = testResultService.findByTestType(testType);
            Map<String, Object> typeMetrics = new HashMap<>();
            
            double typeAvgTime = typeTests.stream()
                .filter(t -> t.getExecutionTimeMs() != null)
                .mapToLong(TestResult::getExecutionTimeMs)
                .average()
                .orElse(0.0);
            
            long typeMaxTime = typeTests.stream()
                .filter(t -> t.getExecutionTimeMs() != null)
                .mapToLong(TestResult::getExecutionTimeMs)
                .max()
                .orElse(0L);
            
            typeMetrics.put("averageTime", typeAvgTime);
            typeMetrics.put("maxTime", typeMaxTime);
            typeMetrics.put("totalTests", typeTests.size());
            testTypePerformance.put(testType.toString(), typeMetrics);
        }
        
        metrics.put("testTypePerformance", testTypePerformance);
        
        // Slow tests
        List<TestResult> slowTests = testResultService.findSlowTests(1000L);
        metrics.put("slowTests", slowTests.stream().map(this::convertToMap).collect(Collectors.toList()));
        
        return ResponseEntity.ok(metrics);
    }

    @GetMapping("/failure-analysis")
    public ResponseEntity<Map<String, Object>> getFailureAnalysis() {
        Map<String, Object> analysis = new HashMap<>();
        
        List<TestResult> failedTests = testResultService.findByStatus(TestStatus.FAILED);
        
        // Failure patterns
        Map<String, Long> failurePatterns = failedTests.stream()
            .filter(t -> t.getErrorMessage() != null)
            .collect(Collectors.groupingBy(
                t -> extractFailurePattern(t.getErrorMessage()),
                Collectors.counting()
            ));
        
        analysis.put("failurePatterns", failurePatterns);
        
        // Failures by service
        Map<String, Long> failuresByService = failedTests.stream()
            .collect(Collectors.groupingBy(TestResult::getServiceName, Collectors.counting()));
        analysis.put("failuresByService", failuresByService);
        
        // Failures by test type
        Map<String, Long> failuresByType = failedTests.stream()
            .collect(Collectors.groupingBy(t -> t.getTestType().toString(), Collectors.counting()));
        analysis.put("failuresByType", failuresByType);
        
        // Recent failures
        List<TestResult> recentFailures = failedTests.stream()
            .sorted((a, b) -> b.getStartTime().compareTo(a.getStartTime()))
            .limit(10)
            .collect(Collectors.toList());
        analysis.put("recentFailures", recentFailures.stream().map(this::convertToMap).collect(Collectors.toList()));
        
        // Failure trends (last 7 days)
        LocalDateTime last7Days = LocalDateTime.now().minusDays(7);
        List<TestResult> recentFailedTests = testResultService.findByStatus(TestStatus.FAILED).stream()
            .filter(t -> t.getStartTime().isAfter(last7Days))
            .collect(Collectors.toList());
        analysis.put("recentFailureCount", recentFailedTests.size());
        
        return ResponseEntity.ok(analysis);
    }

    @GetMapping("/security-dashboard")
    public ResponseEntity<Map<String, Object>> getSecurityDashboard() {
        Map<String, Object> security = new HashMap<>();
        
        List<TestResult> securityTests = testResultService.findByTestType(TestType.SECURITY_TEST);
        
        // Security test statistics
        long totalSecurityTests = securityTests.size();
        long passedSecurityTests = securityTests.stream()
            .filter(t -> t.getStatus() == TestStatus.PASSED)
            .count();
        long failedSecurityTests = securityTests.stream()
            .filter(t -> t.getStatus() == TestStatus.FAILED)
            .count();
        
        double securityScore = totalSecurityTests > 0 ? (double) passedSecurityTests / totalSecurityTests * 100 : 0;
        
        security.put("totalSecurityTests", totalSecurityTests);
        security.put("passedSecurityTests", passedSecurityTests);
        security.put("failedSecurityTests", failedSecurityTests);
        security.put("securityScore", securityScore);
        
        // Risk level distribution
        Map<String, Long> riskDistribution = securityTests.stream()
            .filter(t -> t.getRiskLevel() != null)
            .collect(Collectors.groupingBy(t -> t.getRiskLevel().toString(), Collectors.counting()));
        security.put("riskDistribution", riskDistribution);
        
        // Security tests by service
        Map<String, List<TestResult>> securityByService = securityTests.stream()
            .collect(Collectors.groupingBy(TestResult::getServiceName));
        
        Map<String, Map<String, Object>> serviceSecurityStats = new HashMap<>();
        for (Map.Entry<String, List<TestResult>> entry : securityByService.entrySet()) {
            String service = entry.getKey();
            List<TestResult> serviceSecurityTests = entry.getValue();
            
            Map<String, Object> stats = new HashMap<>();
            stats.put("total", serviceSecurityTests.size());
            stats.put("passed", serviceSecurityTests.stream().filter(t -> t.getStatus() == TestStatus.PASSED).count());
            stats.put("failed", serviceSecurityTests.stream().filter(t -> t.getStatus() == TestStatus.FAILED).count());
            stats.put("successRate", serviceSecurityTests.size() > 0 ? 
                (double) serviceSecurityTests.stream().filter(t -> t.getStatus() == TestStatus.PASSED).count() / serviceSecurityTests.size() * 100 : 0);
            
            serviceSecurityStats.put(service, stats);
        }
        security.put("serviceSecurityStats", serviceSecurityStats);
        
        // Recent security tests
        List<TestResult> recentSecurityTests = securityTests.stream()
            .sorted((a, b) -> b.getStartTime().compareTo(a.getStartTime()))
            .limit(10)
            .collect(Collectors.toList());
        security.put("recentSecurityTests", recentSecurityTests.stream().map(this::convertToMap).collect(Collectors.toList()));
        
        return ResponseEntity.ok(security);
    }

    @GetMapping("/export")
    public ResponseEntity<Map<String, Object>> exportTestResults(
            @RequestParam(required = false) String format,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        Map<String, Object> export = new HashMap<>();
        
        List<TestResult> tests = testResultService.findAll();
        
        // Apply date filters if provided
        if (startDate != null || endDate != null) {
            tests = tests.stream()
                .filter(t -> startDate == null || t.getStartTime().isAfter(startDate))
                .filter(t -> endDate == null || t.getStartTime().isBefore(endDate))
                .collect(Collectors.toList());
        }
        
        export.put("totalRecords", tests.size());
        export.put("exportFormat", format != null ? format : "json");
        export.put("data", tests.stream().map(this::convertToMap).collect(Collectors.toList()));
        export.put("exportedAt", LocalDateTime.now());
        export.put("filters", Map.of(
            "startDate", startDate != null ? startDate.toString() : "all",
            "endDate", endDate != null ? endDate.toString() : "all"
        ));
        
        return ResponseEntity.ok(export);
    }

    // Helper methods
    private double calculateSuccessRate() {
        long total = testResultService.getTotalCount();
        long passed = testResultService.getPassedCount();
        return total > 0 ? (double) passed / total * 100 : 0;
    }

    private String extractFailurePattern(String errorMessage) {
        if (errorMessage == null) return "Unknown";
        
        String lowerError = errorMessage.toLowerCase();
        if (lowerError.contains("timeout")) return "Timeout";
        if (lowerError.contains("connection")) return "Connection Error";
        if (lowerError.contains("validation")) return "Validation Error";
        if (lowerError.contains("authentication")) return "Authentication Error";
        if (lowerError.contains("authorization")) return "Authorization Error";
        if (lowerError.contains("not found")) return "Not Found";
        if (lowerError.contains("internal server")) return "Internal Server Error";
        
        return "Other";
    }

    private Map<String, Object> convertToMap(TestResult testResult) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", testResult.getId());
        result.put("testName", testResult.getTestName());
        result.put("serviceName", testResult.getServiceName());
        result.put("testType", testResult.getTestType());
        result.put("status", testResult.getStatus());
        result.put("executionTimeMs", testResult.getExecutionTimeMs());
        result.put("startTime", testResult.getStartTime());
        result.put("endTime", testResult.getEndTime());
        result.put("errorMessage", testResult.getErrorMessage());
        result.put("confidenceScore", testResult.getConfidenceScore());
        result.put("riskLevel", testResult.getRiskLevel());
        result.put("tags", testResult.getTags());
        result.put("testParameters", testResult.getTestParameters());
        result.put("testOutput", testResult.getTestOutput());
        result.put("performanceMetrics", testResult.getPerformanceMetrics());
        result.put("environmentInfo", testResult.getEnvironmentInfo());
        result.put("createdAt", testResult.getCreatedAt());
        result.put("updatedAt", testResult.getUpdatedAt());
        return result;
    }
}
