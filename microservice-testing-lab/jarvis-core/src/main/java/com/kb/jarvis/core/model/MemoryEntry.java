package com.kb.jarvis.core.model;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

public class MemoryEntry {
    private String key;
    private Object value;
    private LocalDateTime timestamp;
    private long ttl; // Time to live in seconds
    private MemoryType type;
    private Map<String, Object> data;

    // Default constructor
    public MemoryEntry() {}

    // Constructor with all fields
    public MemoryEntry(String key, Object value, LocalDateTime timestamp, long ttl, MemoryType type, Map<String, Object> data) {
        this.key = key;
        this.value = value;
        this.timestamp = timestamp;
        this.ttl = ttl;
        this.type = type;
        this.data = data;
    }

    // Builder pattern
    public static MemoryEntryBuilder builder() {
        return new MemoryEntryBuilder();
    }

    // Getter methods
    public String getKey() { return key; }
    public Object getValue() { return value; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public long getTtl() { return ttl; }
    public MemoryType getType() { return type; }
    public Map<String, Object> getData() { return data; }

    // Setter methods
    public void setKey(String key) { this.key = key; }
    public void setValue(Object value) { this.value = value; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public void setTtl(long ttl) { this.ttl = ttl; }
    public void setType(MemoryType type) { this.type = type; }
    public void setData(Map<String, Object> data) { this.data = data; }

    // equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemoryEntry that = (MemoryEntry) o;
        return ttl == that.ttl &&
                Objects.equals(key, that.key) &&
                Objects.equals(value, that.value) &&
                Objects.equals(timestamp, that.timestamp) &&
                type == that.type &&
                Objects.equals(data, that.data);
    }

    // hashCode method
    @Override
    public int hashCode() {
        return Objects.hash(key, value, timestamp, ttl, type, data);
    }

    // toString method
    @Override
    public String toString() {
        return "MemoryEntry{" +
                "key='" + key + '\'' +
                ", value=" + value +
                ", timestamp=" + timestamp +
                ", ttl=" + ttl +
                ", type=" + type +
                ", data=" + data +
                '}';
    }

    // Builder class
    public static class MemoryEntryBuilder {
        private String key;
        private Object value;
        private LocalDateTime timestamp;
        private long ttl;
        private MemoryType type;
        private Map<String, Object> data;

        public MemoryEntryBuilder key(String key) { this.key = key; return this; }
        public MemoryEntryBuilder value(Object value) { this.value = value; return this; }
        public MemoryEntryBuilder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }
        public MemoryEntryBuilder ttl(long ttl) { this.ttl = ttl; return this; }
        public MemoryEntryBuilder type(MemoryType type) { this.type = type; return this; }
        public MemoryEntryBuilder data(Map<String, Object> data) { this.data = data; return this; }

        public MemoryEntry build() {
            return new MemoryEntry(key, value, timestamp, ttl, type, data);
        }
    }
} 