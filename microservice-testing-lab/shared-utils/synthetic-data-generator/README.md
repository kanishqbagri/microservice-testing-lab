# 🎯 Synthetic Data Generator for AI/ML Model Training

A comprehensive test container-based synthetic data generator designed to create realistic datasets for training AI/ML models in microservice environments. This tool generates ~50k synthetic PRs, system runs, and e-commerce flows with controlled noise and failure patterns.

## 🏗️ Architecture Overview

```
synthetic-data-generator/
├── src/main/java/com/kb/synthetic/
│   ├── model/                    # Data models for synthetic entities
│   │   ├── PullRequest.java     # PR data with performance impact
│   │   ├── SystemRun.java       # System run metrics and results
│   │   ├── ServiceMetrics.java  # Service-level performance metrics
│   │   ├── EcommerceFlow.java   # E-commerce user journey flows
│   │   ├── FlowStep.java        # Individual flow steps
│   │   └── enums/               # Supporting enumerations
│   ├── repository/              # JPA repositories for data access
│   ├── service/                 # Core business logic
│   │   ├── SyntheticDataGeneratorService.java
│   │   └── MLDatasetExporterService.java
│   ├── config/                  # Configuration classes
│   └── SyntheticDataGeneratorApplication.java
├── src/test/java/               # TestContainers-based tests
└── src/main/resources/          # Configuration files
```

## 🚀 Quick Start

### Prerequisites

- **Java 17+**
- **Maven 3.8+**
- **PostgreSQL 15+** (for production)
- **Docker** (for TestContainers)

### Installation

1. **Clone and Build**
   ```bash
   cd microservice-testing-lab/shared-utils/synthetic-data-generator
   mvn clean install
   ```

2. **Run with Default Settings**
   ```bash
   mvn spring-boot:run -Dspring-boot.run.arguments="generate 1000 5 ./ml-datasets"
   ```

3. **Run Tests**
   ```bash
   mvn test
   ```

## 📊 Generated Data Overview

### 1. Pull Request Data (~50k records)
- **Code Quality Metrics**: Lines added/deleted, complexity, test coverage
- **Performance Impact**: Response time changes, error rate increases
- **Service Impact**: Multi-service dependency analysis
- **Risk Assessment**: Calculated risk scores and regression indicators
- **Change Classification**: API changes, DB changes, security updates

### 2. System Run Data (~250k records)
- **Performance Metrics**: Response times, throughput, success rates
- **Resource Utilization**: CPU, memory, disk usage patterns
- **Error Analysis**: Categorized error breakdowns
- **Anomaly Detection**: Flagged anomalous runs with scores
- **Load Testing**: Various load intensities and test types

### 3. Service Metrics Data (~1.25M records)
- **Health Monitoring**: Service health scores and status
- **Performance Tracking**: Response times, error rates, throughput
- **Resource Monitoring**: CPU, memory, disk, network metrics
- **Business Metrics**: Orders processed, revenue, user activity
- **Dependency Analysis**: Upstream/downstream service health

### 4. E-commerce Flow Data (~750k records)
- **User Journeys**: Complete shopping cart to checkout flows
- **Conversion Tracking**: Purchase completion and abandonment
- **User Segmentation**: New users, returning users, VIP users
- **Device Analytics**: Mobile, desktop, tablet usage patterns
- **Geographic Data**: Location-based performance variations

### 5. Flow Step Data (~6M records)
- **Step-by-Step Analysis**: Individual API call performance
- **Error Tracking**: Step-level failure analysis
- **Business Context**: Action-specific performance metrics
- **Retry Logic**: Retry attempts and failure patterns

## 🎛️ Configuration

### Application Configuration (`application.yml`)

```yaml
synthetic:
  data:
    generation:
      default-pr-count: 1000        # Number of PRs to generate
      default-runs-per-pr: 5        # System runs per PR
      default-flows-per-run: 30     # E-commerce flows per run
      default-steps-per-flow: 8     # Steps per flow
      enable-parallel-generation: true
      batch-size: 100
    
    performance:
      baseline-response-time-ms: 200.0
      max-response-time-ms: 5000.0
      baseline-success-rate: 0.95
      min-success-rate: 0.7
    
    ecommerce:
      conversion-rate: 0.3          # 30% conversion rate
      cart-abandonment-rate: 0.7    # 70% abandonment rate
      payment-success-rate: 0.95    # 95% payment success
    
    noise:
      performance-regression-rate: 0.2  # 20% of PRs cause degradation
      anomaly-rate: 0.1                 # 10% of runs are anomalous
      failure-rate: 0.05                # 5% base failure rate
```

