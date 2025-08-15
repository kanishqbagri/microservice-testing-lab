package com.kb.jarvis.core.memory;

import com.kb.jarvis.core.model.ActiveTest;
import com.kb.jarvis.core.model.TestFailure;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MemoryManager {
    public List<ActiveTest> getActiveTests() {
        return null;
    }
    public List<TestFailure> getRecentFailures() {
        return null;
    }
} 