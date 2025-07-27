package com.kb.jarvis.core.learning;

import com.kb.jarvis.core.model.UserIntent;
import com.kb.jarvis.core.model.AIAnalysis;
import com.kb.jarvis.core.model.DecisionAction;
import com.kb.jarvis.core.model.LearningInsights;
import org.springframework.stereotype.Component;

@Component
public class LearningEngine {
    public void learnFromInteraction(UserIntent intent, AIAnalysis analysis, DecisionAction action) {
        // Stub: No-op
    }
    public LearningInsights getInsights() {
        return null;
    }
} 