package com.kb.jarvis.core.model;

/**
 * Enum representing the level of impact of changes
 */
public enum ImpactLevel {
    NONE("None", "No impact"),
    LOW("Low", "Minimal impact"),
    MEDIUM("Medium", "Moderate impact"),
    HIGH("High", "Significant impact"),
    CRITICAL("Critical", "Critical impact");

    private final String displayName;
    private final String description;

    ImpactLevel(String displayName, String description) {
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
