package com.kb.synthetic;

import com.kb.synthetic.model.*;
import com.kb.synthetic.model.enums.*;
import com.kb.synthetic.repository.*;
import com.kb.synthetic.service.SyntheticDataGeneratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
public class SyntheticDataGeneratorTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("synthetic_test")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PullRequestRepository pullRequestRepository;

    @Autowired
    private SystemRunRepository systemRunRepository;

    @Autowired
    private ServiceMetricsRepository serviceMetricsRepository;

    @Autowired
    private EcommerceFlowRepository ecommerceFlowRepository;

    @Autowired
    private FlowStepRepository flowStepRepository;

    private SyntheticDataGeneratorService generatorService;

    @BeforeEach
    void setUp() {
        generatorService = new SyntheticDataGeneratorService(
            pullRequestRepository,
            systemRunRepository,
            serviceMetricsRepository,
            ecommerceFlowRepository,
            flowStepRepository,
            null // config will be null for this test
        );
    }

    @Test
    void shouldGenerateSyntheticPullRequests() {
        // Given
        int prCount = 10;
        int runsPerPR = 3;

        // When
        generatorService.generateSyntheticData(prCount, runsPerPR);

        // Then
        List<PullRequest> prs = pullRequestRepository.findAll();
        assertThat(prs).hasSize(prCount);

        List<SystemRun> runs = systemRunRepository.findAll();
        assertThat(runs).hasSize(prCount * runsPerPR);

        // Verify PR characteristics
        PullRequest pr = prs.get(0);
        assertThat(pr.getPrNumber()).isNotNull();
        assertThat(pr.getTitle()).isNotNull();
        assertThat(pr.getAuthor()).isNotNull();
        assertThat(pr.getLinesAdded()).isGreaterThan(0);
        assertThat(pr.getTestCoverage()).isBetween(0.0, 1.0);
        assertThat(pr.getRiskScore()).isBetween(0.0, 1.0);
    }

    @Test
    void shouldGenerateSystemRunsWithMetrics() {
        // Given
        int prCount = 5;
        int runsPerPR = 2;

        // When
        generatorService.generateSyntheticData(prCount, runsPerPR);

        // Then
        List<SystemRun> runs = systemRunRepository.findAll();
        assertThat(runs).hasSize(prCount * runsPerPR);

        List<ServiceMetrics> metrics = serviceMetricsRepository.findAll();
        assertThat(metrics).hasSize(runs.size() * 5); // 5 services per run

        // Verify run characteristics
        SystemRun run = runs.get(0);
        assertThat(run.getRunId()).isNotNull();
        assertThat(run.getOverallSuccessRate()).isBetween(0.0, 1.0);
        assertThat(run.getTotalRequests()).isGreaterThan(0);
        assertThat(run.getAvgResponseTimeMs()).isGreaterThan(0);
    }

    @Test
    void shouldGenerateEcommerceFlows() {
        // Given
        int prCount = 3;
        int runsPerPR = 2;

        // When
        generatorService.generateSyntheticData(prCount, runsPerPR);

        // Then
        List<EcommerceFlow> flows = ecommerceFlowRepository.findAll();
        assertThat(flows).isNotEmpty();

        List<FlowStep> steps = flowStepRepository.findAll();
        assertThat(steps).isNotEmpty();

        // Verify flow characteristics
        EcommerceFlow flow = flows.get(0);
        assertThat(flow.getFlowId()).isNotNull();
        assertThat(flow.getUserId()).isNotNull();
        assertThat(flow.getFlowType()).isNotNull();
        assertThat(flow.getSuccessRate()).isBetween(0.0, 1.0);
        assertThat(flow.getCartValue()).isGreaterThan(0);
    }

    @Test
    void shouldGeneratePerformanceRegressionData() {
        // Given
        int prCount = 20;
        int runsPerPR = 1;

        // When
        generatorService.generateSyntheticData(prCount, runsPerPR);

        // Then
        List<PullRequest> prsWithRegression = pullRequestRepository.findByHasPerformanceRegressionTrue();
        assertThat(prsWithRegression).isNotEmpty();

        List<SystemRun> runsWithRegression = systemRunRepository.findByHasPerformanceRegressionTrue();
        assertThat(runsWithRegression).isNotEmpty();

        // Verify regression characteristics
        PullRequest pr = prsWithRegression.get(0);
        assertThat(pr.getHasPerformanceRegression()).isTrue();
        assertThat(pr.getPerformanceImpactScore()).isNotNull();
    }

    @Test
    void shouldGenerateAnomalousData() {
        // Given
        int prCount = 50;
        int runsPerPR = 1;

        // When
        generatorService.generateSyntheticData(prCount, runsPerPR);

        // Then
        List<SystemRun> anomalousRuns = systemRunRepository.findByIsAnomalousTrue();
        assertThat(anomalousRuns).isNotEmpty();

        List<ServiceMetrics> anomalousMetrics = serviceMetricsRepository.findByIsAnomalousTrue();
        assertThat(anomalousMetrics).isNotEmpty();

        List<EcommerceFlow> anomalousFlows = ecommerceFlowRepository.findByIsAnomalousTrue();
        assertThat(anomalousFlows).isNotEmpty();

        // Verify anomaly characteristics
        SystemRun run = anomalousRuns.get(0);
        assertThat(run.getIsAnomalous()).isTrue();
        assertThat(run.getAnomalyScore()).isBetween(0.0, 1.0);
    }

    @Test
    void shouldGenerateRealisticEcommerceScenarios() {
        // Given
        int prCount = 10;
        int runsPerPR = 1;

        // When
        generatorService.generateSyntheticData(prCount, runsPerPR);

        // Then
        List<EcommerceFlow> convertedFlows = ecommerceFlowRepository.findByConvertedTrue();
        assertThat(convertedFlows).isNotEmpty();

        List<EcommerceFlow> highValueFlows = ecommerceFlowRepository.findByHighCartValue(100.0);
        assertThat(highValueFlows).isNotEmpty();

        // Verify e-commerce characteristics
        EcommerceFlow flow = convertedFlows.get(0);
        assertThat(flow.getConverted()).isTrue();
        assertThat(flow.getConversionValue()).isGreaterThan(0);
        assertThat(flow.getPaymentSuccessful()).isTrue();
    }

    @Test
    void shouldGenerateServiceSpecificMetrics() {
        // Given
        int prCount = 5;
        int runsPerPR = 1;

        // When
        generatorService.generateSyntheticData(prCount, runsPerPR);

        // Then
        List<ServiceMetrics> orderServiceMetrics = serviceMetricsRepository.findByServiceName("order-service");
        assertThat(orderServiceMetrics).isNotEmpty();

        List<ServiceMetrics> userServiceMetrics = serviceMetricsRepository.findByServiceName("user-service");
        assertThat(userServiceMetrics).isNotEmpty();

        // Verify service-specific business metrics
        ServiceMetrics orderMetrics = orderServiceMetrics.get(0);
        assertThat(orderMetrics.getOrdersProcessed()).isNotNull();
        assertThat(orderMetrics.getRevenueGenerated()).isNotNull();

        ServiceMetrics userMetrics = userServiceMetrics.get(0);
        assertThat(userMetrics.getUsersActive()).isNotNull();
    }

    @Test
    void shouldGenerateFlowStepsWithRealisticPatterns() {
        // Given
        int prCount = 3;
        int runsPerPR = 1;

        // When
        generatorService.generateSyntheticData(prCount, runsPerPR);

        // Then
        List<FlowStep> paymentSteps = flowStepRepository.findByBusinessAction("PAYMENT_PROCESS");
        assertThat(paymentSteps).isNotEmpty();

        List<FlowStep> cartSteps = flowStepRepository.findByBusinessAction("ADD_TO_CART");
        assertThat(cartSteps).isNotEmpty();

        // Verify step characteristics
        FlowStep step = paymentSteps.get(0);
        assertThat(step.getServiceName()).isEqualTo("order-service");
        assertThat(step.getHttpMethod()).isEqualTo("POST");
        assertThat(step.getResponseTimeMs()).isGreaterThan(0);
    }
}
