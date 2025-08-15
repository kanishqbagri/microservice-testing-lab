package com.kb.jarvis.core.decision;

import com.kb.jarvis.core.model.UserIntent;
import com.kb.jarvis.core.model.AIAnalysis;
import com.kb.jarvis.core.model.DecisionAction;
import org.springframework.stereotype.Component;

@Component
public class DecisionEngine {
    public DecisionAction decideAction(UserIntent intent, AIAnalysis analysis) {
        // Stub: Return a default action
        return DecisionAction.builder()
                .description("Default action")
                .build();
    }
} 