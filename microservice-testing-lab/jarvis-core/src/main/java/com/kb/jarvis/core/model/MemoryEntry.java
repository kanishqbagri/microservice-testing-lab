package com.kb.jarvis.core.model;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class MemoryEntry {
    private String key;
    private Object value;
    private LocalDateTime timestamp;
    private long ttl; // Time to live in seconds
    private MemoryType type;
    private Map<String, Object> data;
} 