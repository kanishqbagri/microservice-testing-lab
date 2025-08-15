package com.kb.jarvis.core.context;

import com.kb.jarvis.core.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class ContextManager {
    
    private static final Logger log = LoggerFactory.getLogger(ContextManager.class);
    
    @Value("${jarvis.context.update-interval:30000}")
    private long updateInterval;
    
    @Value("${jarvis.context.service-timeout:5000}")
    private int serviceTimeout;
    
    @Value("${jarvis.context.max-retries:3}")
    private int maxRetries;
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    // Current system context
    private final AtomicReference<SystemContext> currentContext = new AtomicReference<>();
    
    // Service health cache
    private final Map<String, ServiceHealth> serviceHealthCache = new ConcurrentHashMap<>();
    
    // Performance metrics cache
    private final AtomicReference<PerformanceMetrics> performanceMetrics = new AtomicReference<>();
    
    // System health cache
    private final AtomicReference<SystemHealth> systemHealth = new AtomicReference<>();
    
    // Event history
    private final List<SystemEvent> eventHistory = Collections.synchronizedList(new ArrayList<>());
    
    // Service endpoints configuration
    private static final Map<String, String> SERVICE_ENDPOINTS = new HashMap<>();
    
    static {
        initializeServiceEndpoints();
    }
    
    public void updateContext(UserIntent intent) {
        log.debug("Updating context with intent: {}", intent.getType());
        
        try {
            // Create system event
            SystemEvent event = SystemEvent.builder()
                .type("USER_INTENT")
                .data(Map.of("intent", intent.getType().toString(), "confidence", intent.getConfidence()))
                .timestamp(LocalDateTime.now())
                .build();
            
            // Add to event history
            eventHistory.add(event);
            
            // Update current context
            SystemContext context = SystemContext.builder()
                .lastIntent(intent)
                .lastUpdate(LocalDateTime.now())
                .activeEvents(1)
                .build();
            
            currentContext.set(context);
            
            // Trigger health check if needed
            if (intent.getParameters().containsKey("services")) {
                List<String> services = (List<String>) intent.getParameters().get("services");
                checkServiceHealth(services);
            }
            
        } catch (Exception e) {
            log.error("Error updating context: {}", e.getMessage(), e);
        }
    }
    
    public SystemHealth getSystemHealth() {
        return systemHealth.get();
    }
    
    public PerformanceMetrics getPerformanceMetrics() {
        return performanceMetrics.get();
    }
    
    public SystemContext getCurrentContext() {
        return currentContext.get();
    }
    
    public List<ServiceHealth> getAllServiceHealth() {
        return new ArrayList<>(serviceHealthCache.values());
    }
    
    public ServiceHealth getServiceHealth(String serviceName) {
        return serviceHealthCache.get(serviceName);
    }
    
    public List<SystemEvent> getRecentEvents(int count) {
        synchronized (eventHistory) {
            int startIndex = Math.max(0, eventHistory.size() - count);
            return new ArrayList<>(eventHistory.subList(startIndex, eventHistory.size()));
        }
    }
    
    /**
     * Scheduled task to update system health and performance metrics
     */
    @Scheduled(fixedDelayString = "${jarvis.context.update-interval:30000}")
    public void updateSystemMetrics() {
        log.debug("Updating system metrics");
        
        try {
            // Update performance metrics
            updatePerformanceMetrics();
            
            // Update system health
            updateSystemHealth();
            
            // Check all service health
            checkAllServiceHealth();
            
            // Clean up old events
            cleanupOldEvents();
            
        } catch (Exception e) {
            log.error("Error updating system metrics: {}", e.getMessage(), e);
        }
    }
    
    private void updatePerformanceMetrics() {
        try {
            // Simulate performance metrics collection
            // In a real implementation, this would collect actual system metrics
            
            double cpuUsage = getCurrentCpuUsage();
            double memoryUsage = getCurrentMemoryUsage();
            double diskUsage = getCurrentDiskUsage();
            double networkLatency = getCurrentNetworkLatency();
            
            PerformanceMetrics metrics = PerformanceMetrics.builder()
                .cpuUsage(cpuUsage)
                .memoryUsage(memoryUsage)
                .diskUsage(diskUsage)
                .networkLatency(networkLatency)
                .timestamp(LocalDateTime.now())
                .build();
            
            performanceMetrics.set(metrics);
            
        } catch (Exception e) {
            log.warn("Could not update performance metrics: {}", e.getMessage());
        }
    }
    
    private void updateSystemHealth() {
        try {
            // Determine overall system health based on various factors
            HealthStatus overallStatus = HealthStatus.HEALTHY;
            List<String> issues = new ArrayList<>();
            
            // Check performance metrics
            PerformanceMetrics metrics = performanceMetrics.get();
            if (metrics != null) {
                if (metrics.getCpuUsage() > 90) {
                    overallStatus = HealthStatus.DEGRADED;
                    issues.add("High CPU usage: " + metrics.getCpuUsage() + "%");
                }
                
                if (metrics.getMemoryUsage() > 85) {
                    overallStatus = HealthStatus.DEGRADED;
                    issues.add("High memory usage: " + metrics.getMemoryUsage() + "%");
                }
                
                if (metrics.getNetworkLatency() > 1000) {
                    overallStatus = HealthStatus.DEGRADED;
                    issues.add("High network latency: " + metrics.getNetworkLatency() + "ms");
                }
            }
            
            // Check service health
            long unhealthyServices = serviceHealthCache.values().stream()
                .filter(health -> health.getStatus() != HealthStatus.HEALTHY)
                .count();
            
            if (unhealthyServices > 0) {
                overallStatus = HealthStatus.DEGRADED;
                issues.add(unhealthyServices + " unhealthy services detected");
            }
            
            if (unhealthyServices > 2) {
                overallStatus = HealthStatus.UNHEALTHY;
            }
            
            SystemHealth health = SystemHealth.builder()
                .status(overallStatus)
                .issues(issues)
                .timestamp(LocalDateTime.now())
                .build();
            
            systemHealth.set(health);
            
        } catch (Exception e) {
            log.warn("Could not update system health: {}", e.getMessage());
        }
    }
    
    private void checkAllServiceHealth() {
        for (String serviceName : SERVICE_ENDPOINTS.keySet()) {
            checkServiceHealth(Collections.singletonList(serviceName));
        }
    }
    
    private void checkServiceHealth(List<String> services) {
        for (String serviceName : services) {
            try {
                String endpoint = SERVICE_ENDPOINTS.get(serviceName);
                if (endpoint == null) {
                    log.warn("No endpoint configured for service: {}", serviceName);
                    continue;
                }
                
                HealthStatus status = checkServiceEndpoint(endpoint);
                String message = status == HealthStatus.HEALTHY ? "Service is healthy" : "Service health check failed";
                
                ServiceHealth health = ServiceHealth.builder()
                    .serviceName(serviceName)
                    .status(status)
                    .message(message)
                    .timestamp(LocalDateTime.now())
                    .build();
                
                serviceHealthCache.put(serviceName, health);
                
            } catch (Exception e) {
                log.warn("Error checking health for service {}: {}", serviceName, e.getMessage());
                
                ServiceHealth health = ServiceHealth.builder()
                    .serviceName(serviceName)
                    .status(HealthStatus.UNHEALTHY)
                    .message("Health check failed: " + e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .build();
                
                serviceHealthCache.put(serviceName, health);
            }
        }
    }
    
    private HealthStatus checkServiceEndpoint(String endpoint) {
        try {
            // In a real implementation, this would make an actual HTTP call
            // For now, we'll simulate the health check
            
            // Simulate network delay
            Thread.sleep(100);
            
            // Simulate random health status (mostly healthy)
            double random = Math.random();
            if (random > 0.9) {
                return HealthStatus.UNHEALTHY;
            } else if (random > 0.7) {
                return HealthStatus.DEGRADED;
            } else {
                return HealthStatus.HEALTHY;
            }
            
        } catch (Exception e) {
            log.warn("Service health check failed for {}: {}", endpoint, e.getMessage());
            return HealthStatus.UNHEALTHY;
        }
    }
    
    private void cleanupOldEvents() {
        LocalDateTime cutoff = LocalDateTime.now().minusHours(24);
        
        synchronized (eventHistory) {
            eventHistory.removeIf(event -> event.getTimestamp().isBefore(cutoff));
        }
    }
    
    // Simulated metrics collection methods
    private double getCurrentCpuUsage() {
        // Simulate CPU usage between 20-80%
        return 20 + Math.random() * 60;
    }
    
    private double getCurrentMemoryUsage() {
        // Simulate memory usage between 30-85%
        return 30 + Math.random() * 55;
    }
    
    private double getCurrentDiskUsage() {
        // Simulate disk usage between 40-90%
        return 40 + Math.random() * 50;
    }
    
    private double getCurrentNetworkLatency() {
        // Simulate network latency between 10-500ms
        return 10 + Math.random() * 490;
    }
    
    private static void initializeServiceEndpoints() {
        SERVICE_ENDPOINTS.put("user-service", "http://localhost:8081/actuator/health");
        SERVICE_ENDPOINTS.put("product-service", "http://localhost:8082/actuator/health");
        SERVICE_ENDPOINTS.put("order-service", "http://localhost:8083/actuator/health");
        SERVICE_ENDPOINTS.put("notification-service", "http://localhost:8084/actuator/health");
        SERVICE_ENDPOINTS.put("gateway-service", "http://localhost:8080/actuator/health");
    }
    
    /**
     * Get system statistics for monitoring
     */
    public Map<String, Object> getSystemStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // Context stats
        SystemContext context = currentContext.get();
        if (context != null) {
            stats.put("lastUpdate", context.getLastUpdate());
            stats.put("activeEvents", context.getActiveEvents());
        }
        
        // Health stats
        SystemHealth health = systemHealth.get();
        if (health != null) {
            stats.put("systemStatus", health.getStatus());
            stats.put("issueCount", health.getIssues().size());
        }
        
        // Service stats
        long healthyServices = serviceHealthCache.values().stream()
            .filter(h -> h.getStatus() == HealthStatus.HEALTHY)
            .count();
        stats.put("healthyServices", healthyServices);
        stats.put("totalServices", serviceHealthCache.size());
        
        // Performance stats
        PerformanceMetrics metrics = performanceMetrics.get();
        if (metrics != null) {
            stats.put("cpuUsage", metrics.getCpuUsage());
            stats.put("memoryUsage", metrics.getMemoryUsage());
            stats.put("networkLatency", metrics.getNetworkLatency());
        }
        
        // Event stats
        synchronized (eventHistory) {
            stats.put("totalEvents", eventHistory.size());
            stats.put("recentEvents", eventHistory.size());
        }
        
        return stats;
    }
} 