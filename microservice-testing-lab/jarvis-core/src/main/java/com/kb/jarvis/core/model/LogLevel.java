package com.kb.jarvis.core.model;

public enum LogLevel {
    TRACE("TRACE", "Trace level logging for detailed debugging"),
    DEBUG("DEBUG", "Debug level logging for development"),
    INFO("INFO", "Information level logging for general information"),
    WARN("WARN", "Warning level logging for potential issues"),
    ERROR("ERROR", "Error level logging for errors"),
    FATAL("FATAL", "Fatal level logging for critical errors");

    private final String level;
    private final String description;

    LogLevel(String level, String description) {
        this.level = level;
        this.description = description;
    }

    public String getLevel() {
        return level;
    }

    public String getDescription() {
        return description;
    }
}
