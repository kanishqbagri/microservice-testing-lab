package com.kb.jarvis.core.ai;

import com.kb.jarvis.core.model.UserIntent;
import com.kb.jarvis.core.model.AIAnalysis;
import org.springframework.stereotype.Component;

@Component
public class AIEngine {
    public AIAnalysis analyzeIntent(UserIntent intent) {
        // Stub: In a real implementation, AI would analyze the intent
        return AIAnalysis.builder()
                .intent(intent)
                .confidence(0.5)
                .build();
    }
} 