package com.kb.jarvis.core.test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import java.util.Optional;

@ExtendWith(BaseApiTest.BaseApiTestWatcher.class)
public abstract class BaseApiTest {
    public static class BaseApiTestWatcher implements TestWatcher {
        @Override
        public void testSuccessful(ExtensionContext context) {
            // ...existing code for reporting to Supabase...
        }
        @Override
        public void testFailed(ExtensionContext context, Throwable cause) {
            // ...existing code for reporting to Supabase...
        }
        @Override
        public void testDisabled(ExtensionContext context, Optional<String> reason) {
            // ...existing code for reporting to Supabase...
        }
        @Override
        public void testAborted(ExtensionContext context, Throwable cause) {
            // ...existing code for reporting to Supabase...
        }
    }
}
