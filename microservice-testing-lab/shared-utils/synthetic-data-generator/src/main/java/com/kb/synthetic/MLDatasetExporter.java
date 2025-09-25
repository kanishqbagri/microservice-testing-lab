package com.kb.synthetic;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * ML Dataset Exporter for Synthetic Data
 * 
 * This class exports the generated synthetic data to JSON format for ML model training.
 * It creates multiple datasets optimized for different ML use cases.
 */
public class MLDatasetExporter {
    
    private final SimpleSyntheticDataGenerator generator;
    
    public MLDatasetExporter() {
        this.generator = new SimpleSyntheticDataGenerator();
    }
    
    /**
     * Generate and export comprehensive ML datasets
     */
    public void generateAndExportMLDatasets(int prCount, int runsPerPR, String outputPath) {
        System.out.println("🎯 Generating and Exporting ML Datasets");
        System.out.println("======================================");
        System.out.println("PR Count: " + prCount);
        System.out.println("Runs per PR: " + runsPerPR);
        System.out.println("Output Path: " + outputPath);
        System.out.println();
        
        // Generate synthetic data
        List<PullRequestData> pullRequests = generator.generatePullRequests(prCount);
        List<SystemRunData> allSystemRuns = new ArrayList<>();
        List<ServiceMetricsData> allServiceMetrics = new ArrayList<>();
        List<EcommerceFlowData> allEcommerceFlows = new ArrayList<>();
        
        for (PullRequestData pr : pullRequests) {
            List<SystemRunData> systemRuns = generator.generateSystemRuns(pr, runsPerPR);
            allSystemRuns.addAll(systemRuns);
            
            for (SystemRunData run : systemRuns) {
                generator.generateServiceMetrics(run);
                generator.generateEcommerceFlows(run);
                allServiceMetrics.addAll(run.serviceMetrics);
                allEcommerceFlows.addAll(run.ecommerceFlows);
            }
        }
        
        // Export datasets
        try {
            exportPullRequestDataset(pullRequests, outputPath + "/pr_dataset.json");
            exportSystemRunDataset(allSystemRuns, outputPath + "/system_run_dataset.json");
            exportServiceMetricsDataset(allServiceMetrics, outputPath + "/service_metrics_dataset.json");
            exportEcommerceFlowDataset(allEcommerceFlows, outputPath + "/ecommerce_flow_dataset.json");
            exportCombinedDataset(pullRequests, allSystemRuns, allServiceMetrics, outputPath + "/combined_dataset.json");
            exportDatasetStatistics(pullRequests, allSystemRuns, allServiceMetrics, allEcommerceFlows, outputPath + "/dataset_statistics.json");
            
            System.out.println("✅ All datasets exported successfully!");
            System.out.println("📁 Files created in: " + outputPath);
            
        } catch (IOException e) {
            System.err.println("❌ Error exporting datasets: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Export Pull Request dataset for PR impact prediction
     */
    private void exportPullRequestDataset(List<PullRequestData> prs, String filePath) throws IOException {
        StringBuilder json = new StringBuilder();
        json.append("[\n");
        
        for (int i = 0; i < prs.size(); i++) {
            PullRequestData pr = prs.get(i);
            json.append("  {\n");
            json.append("    \"pr_number\": \"").append(pr.prNumber).append("\",\n");
            json.append("    \"lines_added\": ").append(pr.linesAdded).append(",\n");
            json.append("    \"lines_deleted\": ").append(pr.linesDeleted).append(",\n");
            json.append("    \"files_changed\": ").append(pr.filesChanged).append(",\n");
            json.append("    \"test_coverage\": ").append(pr.testCoverage).append(",\n");
            json.append("    \"code_complexity\": ").append(pr.codeComplexity).append(",\n");
            json.append("    \"cyclomatic_complexity\": ").append(pr.cyclomaticComplexity).append(",\n");
            json.append("    \"affected_services_count\": ").append(pr.affectedServices.split(",").length).append(",\n");
            json.append("    \"change_types_count\": ").append(pr.changeTypes.split(",").length).append(",\n");
            json.append("    \"has_breaking_change\": ").append(pr.hasBreakingChange).append(",\n");
            json.append("    \"has_security_vulnerability\": ").append(pr.hasSecurityVulnerability).append(",\n");
            json.append("    \"has_performance_regression\": ").append(pr.hasPerformanceRegression).append(",\n");
            json.append("    \"performance_impact_score\": ").append(pr.performanceImpactScore).append(",\n");
            json.append("    \"risk_score\": ").append(pr.riskScore).append(",\n");
            json.append("    \"avg_response_time_ms\": ").append(pr.avgResponseTimeMs).append(",\n");
            json.append("    \"error_rate_increase\": ").append(pr.errorRateIncrease).append(",\n");
            json.append("    \"memory_usage_increase\": ").append(pr.memoryUsageIncrease).append(",\n");
            json.append("    \"cpu_usage_increase\": ").append(pr.cpuUsageIncrease).append("\n");
            json.append("  }");
            if (i < prs.size() - 1) json.append(",");
            json.append("\n");
        }
        
        json.append("]\n");
        
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(json.toString());
        }
        
        System.out.println("📊 Exported " + prs.size() + " PR records to " + filePath);
    }
    
    /**
     * Export System Run dataset for system performance prediction
     */
    private void exportSystemRunDataset(List<SystemRunData> runs, String filePath) throws IOException {
        StringBuilder json = new StringBuilder();
        json.append("[\n");
        
        for (int i = 0; i < runs.size(); i++) {
            SystemRunData run = runs.get(i);
            json.append("  {\n");
            json.append("    \"run_id\": \"").append(run.runId).append("\",\n");
            json.append("    \"run_type\": \"").append(run.runType).append("\",\n");
            json.append("    \"load_intensity\": ").append(run.loadIntensity).append(",\n");
            json.append("    \"total_requests\": ").append(run.totalRequests).append(",\n");
            json.append("    \"avg_cpu_usage\": ").append(run.avgCpuUsage).append(",\n");
            json.append("    \"max_cpu_usage\": ").append(run.maxCpuUsage).append(",\n");
            json.append("    \"avg_memory_usage\": ").append(run.avgMemoryUsage).append(",\n");
            json.append("    \"max_memory_usage\": ").append(run.maxMemoryUsage).append(",\n");
            json.append("    \"avg_disk_usage\": ").append(run.avgDiskUsage).append(",\n");
            json.append("    \"max_disk_usage\": ").append(run.maxDiskUsage).append(",\n");
            json.append("    \"overall_success_rate\": ").append(run.overallSuccessRate).append(",\n");
            json.append("    \"avg_response_time_ms\": ").append(run.avgResponseTimeMs).append(",\n");
            json.append("    \"p95_response_time_ms\": ").append(run.p95ResponseTimeMs).append(",\n");
            json.append("    \"p99_response_time_ms\": ").append(run.p99ResponseTimeMs).append(",\n");
            json.append("    \"throughput_rps\": ").append(run.throughputRps).append(",\n");
            json.append("    \"has_performance_regression\": ").append(run.hasPerformanceRegression).append(",\n");
            json.append("    \"performance_regression_score\": ").append(run.performanceRegressionScore).append(",\n");
            json.append("    \"is_anomalous\": ").append(run.isAnomalous).append(",\n");
            json.append("    \"anomaly_score\": ").append(run.anomalyScore).append("\n");
            json.append("  }");
            if (i < runs.size() - 1) json.append(",");
            json.append("\n");
        }
        
        json.append("]\n");
        
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(json.toString());
        }
        
        System.out.println("📊 Exported " + runs.size() + " system run records to " + filePath);
    }
    
    /**
     * Export Service Metrics dataset for service health prediction
     */
    private void exportServiceMetricsDataset(List<ServiceMetricsData> metrics, String filePath) throws IOException {
        StringBuilder json = new StringBuilder();
        json.append("[\n");
        
        for (int i = 0; i < metrics.size(); i++) {
            ServiceMetricsData metric = metrics.get(i);
            json.append("  {\n");
            json.append("    \"service_name\": \"").append(metric.serviceName).append("\",\n");
            json.append("    \"service_version\": \"").append(metric.serviceVersion).append("\",\n");
            json.append("    \"health_score\": ").append(metric.healthScore).append(",\n");
            json.append("    \"total_requests\": ").append(metric.totalRequests).append(",\n");
            json.append("    \"cpu_usage_percent\": ").append(metric.cpuUsagePercent).append(",\n");
            json.append("    \"memory_usage_mb\": ").append(metric.memoryUsageMB).append(",\n");
            json.append("    \"memory_usage_percent\": ").append(metric.memoryUsagePercent).append(",\n");
            json.append("    \"disk_usage_mb\": ").append(metric.diskUsageMB).append(",\n");
            json.append("    \"disk_usage_percent\": ").append(metric.diskUsagePercent).append(",\n");
            json.append("    \"network_in_mbps\": ").append(metric.networkInMBps).append(",\n");
            json.append("    \"network_out_mbps\": ").append(metric.networkOutMBps).append(",\n");
            json.append("    \"upstream_dependencies\": ").append(metric.upstreamDependencies).append(",\n");
            json.append("    \"downstream_dependencies\": ").append(metric.downstreamDependencies).append(",\n");
            json.append("    \"dependency_health_score\": ").append(metric.dependencyHealthScore).append(",\n");
            json.append("    \"orders_processed\": ").append(metric.ordersProcessed != null ? metric.ordersProcessed : "null").append(",\n");
            json.append("    \"products_viewed\": ").append(metric.productsViewed != null ? metric.productsViewed : "null").append(",\n");
            json.append("    \"users_active\": ").append(metric.usersActive != null ? metric.usersActive : "null").append(",\n");
            json.append("    \"revenue_generated\": ").append(metric.revenueGenerated != null ? metric.revenueGenerated : "null").append(",\n");
            json.append("    \"success_rate\": ").append(metric.successRate).append(",\n");
            json.append("    \"error_rate\": ").append(metric.errorRate).append(",\n");
            json.append("    \"avg_response_time_ms\": ").append(metric.avgResponseTimeMs).append(",\n");
            json.append("    \"p95_response_time_ms\": ").append(metric.p95ResponseTimeMs).append(",\n");
            json.append("    \"p99_response_time_ms\": ").append(metric.p99ResponseTimeMs).append(",\n");
            json.append("    \"throughput_rps\": ").append(metric.throughputRps).append(",\n");
            json.append("    \"is_anomalous\": ").append(metric.isAnomalous).append(",\n");
            json.append("    \"anomaly_score\": ").append(metric.anomalyScore).append(",\n");
            json.append("    \"anomaly_type\": \"").append(metric.anomalyType).append("\",\n");
            json.append("    \"predicted_failure_probability\": ").append(metric.predictedFailureProbability).append("\n");
            json.append("  }");
            if (i < metrics.size() - 1) json.append(",");
            json.append("\n");
        }
        
        json.append("]\n");
        
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(json.toString());
        }
        
        System.out.println("📊 Exported " + metrics.size() + " service metrics records to " + filePath);
    }
    
