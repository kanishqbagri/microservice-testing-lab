package com.kb.jarvis.core.model;

public enum PluginStatus {
    REGISTERED("Registered", "Plugin is registered but not loaded"),
    LOADED("Loaded", "Plugin is loaded and ready"),
    ACTIVE("Active", "Plugin is active and running"),
    DISABLED("Disabled", "Plugin is disabled"),
    ERROR("Error", "Plugin encountered an error"),
    UPDATING("Updating", "Plugin is being updated"),
    DEPRECATED("Deprecated", "Plugin is deprecated"),
    INCOMPATIBLE("Incompatible", "Plugin is incompatible with current version");

    private final String displayName;
    private final String description;

    PluginStatus(String displayName, String description) {
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
