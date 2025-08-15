package com.kb.jarvis.core.model;

public enum LearningDataType {
    TEST_EXECUTION("Test Execution", "Data from test execution patterns"),
    FAILURE_ANALYSIS("Failure Analysis", "Data from test failure analysis"),
    PERFORMANCE_METRICS("Performance Metrics", "Data from performance measurements"),
    OPTIMIZATION_RESULT("Optimization Result", "Data from test optimization attempts"),
    PATTERN_RECOGNITION("Pattern Recognition", "Data for pattern recognition training"),
    PREDICTION_MODEL("Prediction Model", "Data for prediction model training"),
    CLASSIFICATION("Classification", "Data for classification model training"),
    REGRESSION("Regression", "Data for regression model training"),
    CLUSTERING("Clustering", "Data for clustering analysis"),
    ANOMALY_DETECTION("Anomaly Detection", "Data for anomaly detection training"),
    RECOMMENDATION("Recommendation", "Data for recommendation system training"),
    NATURAL_LANGUAGE("Natural Language", "Data for NLP model training"),
    INTERACTION("Interaction", "Data from user interactions"),
    SERVICE_USAGE("Service Usage", "Data from service usage patterns");

    private final String displayName;
    private final String description;

    LearningDataType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}
