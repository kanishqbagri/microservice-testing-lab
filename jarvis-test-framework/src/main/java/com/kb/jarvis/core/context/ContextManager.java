package com.kb.jarvis.core.context;

import com.kb.jarvis.core.model.UserIntent;
import com.kb.jarvis.core.model.SystemHealth;
import com.kb.jarvis.core.model.PerformanceMetrics;
import org.springframework.stereotype.Component;

@Component
public class ContextManager {
    public void updateContext(UserIntent intent) {
        // Stub: No-op
    }
    public SystemHealth getSystemHealth() {
        return null;
    }
    public PerformanceMetrics getPerformanceMetrics() {
        return null;
    }
} 