package com.kb.jarvis.core.model;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class SystemContext {
    private Map<String, Object> variables;
    private List<SystemEvent> recentEvents;
    private Map<String, Object> environment;
    private LocalDateTime lastUpdated;
} 