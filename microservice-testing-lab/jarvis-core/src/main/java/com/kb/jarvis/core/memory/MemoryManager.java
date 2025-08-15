package com.kb.jarvis.core.memory;

import com.kb.jarvis.core.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class MemoryManager {
    
    private static final Logger log = LoggerFactory.getLogger(MemoryManager.class);
    
    @Value("${jarvis.memory.max-entries:10000}")
    private int maxEntries;
    
    @Value("${jarvis.memory.retention-hours:168}") // 7 days
    private int retentionHours;
    
    @Value("${jarvis.memory.cleanup-interval:3600000}") // 1 hour
    private long cleanupInterval;
    
    // Memory storage by type
    private final Map<String, List<MemoryEntry>> memoryStore = new ConcurrentHashMap<>();
    
    // Active tests cache
    private final List<ActiveTest> activeTests = Collections.synchronizedList(new ArrayList<>());
    
    // Recent failures cache
    private final List<TestFailure> recentFailures = Collections.synchronizedList(new ArrayList<>());
    
    // Test results cache
    private final List<TestResult> testResults = Collections.synchronizedList(new ArrayList<>());
    
    // Learning data cache
    private final List<LearningData> learningData = Collections.synchronizedList(new ArrayList<>());
    
    // Pattern cache
    private final Map<String, Pattern> patternCache = new ConcurrentHashMap<>();
    
    public void storeMemory(MemoryEntry entry) {
        log.debug("Storing memory entry: {} - {}", entry.getType(), entry.getKey());
        
        try {
            String type = entry.getType().toString();
            memoryStore.computeIfAbsent(type, k -> Collections.synchronizedList(new ArrayList<>()))
                      .add(entry);
            
            // Maintain memory size limits
            enforceMemoryLimits(type);
            
        } catch (Exception e) {
            log.error("Error storing memory entry: {}", e.getMessage(), e);
        }
    }
    
    public List<MemoryEntry> retrieveMemory(String key) {
        log.debug("Retrieving memory for key: {}", key);
        
        try {
            List<MemoryEntry> results = new ArrayList<>();
            
            // Search across all memory types
            for (List<MemoryEntry> entries : memoryStore.values()) {
                synchronized (entries) {
                    results.addAll(entries.stream()
                        .filter(entry -> entry.getKey().equals(key))
                        .collect(Collectors.toList()));
                }
            }
            
            return results;
            
        } catch (Exception e) {
            log.error("Error retrieving memory for key {}: {}", key, e.getMessage(), e);
            return Collections.emptyList();
        }
    }
    
    public List<MemoryEntry> retrieveMemoryByType(MemoryType type) {
        log.debug("Retrieving memory by type: {}", type);
        
        try {
            List<MemoryEntry> entries = memoryStore.get(type.toString());
            if (entries != null) {
                synchronized (entries) {
                    return new ArrayList<>(entries);
                }
            }
            return Collections.emptyList();
            
        } catch (Exception e) {
            log.error("Error retrieving memory by type {}: {}", type, e.getMessage(), e);
            return Collections.emptyList();
        }
    }
    
    public List<MemoryEntry> retrieveMemoryByTimeRange(LocalDateTime start, LocalDateTime end) {
        log.debug("Retrieving memory from {} to {}", start, end);
        
        try {
            List<MemoryEntry> results = new ArrayList<>();
            
            for (List<MemoryEntry> entries : memoryStore.values()) {
                synchronized (entries) {
                    results.addAll(entries.stream()
                        .filter(entry -> !entry.getTimestamp().isBefore(start) && !entry.getTimestamp().isAfter(end))
                        .collect(Collectors.toList()));
                }
            }
            
            return results;
            
        } catch (Exception e) {
            log.error("Error retrieving memory by time range: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }
    
    public void cleanupExpiredMemory() {
        log.debug("Cleaning up expired memory");
        
        try {
            LocalDateTime cutoff = LocalDateTime.now().minusHours(retentionHours);
            
            for (Map.Entry<String, List<MemoryEntry>> entry : memoryStore.entrySet()) {
                List<MemoryEntry> entries = entry.getValue();
                synchronized (entries) {
                    entries.removeIf(memoryEntry -> memoryEntry.getTimestamp().isBefore(cutoff));
                }
            }
            
            // Clean up active tests
            synchronized (activeTests) {
                activeTests.removeIf(test -> test.getStartTime().isBefore(cutoff));
            }
            
            // Clean up recent failures
            synchronized (recentFailures) {
                recentFailures.removeIf(failure -> failure.getTimestamp().isBefore(cutoff));
            }
            
            // Clean up test results
            synchronized (testResults) {
                testResults.removeIf(result -> result.getTimestamp().isBefore(cutoff));
            }
            
            // Clean up learning data
            synchronized (learningData) {
                learningData.removeIf(data -> data.getTimestamp().isBefore(cutoff));
            }
            
        } catch (Exception e) {
            log.error("Error cleaning up expired memory: {}", e.getMessage(), e);
        }
    }
    
    // Active Tests Management
    public List<ActiveTest> getActiveTests() {
        synchronized (activeTests) {
            return new ArrayList<>(activeTests);
        }
    }
    
    public void addActiveTest(ActiveTest test) {
        synchronized (activeTests) {
            activeTests.add(test);
        }
        log.info("Added active test: {} - {}", test.getType(), test.getServiceName());
    }
    
    public void removeActiveTest(String testId) {
        synchronized (activeTests) {
            activeTests.removeIf(test -> test.getId().equals(testId));
        }
        log.info("Removed active test: {}", testId);
    }
    
    public ActiveTest getActiveTest(String testId) {
        synchronized (activeTests) {
            return activeTests.stream()
                .filter(test -> test.getId().equals(testId))
                .findFirst()
                .orElse(null);
        }
    }
    
    // Test Failures Management
    public List<TestFailure> getRecentFailures() {
        synchronized (recentFailures) {
            return new ArrayList<>(recentFailures);
        }
    }
    
    public void addTestFailure(TestFailure failure) {
        synchronized (recentFailures) {
            recentFailures.add(failure);
            
            // Keep only recent failures (last 100)
            if (recentFailures.size() > 100) {
                recentFailures.remove(0);
            }
        }
        
        // Store in memory
        MemoryEntry entry = MemoryEntry.builder()
            .type(MemoryType.TEST_FAILURE)
            .key("failure_" + failure.getTestId())
            .data(Map.of(
                "testId", failure.getTestId(),
                "failureType", failure.getFailureType().toString(),
                "message", failure.getMessage(),
                "serviceName", failure.getServiceName()
            ))
            .timestamp(failure.getTimestamp())
            .build();
        
        storeMemory(entry);
        log.info("Added test failure: {} - {}", failure.getTestId(), failure.getFailureType());
    }
    
    public List<TestFailure> getFailuresByService(String serviceName) {
        synchronized (recentFailures) {
            return recentFailures.stream()
                .filter(failure -> serviceName.equals(failure.getServiceName()))
                .collect(Collectors.toList());
        }
    }
    
    public List<TestFailure> getFailuresByType(FailureType failureType) {
        synchronized (recentFailures) {
            return recentFailures.stream()
                .filter(failure -> failureType == failure.getFailureType())
                .collect(Collectors.toList());
        }
    }
    
    // Test Results Management
    public void addTestResult(TestResult result) {
        synchronized (testResults) {
            testResults.add(result);
            
            // Keep only recent results (last 1000)
            if (testResults.size() > 1000) {
                testResults.remove(0);
            }
        }
        
        // Store in memory
        MemoryEntry entry = MemoryEntry.builder()
            .type(MemoryType.TEST_RESULT)
            .key("result_" + result.getTestId())
            .data(Map.of(
                "testId", result.getTestId(),
                "status", result.getStatus().toString(),
                "duration", result.getDuration(),
                "serviceName", result.getServiceName(),
                "testType", result.getTestType().toString()
            ))
            .timestamp(result.getTimestamp())
            .build();
        
        storeMemory(entry);
        log.info("Added test result: {} - {}", result.getTestId(), result.getStatus());
    }
    
    public List<TestResult> getTestResults() {
        synchronized (testResults) {
            return new ArrayList<>(testResults);
        }
    }
    
    public List<TestResult> getTestResultsByService(String serviceName) {
        synchronized (testResults) {
            return testResults.stream()
                .filter(result -> serviceName.equals(result.getServiceName()))
                .collect(Collectors.toList());
        }
    }
    
    public List<TestResult> getTestResultsByType(TestType testType) {
        synchronized (testResults) {
            return testResults.stream()
                .filter(result -> testType == result.getTestType())
                .collect(Collectors.toList());
        }
    }
    
    // Learning Data Management
    public void addLearningData(LearningData data) {
        synchronized (learningData) {
            learningData.add(data);
            
            // Keep only recent learning data (last 500)
            if (learningData.size() > 500) {
                learningData.remove(0);
            }
        }
        
        // Store in memory
        MemoryEntry entry = MemoryEntry.builder()
            .type(MemoryType.LEARNING_DATA)
            .key("learning_" + data.getType().toString() + "_" + data.getTimestamp().toString())
            .data(Map.of(
                "type", data.getType().toString(),
                "data", data.getData(),
                "insights", data.getInsights()
            ))
            .timestamp(data.getTimestamp())
            .build();
        
        storeMemory(entry);
        log.info("Added learning data: {} - {}", data.getType(), data.getInsights());
    }
    
    public List<LearningData> getLearningData() {
        synchronized (learningData) {
            return new ArrayList<>(learningData);
        }
    }
    
    public List<LearningData> getLearningDataByType(LearningDataType type) {
        synchronized (learningData) {
            return learningData.stream()
                .filter(data -> type == data.getType())
                .collect(Collectors.toList());
        }
    }
    
    // Pattern Management
    public void storePattern(Pattern pattern) {
        patternCache.put(pattern.getName(), pattern);
        log.info("Stored pattern: {} with confidence: {}", pattern.getName(), pattern.getConfidence());
    }
    
    public Pattern getPattern(String patternName) {
        return patternCache.get(patternName);
    }
    
    public List<Pattern> getAllPatterns() {
        return new ArrayList<>(patternCache.values());
    }
    
    public List<Pattern> getPatternsByConfidence(double minConfidence) {
        return patternCache.values().stream()
            .filter(pattern -> pattern.getConfidence() >= minConfidence)
            .collect(Collectors.toList());
    }
    
    // Statistics and Analytics
    public Map<String, Object> getMemoryStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // Memory store stats
        for (Map.Entry<String, List<MemoryEntry>> entry : memoryStore.entrySet()) {
            synchronized (entry.getValue()) {
                stats.put("memory_" + entry.getKey(), entry.getValue().size());
            }
        }
        
        // Active tests stats
        synchronized (activeTests) {
            stats.put("activeTests", activeTests.size());
        }
        
        // Recent failures stats
        synchronized (recentFailures) {
            stats.put("recentFailures", recentFailures.size());
        }
        
        // Test results stats
        synchronized (testResults) {
            stats.put("testResults", testResults.size());
            
            // Calculate success rate
            long successfulTests = testResults.stream()
                .filter(result -> result.getStatus() == TestStatus.PASSED)
                .count();
            double successRate = testResults.isEmpty() ? 0.0 : (double) successfulTests / testResults.size();
            stats.put("testSuccessRate", successRate);
        }
        
        // Learning data stats
        synchronized (learningData) {
            stats.put("learningData", learningData.size());
        }
        
        // Pattern stats
        stats.put("patterns", patternCache.size());
        
        return stats;
    }
    
    // Scheduled cleanup
    @Scheduled(fixedDelayString = "${jarvis.memory.cleanup-interval:3600000}")
    public void scheduledCleanup() {
        cleanupExpiredMemory();
    }
    
    private void enforceMemoryLimits(String type) {
        List<MemoryEntry> entries = memoryStore.get(type);
        if (entries != null) {
            synchronized (entries) {
                while (entries.size() > maxEntries) {
                    entries.remove(0); // Remove oldest entries
                }
            }
        }
    }
    
    // Search functionality
    public List<MemoryEntry> searchMemory(String query) {
        log.debug("Searching memory for query: {}", query);
        
        try {
            List<MemoryEntry> results = new ArrayList<>();
            String lowerQuery = query.toLowerCase();
            
            for (List<MemoryEntry> entries : memoryStore.values()) {
                synchronized (entries) {
                    results.addAll(entries.stream()
                        .filter(entry -> 
                            entry.getKey().toLowerCase().contains(lowerQuery) ||
                            entry.getData().toString().toLowerCase().contains(lowerQuery))
                        .collect(Collectors.toList()));
                }
            }
            
            return results;
            
        } catch (Exception e) {
            log.error("Error searching memory: {}", e.getMessage(), e);
            return Collections.emptyList();
        }
    }
} 