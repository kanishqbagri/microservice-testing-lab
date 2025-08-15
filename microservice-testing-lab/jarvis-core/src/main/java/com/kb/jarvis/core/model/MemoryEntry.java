package com.kb.jarvis.core.model;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class MemoryEntry {
    private String key;
    private Object value;
    private LocalDateTime timestamp;
    private long ttl; // Time to live in seconds
    private MemoryType type;
} 