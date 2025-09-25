package com.kb.synthetic.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "synthetic.data")
@Data
public class SyntheticDataConfig {
    
    private GenerationConfig generation = new GenerationConfig();
    private PerformanceConfig performance = new PerformanceConfig();
    private EcommerceConfig ecommerce = new EcommerceConfig();
    private NoiseConfig noise = new NoiseConfig();
    
    @Data
    public static class GenerationConfig {
        private int defaultPrCount = 1000;
        private int defaultRunsPerPr = 5;
        private int defaultFlowsPerRun = 30;
        private int defaultStepsPerFlow = 8;
        private boolean enableParallelGeneration = true;
        private int batchSize = 100;
    }
    
    @Data
    public static class PerformanceConfig {
        private double baselineResponseTimeMs = 200.0;
        private double maxResponseTimeMs = 5000.0;
        private double baselineSuccessRate = 0.95;
        private double minSuccessRate = 0.7;
        private double baselineCpuUsage = 0.3;
        private double maxCpuUsage = 0.9;
        private double baselineMemoryUsage = 0.4;
        private double maxMemoryUsage = 0.8;
    }
    
    @Data
    public static class EcommerceConfig {
        private double conversionRate = 0.3;
        private double cartAbandonmentRate = 0.7;
        private double paymentSuccessRate = 0.95;
        private double averageCartValue = 150.0;
        private double maxCartValue = 2000.0;
        private int averageItemsPerCart = 3;
        private int maxItemsPerCart = 15;
        private Map<String, Double> flowTypeSuccessRates = Map.of(
            "PRODUCT_BROWSE", 0.98,
            "ADD_TO_CART", 0.95,
            "CHECKOUT", 0.85,
            "PAYMENT", 0.92,
            "ORDER_CONFIRMATION", 0.99
        );
    }
    
    @Data
    public static class NoiseConfig {
        private double performanceRegressionRate = 0.2; // 20% of PRs lead to degraded performance
        private double anomalyRate = 0.1; // 10% of runs are anomalous
        private double failureRate = 0.05; // 5% base failure rate
        private double securityVulnerabilityRate = 0.05; // 5% of PRs have security issues
        private double breakingChangeRate = 0.1; // 10% of PRs have breaking changes
        private Map<String, Double> serviceFailureRates = Map.of(
            "gateway-service", 0.02,
            "user-service", 0.03,
            "product-service", 0.025,
            "order-service", 0.04,
            "notification-service", 0.035
        );
        private Map<String, Double> errorTypeProbabilities = Map.of(
            "TIMEOUT", 0.3,
            "VALIDATION_ERROR", 0.25,
            "SERVICE_UNAVAILABLE", 0.2,
            "AUTHENTICATION_ERROR", 0.15,
            "RATE_LIMIT_EXCEEDED", 0.1
        );
    }
}
