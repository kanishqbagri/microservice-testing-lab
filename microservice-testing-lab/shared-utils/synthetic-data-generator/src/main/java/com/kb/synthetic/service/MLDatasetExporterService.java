package com.kb.synthetic.service;

import com.kb.synthetic.model.*;
import com.kb.synthetic.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MLDatasetExporterService {
    
    private final PullRequestRepository pullRequestRepository;
    private final SystemRunRepository systemRunRepository;
    private final ServiceMetricsRepository serviceMetricsRepository;
    private final EcommerceFlowRepository ecommerceFlowRepository;
    private final FlowStepRepository flowStepRepository;
    
    private final ObjectMapper objectMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);
    
    /**
     * Export comprehensive ML dataset with all features and labels
     */
    public void exportMLDataset(String outputPath) throws IOException {
        log.info("Starting ML dataset export to: {}", outputPath);
        
        // Export different types of datasets
        exportPullRequestDataset(outputPath + "/pr_dataset.json");
        exportSystemRunDataset(outputPath + "/system_run_dataset.json");
        exportServiceMetricsDataset(outputPath + "/service_metrics_dataset.json");
        exportEcommerceFlowDataset(outputPath + "/ecommerce_flow_dataset.json");
        exportFlowStepDataset(outputPath + "/flow_step_dataset.json");
        exportCombinedDataset(outputPath + "/combined_dataset.json");
        exportTimeSeriesDataset(outputPath + "/time_series_dataset.json");
        
        log.info("ML dataset export completed successfully");
    }
    
    /**
     * Export Pull Request dataset for PR impact prediction
     */
    private void exportPullRequestDataset(String filePath) throws IOException {
        List<PullRequest> prs = pullRequestRepository.findAll();
        List<Map<String, Object>> dataset = new ArrayList<>();
        
        for (PullRequest pr : prs) {
            Map<String, Object> record = new HashMap<>();
            
            // Features
            record.put("pr_number", pr.getPrNumber());
            record.put("lines_added", pr.getLinesAdded());
            record.put("lines_deleted", pr.getLinesDeleted());
            record.put("files_changed", pr.getFilesChanged());
            record.put("test_coverage", pr.getTestCoverage());
            record.put("code_complexity", pr.getCodeComplexity());
            record.put("cyclomatic_complexity", pr.getCyclomaticComplexity());
            record.put("affected_services_count", pr.getAffectedServices().split(",").length);
            record.put("change_types_count", pr.getChangeTypes().split(",").length);
            record.put("has_breaking_change", pr.getHasBreakingChange());
            record.put("has_security_vulnerability", pr.getHasSecurityVulnerability());
            
            // Service impact scores
            if (pr.getServiceImpactScores() != null) {
                for (Map.Entry<String, Double> entry : pr.getServiceImpactScores().entrySet()) {
                    record.put("impact_" + entry.getKey().replace("-", "_"), entry.getValue());
                }
            }
            
            // Labels
            record.put("has_performance_regression", pr.getHasPerformanceRegression());
            record.put("performance_impact_score", pr.getPerformanceImpactScore());
            record.put("risk_score", pr.getRiskScore());
            record.put("avg_response_time_ms", pr.getAvgResponseTimeMs());
            record.put("error_rate_increase", pr.getErrorRateIncrease());
            record.put("memory_usage_increase", pr.getMemoryUsageIncrease());
            record.put("cpu_usage_increase", pr.getCpuUsageIncrease());
            
            dataset.add(record);
        }
        
        writeToFile(filePath, dataset);
        log.info("Exported {} PR records to {}", dataset.size(), filePath);
    }
    
    /**
     * Export System Run dataset for system performance prediction
     */
    private void exportSystemRunDataset(String filePath) throws IOException {
        List<SystemRun> runs = systemRunRepository.findAll();
        List<Map<String, Object>> dataset = new ArrayList<>();
        
        for (SystemRun run : runs) {
            Map<String, Object> record = new HashMap<>();
            
            // Features
            record.put("run_id", run.getRunId());
            record.put("run_type", run.getRunType().toString());
            record.put("load_intensity", run.getLoadIntensity());
            record.put("total_requests", run.getTotalRequests());
            record.put("avg_cpu_usage", run.getAvgCpuUsage());
            record.put("max_cpu_usage", run.getMaxCpuUsage());
            record.put("avg_memory_usage", run.getAvgMemoryUsage());
            record.put("max_memory_usage", run.getMaxMemoryUsage());
            record.put("avg_disk_usage", run.getAvgDiskUsage());
            record.put("max_disk_usage", run.getMaxDiskUsage());
            
            // Error analysis
            if (run.getErrorAnalysis() != null) {
                for (Map.Entry<String, Integer> entry : run.getErrorAnalysis().entrySet()) {
                    record.put("error_" + entry.getKey().toLowerCase().replace(" ", "_"), entry.getValue());
                }
            }
            
            // Labels
            record.put("overall_success_rate", run.getOverallSuccessRate());
            record.put("avg_response_time_ms", run.getAvgResponseTimeMs());
            record.put("p95_response_time_ms", run.getP95ResponseTimeMs());
            record.put("p99_response_time_ms", run.getP99ResponseTimeMs());
            record.put("throughput_rps", run.getThroughputRps());
            record.put("has_performance_regression", run.getHasPerformanceRegression());
            record.put("performance_regression_score", run.getPerformanceRegressionScore());
            record.put("is_anomalous", run.getIsAnomalous());
            record.put("anomaly_score", run.getAnomalyScore());
            
            dataset.add(record);
        }
        
        writeToFile(filePath, dataset);
        log.info("Exported {} system run records to {}", dataset.size(), filePath);
    }
    
    /**
     * Export Service Metrics dataset for service health prediction
     */
    private void exportServiceMetricsDataset(String filePath) throws IOException {
        List<ServiceMetrics> metrics = serviceMetricsRepository.findAll();
        List<Map<String, Object>> dataset = new ArrayList<>();
        
        for (ServiceMetrics metric : metrics) {
            Map<String, Object> record = new HashMap<>();
            
            // Features
            record.put("service_name", metric.getServiceName());
            record.put("service_version", metric.getServiceVersion());
            record.put("health_score", metric.getHealthScore());
            record.put("total_requests", metric.getTotalRequests());
            record.put("cpu_usage_percent", metric.getCpuUsagePercent());
            record.put("memory_usage_mb", metric.getMemoryUsageMB());
            record.put("memory_usage_percent", metric.getMemoryUsagePercent());
            record.put("disk_usage_mb", metric.getDiskUsageMB());
            record.put("disk_usage_percent", metric.getDiskUsagePercent());
            record.put("network_in_mbps", metric.getNetworkInMBps());
            record.put("network_out_mbps", metric.getNetworkOutMBps());
            record.put("upstream_dependencies", metric.getUpstreamDependencies());
            record.put("downstream_dependencies", metric.getDownstreamDependencies());
            record.put("dependency_health_score", metric.getDependencyHealthScore());
            
            // Business metrics
            record.put("orders_processed", metric.getOrdersProcessed());
            record.put("products_viewed", metric.getProductsViewed());
            record.put("users_active", metric.getUsersActive());
            record.put("revenue_generated", metric.getRevenueGenerated());
            
            // Error breakdown
            if (metric.getErrorBreakdown() != null) {
                for (Map.Entry<String, Integer> entry : metric.getErrorBreakdown().entrySet()) {
                    record.put("error_" + entry.getKey(), entry.getValue());
                }
            }
            
            // Labels
            record.put("success_rate", metric.getSuccessRate());
            record.put("error_rate", metric.getErrorRate());
            record.put("avg_response_time_ms", metric.getAvgResponseTimeMs());
            record.put("p95_response_time_ms", metric.getP95ResponseTimeMs());
            record.put("p99_response_time_ms", metric.getP99ResponseTimeMs());
            record.put("throughput_rps", metric.getThroughputRps());
            record.put("is_anomalous", metric.getIsAnomalous());
            record.put("anomaly_score", metric.getAnomalyScore());
            record.put("anomaly_type", metric.getAnomalyType());
            record.put("predicted_failure_probability", metric.getPredictedFailureProbability());
            
            dataset.add(record);
        }
        
        writeToFile(filePath, dataset);
        log.info("Exported {} service metrics records to {}", dataset.size(), filePath);
    }
    
    /**
     * Export E-commerce Flow dataset for conversion prediction
     */
    private void exportEcommerceFlowDataset(String filePath) throws IOException {
        List<EcommerceFlow> flows = ecommerceFlowRepository.findAll();
        List<Map<String, Object>> dataset = new ArrayList<>();
        
        for (EcommerceFlow flow : flows) {
            Map<String, Object> record = new HashMap<>();
            
            // Features
            record.put("flow_id", flow.getFlowId());
            record.put("flow_type", flow.getFlowType().toString());
            record.put("user_segment", flow.getUserSegment());
            record.put("device_type", flow.getDeviceType());
            record.put("browser_type", flow.getBrowserType());
            record.put("location", flow.getLocation());
            record.put("session_duration_minutes", flow.getSessionDurationMinutes());
            record.put("items_in_cart", flow.getItemsInCart());
            record.put("cart_value", flow.getCartValue());
            record.put("payment_method", flow.getPaymentMethod());
            record.put("total_steps", flow.getTotalSteps());
            record.put("successful_steps", flow.getSuccessfulSteps());
            record.put("failed_steps", flow.getFailedSteps());
            record.put("total_duration_ms", flow.getTotalDurationMs());
            record.put("avg_step_duration_ms", flow.getAvgStepDurationMs());
            
            // Error analysis
            if (flow.getErrorAnalysis() != null) {
                for (Map.Entry<String, Integer> entry : flow.getErrorAnalysis().entrySet()) {
                    record.put("error_" + entry.getKey().toLowerCase().replace(" ", "_"), entry.getValue());
                }
            }
            
            // Labels
            record.put("success_rate", flow.getSuccessRate());
            record.put("converted", flow.getConverted());
            record.put("conversion_value", flow.getConversionValue());
            record.put("conversion_type", flow.getConversionType());
            record.put("final_order_value", flow.getFinalOrderValue());
            record.put("payment_successful", flow.getPaymentSuccessful());
            record.put("abandonment_probability", flow.getAbandonmentProbability());
            record.put("is_anomalous", flow.getIsAnomalous());
            record.put("anomaly_score", flow.getAnomalyScore());
            record.put("anomaly_type", flow.getAnomalyType());
            
            dataset.add(record);
        }
        
        writeToFile(filePath, dataset);
        log.info("Exported {} e-commerce flow records to {}", dataset.size(), filePath);
    }
    
    /**
     * Export Flow Step dataset for step failure prediction
     */
    private void exportFlowStepDataset(String filePath) throws IOException {
        List<FlowStep> steps = flowStepRepository.findAll();
        List<Map<String, Object>> dataset = new ArrayList<>();
        
        for (FlowStep step : steps) {
            Map<String, Object> record = new HashMap<>();
            
            // Features
            record.put("step_id", step.getStepId());
            record.put("step_name", step.getStepName());
            record.put("step_order", step.getStepOrder());
            record.put("service_name", step.getServiceName());
            record.put("endpoint", step.getEndpoint());
            record.put("http_method", step.getHttpMethod());
            record.put("business_action", step.getBusinessAction());
            record.put("duration_ms", step.getDurationMs());
            record.put("response_size_bytes", step.getResponseSizeBytes());
            record.put("is_retryable", step.getIsRetryable());
            record.put("retry_attempts", step.getRetryAttempts());
            
            // Labels
            record.put("response_code", step.getResponseCode());
            record.put("response_time_ms", step.getResponseTimeMs());
            record.put("error_type", step.getErrorType());
            record.put("error_category", step.getErrorCategory());
            record.put("failure_probability", step.getFailureProbability());
            record.put("is_anomalous", step.getIsAnomalous());
            record.put("anomaly_score", step.getAnomalyScore());
            
            dataset.add(record);
        }
        
        writeToFile(filePath, dataset);
        log.info("Exported {} flow step records to {}", dataset.size(), filePath);
    }
    
    /**
     * Export combined dataset with all entities joined
     */
    private void exportCombinedDataset(String filePath) throws IOException {
        List<PullRequest> prs = pullRequestRepository.findAll();
        List<Map<String, Object>> dataset = new ArrayList<>();
        
        for (PullRequest pr : prs) {
            List<SystemRun> runs = systemRunRepository.findByPullRequest(pr);
            
            for (SystemRun run : runs) {
                List<ServiceMetrics> metrics = serviceMetricsRepository.findBySystemRun(run);
                List<EcommerceFlow> flows = ecommerceFlowRepository.findBySystemRun(run);
                
                for (ServiceMetrics metric : metrics) {
                    Map<String, Object> record = new HashMap<>();
                    
                    // PR features
                    record.put("pr_lines_added", pr.getLinesAdded());
                    record.put("pr_test_coverage", pr.getTestCoverage());
                    record.put("pr_complexity", pr.getCodeComplexity());
                    record.put("pr_risk_score", pr.getRiskScore());
                    record.put("pr_has_performance_regression", pr.getHasPerformanceRegression());
                    
                    // Run features
                    record.put("run_type", run.getRunType().toString());
                    record.put("run_load_intensity", run.getLoadIntensity());
                    record.put("run_total_requests", run.getTotalRequests());
                    
                    // Service features
                    record.put("service_name", metric.getServiceName());
                    record.put("service_health_score", metric.getHealthScore());
                    record.put("service_cpu_usage", metric.getCpuUsagePercent());
                    record.put("service_memory_usage", metric.getMemoryUsagePercent());
                    record.put("service_dependencies", metric.getUpstreamDependencies() + metric.getDownstreamDependencies());
                    
                    // Labels
                    record.put("service_success_rate", metric.getSuccessRate());
                    record.put("service_response_time", metric.getAvgResponseTimeMs());
                    record.put("service_is_anomalous", metric.getIsAnomalous());
                    record.put("service_failure_probability", metric.getPredictedFailureProbability());
                    
                    dataset.add(record);
                }
            }
        }
        
        writeToFile(filePath, dataset);
        log.info("Exported {} combined records to {}", dataset.size(), filePath);
    }
    
    /**
     * Export time series dataset for temporal analysis
     */
    private void exportTimeSeriesDataset(String filePath) throws IOException {
        List<ServiceMetrics> metrics = serviceMetricsRepository.findAll();
        Map<String, List<ServiceMetrics>> metricsByService = metrics.stream()
                .collect(Collectors.groupingBy(ServiceMetrics::getServiceName));
        
        List<Map<String, Object>> dataset = new ArrayList<>();
        
        for (Map.Entry<String, List<ServiceMetrics>> entry : metricsByService.entrySet()) {
            String serviceName = entry.getKey();
            List<ServiceMetrics> serviceMetrics = entry.getValue();
            
            // Sort by timestamp
            serviceMetrics.sort(Comparator.comparing(ServiceMetrics::getTimestamp));
            
            for (int i = 0; i < serviceMetrics.size(); i++) {
                ServiceMetrics current = serviceMetrics.get(i);
                Map<String, Object> record = new HashMap<>();
                
                // Time features
                record.put("timestamp", current.getTimestamp().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                record.put("service_name", serviceName);
                record.put("sequence_index", i);
                
                // Current metrics
                record.put("current_success_rate", current.getSuccessRate());
                record.put("current_response_time", current.getAvgResponseTimeMs());
                record.put("current_cpu_usage", current.getCpuUsagePercent());
                record.put("current_memory_usage", current.getMemoryUsagePercent());
                record.put("current_health_score", current.getHealthScore());
                
                // Historical features (if available)
                if (i > 0) {
                    ServiceMetrics previous = serviceMetrics.get(i - 1);
                    record.put("prev_success_rate", previous.getSuccessRate());
                    record.put("prev_response_time", previous.getAvgResponseTimeMs());
                    record.put("prev_cpu_usage", previous.getCpuUsagePercent());
                    record.put("prev_memory_usage", previous.getMemoryUsagePercent());
                    record.put("success_rate_change", current.getSuccessRate() - previous.getSuccessRate());
                    record.put("response_time_change", current.getAvgResponseTimeMs() - previous.getAvgResponseTimeMs());
                }
                
                // Labels
                record.put("is_anomalous", current.getIsAnomalous());
                record.put("anomaly_score", current.getAnomalyScore());
                record.put("failure_probability", current.getPredictedFailureProbability());
                
                dataset.add(record);
            }
        }
        
        writeToFile(filePath, dataset);
        log.info("Exported {} time series records to {}", dataset.size(), filePath);
    }
    
    /**
     * Export dataset statistics for analysis
     */
    public void exportDatasetStatistics(String outputPath) throws IOException {
        Map<String, Object> stats = new HashMap<>();
        
        // Basic counts
        stats.put("total_pull_requests", pullRequestRepository.count());
        stats.put("total_system_runs", systemRunRepository.count());
        stats.put("total_service_metrics", serviceMetricsRepository.count());
        stats.put("total_ecommerce_flows", ecommerceFlowRepository.count());
        stats.put("total_flow_steps", flowStepRepository.count());
        
        // Performance regression stats
        stats.put("prs_with_performance_regression", pullRequestRepository.findByHasPerformanceRegressionTrue().size());
        stats.put("runs_with_performance_regression", systemRunRepository.findByHasPerformanceRegressionTrue().size());
        
        // Anomaly stats
        stats.put("anomalous_runs", systemRunRepository.findByIsAnomalousTrue().size());
        stats.put("anomalous_metrics", serviceMetricsRepository.findByIsAnomalousTrue().size());
        stats.put("anomalous_flows", ecommerceFlowRepository.findByIsAnomalousTrue().size());
        
        // E-commerce stats
        stats.put("converted_flows", ecommerceFlowRepository.findByConvertedTrue().size());
        stats.put("high_value_flows", ecommerceFlowRepository.findByHighCartValue(100.0).size());
        
        // Service-specific stats
        for (String service : Arrays.asList("gateway-service", "user-service", "product-service", "order-service", "notification-service")) {
            Map<String, Object> serviceStats = new HashMap<>();
            serviceStats.put("total_metrics", serviceMetricsRepository.countByServiceNameAndTimestampAfter(service, LocalDateTime.now().minusDays(30)));
            serviceStats.put("avg_success_rate", serviceMetricsRepository.getAverageSuccessRateByService(service));
            serviceStats.put("avg_response_time", serviceMetricsRepository.getAverageResponseTimeByService(service));
            serviceStats.put("avg_cpu_usage", serviceMetricsRepository.getAverageCpuUsageByService(service));
            serviceStats.put("avg_memory_usage", serviceMetricsRepository.getAverageMemoryUsageByService(service));
            stats.put("service_" + service.replace("-", "_"), serviceStats);
        }
        
        writeToFile(outputPath + "/dataset_statistics.json", stats);
        log.info("Exported dataset statistics to {}", outputPath + "/dataset_statistics.json");
    }
    
    private void writeToFile(String filePath, Object data) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            objectMapper.writeValue(writer, data);
        }
    }
}