## 🔧 Usage Examples

### 1. Generate Small Dataset for Testing
```bash
java -jar target/synthetic-data-generator-1.0.0.jar generate 100 3 ./test-datasets
```

### 2. Generate Large Dataset for Production Training
```bash
java -jar target/synthetic-data-generator-1.0.0.jar generate 50000 10 ./production-datasets
```

### 3. Programmatic Usage
```java
@Autowired
private SyntheticDataGeneratorService generatorService;

@Autowired
private MLDatasetExporterService exporterService;

// Generate data
generatorService.generateSyntheticData(1000, 5);

// Export ML datasets
exporterService.exportMLDataset("./ml-datasets");
```

## 📈 ML Dataset Exports

The tool exports multiple ML-ready datasets:

### 1. **PR Dataset** (`pr_dataset.json`)
- **Purpose**: PR impact prediction
- **Features**: Code metrics, complexity, test coverage
- **Labels**: Performance regression, risk scores

### 2. **System Run Dataset** (`system_run_dataset.json`)
- **Purpose**: System performance prediction
- **Features**: Load intensity, resource usage, error patterns
- **Labels**: Success rates, response times, anomalies

### 3. **Service Metrics Dataset** (`service_metrics_dataset.json`)
- **Purpose**: Service health prediction
- **Features**: Resource utilization, dependency health
- **Labels**: Success rates, failure probabilities

### 4. **E-commerce Flow Dataset** (`ecommerce_flow_dataset.json`)
- **Purpose**: Conversion prediction
- **Features**: User behavior, device type, cart value
- **Labels**: Conversion rates, abandonment probability

### 5. **Combined Dataset** (`combined_dataset.json`)
- **Purpose**: Multi-entity analysis
- **Features**: Joined PR, run, and service data
- **Labels**: Cross-entity performance indicators

### 6. **Time Series Dataset** (`time_series_dataset.json`)
- **Purpose**: Temporal analysis
- **Features**: Historical metrics, trend analysis
- **Labels**: Future performance predictions

## 🧪 Testing with TestContainers

The project includes comprehensive TestContainers-based tests:

```java
@Testcontainers
@DataJpaTest
public class SyntheticDataGeneratorTest {
    
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");
    
    @Test
    void shouldGenerateSyntheticPullRequests() {
        // Test PR generation
    }
    
    @Test
    void shouldGeneratePerformanceRegressionData() {
        // Test 20% performance regression injection
    }
    
    @Test
    void shouldGenerateAnomalousData() {
        // Test anomaly generation
    }
}
```

## 📊 Data Quality Features

### 1. **Realistic Distributions**
- Response times follow log-normal distributions
- Error rates follow beta distributions
- Resource usage follows realistic patterns

### 2. **Controlled Noise Injection**
- 20% of PRs lead to performance degradation
- 10% of runs are flagged as anomalous
- 5% base failure rate across services

### 3. **Correlated Data**
- Service metrics correlate with system runs
- E-commerce flows correlate with service performance
- PR impact correlates with code complexity

### 4. **Temporal Patterns**
- Performance degrades during peak hours
- Error rates increase with load
- Resource usage follows realistic cycles

## 🎯 ML Model Training Use Cases

### 1. **Performance Regression Prediction**
- **Input**: PR code metrics, service impact scores
- **Output**: Probability of performance regression
- **Use Case**: Pre-merge performance impact assessment

### 2. **Service Health Prediction**
- **Input**: Current metrics, historical trends
- **Output**: Future health scores, failure probability
- **Use Case**: Proactive service monitoring

### 3. **E-commerce Conversion Prediction**
- **Input**: User behavior, cart contents, device type
- **Output**: Conversion probability, abandonment risk
- **Use Case**: Real-time conversion optimization

### 4. **Anomaly Detection**
- **Input**: Multi-dimensional service metrics
- **Output**: Anomaly scores, anomaly types
- **Use Case**: Automated incident detection

