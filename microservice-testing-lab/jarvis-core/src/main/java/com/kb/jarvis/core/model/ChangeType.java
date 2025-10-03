package com.kb.jarvis.core.model;

/**
 * Enum representing the type of change made to a file
 */
public enum ChangeType {
    ADDED("Added", "New file added"),
    MODIFIED("Modified", "Existing file modified"),
    DELETED("Deleted", "File deleted"),
    RENAMED("Renamed", "File renamed"),
    MOVED("Moved", "File moved to different location"),
    COPIED("Copied", "File copied"),
    UNKNOWN("Unknown", "Change type unknown");

    private final String displayName;
    private final String description;

    ChangeType(String displayName, String description) {
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