    /**
     * Export E-commerce Flow dataset for conversion prediction
     */
    private void exportEcommerceFlowDataset(List<EcommerceFlowData> flows, String filePath) throws IOException {
        StringBuilder json = new StringBuilder();
        json.append("[\n");
        
        for (int i = 0; i < flows.size(); i++) {
            EcommerceFlowData flow = flows.get(i);
            json.append("  {\n");
            json.append("    \"flow_id\": \"").append(flow.flowId).append("\",\n");
            json.append("    \"flow_type\": \"").append(flow.flowType).append("\",\n");
            json.append("    \"user_segment\": \"").append(flow.userSegment).append("\",\n");
            json.append("    \"device_type\": \"").append(flow.deviceType).append("\",\n");
            json.append("    \"browser_type\": \"").append(flow.browserType).append("\",\n");
            json.append("    \"location\": \"").append(flow.location).append("\",\n");
            json.append("    \"session_duration_minutes\": ").append(flow.sessionDurationMinutes).append(",\n");
            json.append("    \"items_in_cart\": ").append(flow.itemsInCart).append(",\n");
            json.append("    \"cart_value\": ").append(flow.cartValue).append(",\n");
            json.append("    \"payment_method\": \"").append(flow.paymentMethod).append("\",\n");
            json.append("    \"total_steps\": ").append(flow.totalSteps).append(",\n");
            json.append("    \"successful_steps\": ").append(flow.successfulSteps).append(",\n");
            json.append("    \"failed_steps\": ").append(flow.failedSteps).append(",\n");
            json.append("    \"total_duration_ms\": ").append(flow.totalDurationMs).append(",\n");
            json.append("    \"avg_step_duration_ms\": ").append(flow.avgStepDurationMs).append(",\n");
            json.append("    \"success_rate\": ").append(flow.successRate).append(",\n");
            json.append("    \"converted\": ").append(flow.converted).append(",\n");
            json.append("    \"conversion_value\": ").append(flow.conversionValue).append(",\n");
            json.append("    \"conversion_type\": \"").append(flow.conversionType).append("\",\n");
            json.append("    \"final_order_value\": ").append(flow.finalOrderValue).append(",\n");
            json.append("    \"payment_successful\": ").append(flow.paymentSuccessful).append(",\n");
            json.append("    \"abandonment_probability\": ").append(flow.abandonmentProbability).append(",\n");
            json.append("    \"is_anomalous\": ").append(flow.isAnomalous).append(",\n");
            json.append("    \"anomaly_score\": ").append(flow.anomalyScore).append(",\n");
            json.append("    \"anomaly_type\": \"").append(flow.anomalyType).append("\"\n");
            json.append("  }");
            if (i < flows.size() - 1) json.append(",");
            json.append("\n");
        }
        
        json.append("]\n");
        
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(json.toString());
        }
        
