package com.kb.jarvis.core.model;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

public class SystemEvent {
    private String eventType;
    private String description;
    private Map<String, Object> data;
    private LocalDateTime timestamp;

    // Default constructor
    public SystemEvent() {}

    // Constructor with all fields
    public SystemEvent(String eventType, String description, Map<String, Object> data, LocalDateTime timestamp) {
        this.eventType = eventType;
        this.description = description;
        this.data = data;
        this.timestamp = timestamp;
    }

    // Builder pattern
    public static SystemEventBuilder builder() {
        return new SystemEventBuilder();
    }

    // Getter methods
    public String getEventType() { return eventType; }
    public String getDescription() { return description; }
    public Map<String, Object> getData() { return data; }
    public LocalDateTime getTimestamp() { return timestamp; }

    // Setter methods
    public void setEventType(String eventType) { this.eventType = eventType; }
    public void setDescription(String description) { this.description = description; }
    public void setData(Map<String, Object> data) { this.data = data; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    // equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SystemEvent that = (SystemEvent) o;
        return Objects.equals(eventType, that.eventType) &&
                Objects.equals(description, that.description) &&
                Objects.equals(data, that.data) &&
                Objects.equals(timestamp, that.timestamp);
    }

    // hashCode method
    @Override
    public int hashCode() {
        return Objects.hash(eventType, description, data, timestamp);
    }

    // toString method
    @Override
    public String toString() {
        return "SystemEvent{" +
                "eventType='" + eventType + '\'' +
                ", description='" + description + '\'' +
                ", data=" + data +
                ", timestamp=" + timestamp +
                '}';
    }

    // Builder class
    public static class SystemEventBuilder {
        private String eventType;
        private String description;
        private Map<String, Object> data;
        private LocalDateTime timestamp;

        public SystemEventBuilder eventType(String eventType) { this.eventType = eventType; return this; }
        public SystemEventBuilder type(String eventType) { this.eventType = eventType; return this; }
        public SystemEventBuilder description(String description) { this.description = description; return this; }
        public SystemEventBuilder data(Map<String, Object> data) { this.data = data; return this; }
        public SystemEventBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }

        public SystemEvent build() {
            return new SystemEvent(eventType, description, data, timestamp);
        }
    }
} 