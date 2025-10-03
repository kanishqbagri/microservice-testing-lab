package com.kb.jarvis.core.model;

/**
 * Enum representing the complexity level of a file change
 */
public enum ChangeComplexity {
    LOW("Low", "Simple changes with minimal impact"),
    MEDIUM("Medium", "Moderate changes with some impact"),
    HIGH("High", "Complex changes with significant impact"),
    CRITICAL("Critical", "Critical changes with major impact");

    private final String displayName;
    private final String description;

    ChangeComplexity(String displayName, String description) {
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