        System.out.println("📊 Exported " + flows.size() + " e-commerce flow records to " + filePath);
    }
    
    /**
     * Export combined dataset with all entities joined
     */
    private void exportCombinedDataset(List<PullRequestData> prs, List<SystemRunData> runs, 
                                     List<ServiceMetricsData> metrics, String filePath) throws IOException {
        StringBuilder json = new StringBuilder();
        json.append("[\n");
        
        int recordCount = 0;
        for (SystemRunData run : runs) {
            for (ServiceMetricsData metric : run.serviceMetrics) {
                if (recordCount > 0) json.append(",\n");
                json.append("  {\n");
                json.append("    \"pr_lines_added\": ").append(run.pullRequest.linesAdded).append(",\n");
                json.append("    \"pr_test_coverage\": ").append(run.pullRequest.testCoverage).append(",\n");
                json.append("    \"pr_complexity\": ").append(run.pullRequest.codeComplexity).append(",\n");
                json.append("    \"pr_risk_score\": ").append(run.pullRequest.riskScore).append(",\n");
                json.append("    \"pr_has_performance_regression\": ").append(run.pullRequest.hasPerformanceRegression).append(",\n");
                json.append("    \"run_type\": \"").append(run.runType).append("\",\n");
                json.append("    \"run_load_intensity\": ").append(run.loadIntensity).append(",\n");
                json.append("    \"run_total_requests\": ").append(run.totalRequests).append(",\n");
                json.append("    \"service_name\": \"").append(metric.serviceName).append("\",\n");
                json.append("    \"service_health_score\": ").append(metric.healthScore).append(",\n");
                json.append("    \"service_cpu_usage\": ").append(metric.cpuUsagePercent).append(",\n");
                json.append("    \"service_memory_usage\": ").append(metric.memoryUsagePercent).append(",\n");
                json.append("    \"service_dependencies\": ").append(metric.upstreamDependencies + metric.downstreamDependencies).append(",\n");
                json.append("    \"service_success_rate\": ").append(metric.successRate).append(",\n");
                json.append("    \"service_response_time\": ").append(metric.avgResponseTimeMs).append(",\n");
                json.append("    \"service_is_anomalous\": ").append(metric.isAnomalous).append(",\n");
                json.append("    \"service_failure_probability\": ").append(metric.predictedFailureProbability).append("\n");
                json.append("  }");
                recordCount++;
            }
        }
        
        json.append("\n]\n");
        
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(json.toString());
        }
        
        System.out.println("📊 Exported " + recordCount + " combined records to " + filePath);
    }
    
    /**
     * Export dataset statistics for analysis
     */
    private void exportDatasetStatistics(List<PullRequestData> prs, List<SystemRunData> runs, 
                                       List<ServiceMetricsData> metrics, List<EcommerceFlowData> flows, 
                                       String filePath) throws IOException {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"dataset_summary\": {\n");
        json.append("    \"total_pull_requests\": ").append(prs.size()).append(",\n");
        json.append("    \"total_system_runs\": ").append(runs.size()).append(",\n");
        json.append("    \"total_service_metrics\": ").append(metrics.size()).append(",\n");
        json.append("    \"total_ecommerce_flows\": ").append(flows.size()).append("\n");
        json.append("  },\n");
        
        // Performance regression stats
        long prsWithRegression = prs.stream().mapToLong(pr -> pr.hasPerformanceRegression ? 1 : 0).sum();
        long runsWithRegression = runs.stream().mapToLong(run -> run.hasPerformanceRegression ? 1 : 0).sum();
        json.append("  \"performance_regression_stats\": {\n");
        json.append("    \"prs_with_performance_regression\": ").append(prsWithRegression).append(",\n");
        json.append("    \"runs_with_performance_regression\": ").append(runsWithRegression).append(",\n");
        json.append("    \"pr_regression_rate\": ").append(String.format("%.2f", (double) prsWithRegression / prs.size() * 100)).append("%,\n");
        json.append("    \"run_regression_rate\": ").append(String.format("%.2f", (double) runsWithRegression / runs.size() * 100)).append("%\n");
        json.append("  },\n");
        
        // Anomaly stats
        long anomalousRuns = runs.stream().mapToLong(run -> run.isAnomalous ? 1 : 0).sum();
        long anomalousMetrics = metrics.stream().mapToLong(metric -> metric.isAnomalous ? 1 : 0).sum();
        long anomalousFlows = flows.stream().mapToLong(flow -> flow.isAnomalous ? 1 : 0).sum();
        json.append("  \"anomaly_stats\": {\n");
        json.append("    \"anomalous_runs\": ").append(anomalousRuns).append(",\n");
        json.append("    \"anomalous_metrics\": ").append(anomalousMetrics).append(",\n");
        json.append("    \"anomalous_flows\": ").append(anomalousFlows).append(",\n");
        json.append("    \"run_anomaly_rate\": ").append(String.format("%.2f", (double) anomalousRuns / runs.size() * 100)).append("%,\n");
        json.append("    \"metric_anomaly_rate\": ").append(String.format("%.2f", (double) anomalousMetrics / metrics.size() * 100)).append("%,\n");
        json.append("    \"flow_anomaly_rate\": ").append(String.format("%.2f", (double) anomalousFlows / flows.size() * 100)).append("%\n");
        json.append("  },\n");
        
        // E-commerce stats
        long convertedFlows = flows.stream().mapToLong(flow -> flow.converted ? 1 : 0).sum();
        long highValueFlows = flows.stream().mapToLong(flow -> flow.cartValue > 100 ? 1 : 0).sum();
        json.append("  \"ecommerce_stats\": {\n");
        json.append("    \"converted_flows\": ").append(convertedFlows).append(",\n");
        json.append("    \"high_value_flows\": ").append(highValueFlows).append(",\n");
        json.append("    \"conversion_rate\": ").append(String.format("%.2f", (double) convertedFlows / flows.size() * 100)).append("%,\n");
        json.append("    \"high_value_rate\": ").append(String.format("%.2f", (double) highValueFlows / flows.size() * 100)).append("%\n");
        json.append("  },\n");
        
        // Service-specific stats
        json.append("  \"service_stats\": {\n");
        Map<String, Long> serviceCounts = new HashMap<>();
        Map<String, Double> serviceAvgSuccess = new HashMap<>();
        Map<String, Double> serviceAvgResponseTime = new HashMap<>();
        
        for (ServiceMetricsData metric : metrics) {
            serviceCounts.merge(metric.serviceName, 1L, Long::sum);
            serviceAvgSuccess.merge(metric.serviceName, metric.successRate, Double::sum);
            serviceAvgResponseTime.merge(metric.serviceName, metric.avgResponseTimeMs, Double::sum);
        }
        
        for (String service : serviceCounts.keySet()) {
            long count = serviceCounts.get(service);
            double avgSuccess = serviceAvgSuccess.get(service) / count;
            double avgResponseTime = serviceAvgResponseTime.get(service) / count;
            json.append("    \"").append(service.replace("-", "_")).append("\": {\n");
            json.append("      \"total_metrics\": ").append(count).append(",\n");
            json.append("      \"avg_success_rate\": ").append(String.format("%.3f", avgSuccess)).append(",\n");
            json.append("      \"avg_response_time\": ").append(String.format("%.2f", avgResponseTime)).append("\n");
            json.append("    }");
            if (!service.equals(serviceCounts.keySet().toArray()[serviceCounts.size() - 1])) {
                json.append(",");
            }
            json.append("\n");
        }
        json.append("  }\n");
        json.append("}\n");
        
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(json.toString());
        }
        
        System.out.println("📊 Exported dataset statistics to " + filePath);
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        MLDatasetExporter exporter = new MLDatasetExporter();
        
        int prCount = args.length > 0 ? Integer.parseInt(args[0]) : 1000;
        int runsPerPR = args.length > 1 ? Integer.parseInt(args[1]) : 5;
        String outputPath = args.length > 2 ? args[2] : "./ml-datasets";
        
        System.out.println("🎯 ML Dataset Exporter for AI/ML Model Training");
        System.out.println("===============================================");
        System.out.println("Generating " + prCount + " PRs with " + runsPerPR + " runs each");
        System.out.println("Output directory: " + outputPath);
        System.out.println();
        
        long startTime = System.currentTimeMillis();
        exporter.generateAndExportMLDatasets(prCount, runsPerPR, outputPath);
        long endTime = System.currentTimeMillis();
        
        System.out.println();
        System.out.println("✅ ML dataset generation and export completed in " + (endTime - startTime) + " ms");
        System.out.println("📊 Generated datasets ready for ML model training:");
        System.out.println("   - pr_dataset.json: PR impact prediction");
        System.out.println("   - system_run_dataset.json: System performance prediction");
        System.out.println("   - service_metrics_dataset.json: Service health prediction");
        System.out.println("   - ecommerce_flow_dataset.json: Conversion prediction");
        System.out.println("   - combined_dataset.json: Multi-entity analysis");
        System.out.println("   - dataset_statistics.json: Data quality metrics");
    }
}
