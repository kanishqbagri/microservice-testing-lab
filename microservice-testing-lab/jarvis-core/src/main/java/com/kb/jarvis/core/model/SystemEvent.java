package com.kb.jarvis.core.model;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class SystemEvent {
    private String eventType;
    private String description;
    private Map<String, Object> data;
    private LocalDateTime timestamp;
    // EventSeverity removed as per previous user edit
} 