package com.kb.jarvis.core.model;

public enum PluginType {
    TEST_EXECUTOR("Test Executor", "Executes specific types of tests"),
    TEST_GENERATOR("Test Generator", "Generates test cases automatically"),
    TEST_ANALYZER("Test Analyzer", "Analyzes test results and failures"),
    PERFORMANCE_MONITOR("Performance Monitor", "Monitors system performance"),
    SECURITY_SCANNER("Security Scanner", "Performs security scanning"),
    CHAOS_ENGINE("Chaos Engine", "Executes chaos engineering tests"),
    DATA_GENERATOR("Data Generator", "Generates test data"),
    REPORT_GENERATOR("Report Generator", "Generates test reports"),
    NOTIFICATION_SENDER("Notification Sender", "Sends notifications"),
    INTEGRATION_ADAPTER("Integration Adapter", "Integrates with external systems"),
    CUSTOM_AGENT("Custom Agent", "Custom testing agent"),
    UTILITY("Utility", "Utility functions and tools");

    private final String displayName;
    private final String description;

    PluginType(String displayName, String description) {
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
