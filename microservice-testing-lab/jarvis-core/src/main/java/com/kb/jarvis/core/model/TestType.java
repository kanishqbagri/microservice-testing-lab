package com.kb.jarvis.core.model;

public enum TestType {
    UNIT_TEST("Unit Test", "Individual component testing"),
    INTEGRATION_TEST("Integration Test", "Component interaction testing"),
    CONTRACT_TEST("Contract Test", "Service contract validation"),
    API_TEST("API Test", "REST API endpoint testing"),
    PERFORMANCE_TEST("Performance Test", "Load and stress testing"),
    SECURITY_TEST("Security Test", "Security vulnerability testing"),
    CHAOS_TEST("Chaos Test", "Resilience and failure testing"),
    PENETRATION_TEST("Penetration Test", "Security penetration testing"),
    END_TO_END_TEST("End-to-End Test", "Complete workflow testing"),
    SMOKE_TEST("Smoke Test", "Basic functionality verification"),
    REGRESSION_TEST("Regression Test", "Regression detection testing"),
    EXPLORATORY_TEST("Exploratory Test", "Ad-hoc testing"),
    ACCESSIBILITY_TEST("Accessibility Test", "Accessibility compliance testing"),
    COMPATIBILITY_TEST("Compatibility Test", "Cross-platform compatibility testing"),
    LOCALIZATION_TEST("Localization Test", "Internationalization testing");

    private final String displayName;
    private final String description;

    TestType(String displayName, String description) {
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