### 5. **Load Testing Optimization**
- **Input**: Historical performance data
- **Output**: Optimal load patterns, capacity planning
- **Use Case**: Infrastructure scaling decisions

## 🔍 Data Analysis Examples

### Performance Regression Analysis
```python
import pandas as pd
import numpy as np

# Load PR dataset
df = pd.read_json('ml-datasets/pr_dataset.json')

# Analyze performance regression patterns
regression_prs = df[df['has_performance_regression'] == True]
print(f"Performance regression rate: {len(regression_prs) / len(df):.2%}")

# Feature importance for regression prediction
features = ['lines_added', 'code_complexity', 'test_coverage', 'cyclomatic_complexity']
X = df[features]
y = df['has_performance_regression']

# Train model and analyze feature importance
from sklearn.ensemble import RandomForestClassifier
model = RandomForestClassifier()
model.fit(X, y)
feature_importance = pd.DataFrame({
    'feature': features,
    'importance': model.feature_importances_
}).sort_values('importance', ascending=False)
```

### Service Health Analysis
```python
# Load service metrics dataset
metrics_df = pd.read_json('ml-datasets/service_metrics_dataset.json')

# Analyze service-specific patterns
service_analysis = metrics_df.groupby('service_name').agg({
    'success_rate': ['mean', 'std'],
    'avg_response_time_ms': ['mean', 'std'],
    'cpu_usage_percent': ['mean', 'std'],
    'is_anomalous': 'sum'
}).round(3)

print("Service Performance Analysis:")
print(service_analysis)
```

## 🚀 Performance Characteristics

### Generation Performance
- **Small Dataset** (1k PRs, 5k runs): ~2 minutes
- **Medium Dataset** (10k PRs, 50k runs): ~15 minutes
- **Large Dataset** (50k PRs, 250k runs): ~2 hours

### Memory Usage
- **Peak Memory**: ~2GB for large datasets
- **Database Size**: ~500MB for 50k PRs
- **Export Size**: ~100MB JSON files

### Scalability
- **Parallel Generation**: Configurable batch processing
- **Database Optimization**: Batch inserts, connection pooling
- **Memory Management**: Streaming for large datasets

## 🔧 Customization

### Custom Data Patterns
```java
// Extend the generator service
@Service
public class CustomDataGeneratorService extends SyntheticDataGeneratorService {
    
    @Override
    protected String generatePRTitle() {
        // Custom PR title generation logic
        return "Custom: " + super.generatePRTitle();
    }
    
    @Override
    protected Double calculateRiskScore() {
        // Custom risk calculation
        return customRiskAlgorithm();
    }
}
```

### Custom Export Formats
```java
// Add custom export format
public void exportToCSV(String filePath) throws IOException {
    List<PullRequest> prs = pullRequestRepository.findAll();
    
    try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
        // Write CSV headers
        writer.writeNext(new String[]{"pr_number", "risk_score", "performance_impact"});
        
        // Write data rows
        for (PullRequest pr : prs) {
            writer.writeNext(new String[]{
                pr.getPrNumber(),
                pr.getRiskScore().toString(),
                pr.getPerformanceImpactScore().toString()
            });
        }
    }
}
```

## 📚 API Reference

### SyntheticDataGeneratorService
```java
// Generate synthetic data
void generateSyntheticData(int prCount, int runsPerPR)

// Generate specific data types
List<PullRequest> generatePullRequests(int count)
List<SystemRun> generateSystemRuns(PullRequest pr, int count)
void generateServiceMetrics(SystemRun run)
void generateEcommerceFlows(SystemRun run)
```

### MLDatasetExporterService
```java
// Export all datasets
void exportMLDataset(String outputPath)

// Export specific datasets
void exportPullRequestDataset(String filePath)
void exportSystemRunDataset(String filePath)
void exportServiceMetricsDataset(String filePath)
void exportEcommerceFlowDataset(String filePath)
void exportCombinedDataset(String filePath)
void exportTimeSeriesDataset(String filePath)

// Export statistics
void exportDatasetStatistics(String outputPath)
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Add tests for new functionality
4. Ensure all tests pass
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Support

For questions and support:
- Create an issue in the repository
- Check the documentation
- Review the test examples

---

**Built with ❤️ for AI/ML model training in microservice environments**
