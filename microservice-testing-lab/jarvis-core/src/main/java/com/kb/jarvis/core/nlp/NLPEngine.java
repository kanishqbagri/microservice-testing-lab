package com.kb.jarvis.core.nlp;

import com.kb.jarvis.core.model.UserIntent;
import com.kb.jarvis.core.model.IntentType;
import org.springframework.stereotype.Component;

@Component
public class NLPEngine {
    public UserIntent parseIntent(String userInput) {
        // Stub: In a real implementation, NLP would analyze userInput
        return UserIntent.builder()
                .rawInput(userInput)
                .type(IntentType.UNKNOWN)
                .description("Parsed intent from input")
                .confidence(0.5)
                .build();
    }
} 