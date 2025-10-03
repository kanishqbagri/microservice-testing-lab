package com.kb.jarvis.core.model;

/**
 * Enum representing the priority level of a test recommendation
 */
public enum PriorityLevel {
    MINIMAL("Minimal", "Lowest priority"),
    LOW("Low", "Low priority"),
    MEDIUM("Medium", "Medium priority"),
    HIGH("High", "High priority"),
    CRITICAL("Critical", "Critical priority");

    private final String displayName;
    private final String description;

    PriorityLevel(String displayName, String description) {
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
