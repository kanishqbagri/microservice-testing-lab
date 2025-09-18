package com.kb.jarvis.core.test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import java.util.Optional;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.io.OutputStream;

/**
 * Base test class for API tests. Handles logging and reporting to Supabase.
 */
@ExtendWith(BaseApiTest.SupabaseTestReporter.class)
public abstract class BaseApiTest {
    // You can override these in subclasses if needed
    protected String supabaseUrl = "https://smuaribfocdanafiixzi.supabase.co/rest/v1/test_result";
    protected String supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InNtdWFyaWJmb2NkYW5hZmlpeHppIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTgyMDcxMzUsImV4cCI6MjA3Mzc4MzEzNX0.l071CVCjnuKGmhZiNSpkGqbOh17ls6atb3aDSnC1vzs";

    public static class SupabaseTestReporter implements TestWatcher {
        @Override
        public void testSuccessful(ExtensionContext context) {
            reportResult(context, "PASS", null);
        }
        @Override
        public void testFailed(ExtensionContext context, Throwable cause) {
            reportResult(context, "FAIL", cause.getMessage());
        }
        private void reportResult(ExtensionContext context, String status, String error) {
            try {
                String testName = context.getDisplayName();
                String className = context.getTestClass().map(Class::getName).orElse("");
                String payload = String.format("{\"test_name\":\"%s\",\"class_name\":\"%s\",\"status\":\"%s\",\"error_message\":%s}",
                        testName, className, status, error == null ? "null" : ("\"" + error.replace("\"", "'") + "\""));
                URL url = new URL("https://smuaribfocdanafiixzi.supabase.co/rest/v1/test_result");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("apikey", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InNtdWFyaWJmb2NkYW5hZmlpeHppIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTgyMDcxMzUsImV4cCI6MjA3Mzc4MzEzNX0.l071CVCjnuKGmhZiNSpkGqbOh17ls6atb3aDSnC1vzs");
                conn.setRequestProperty("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InNtdWFyaWJmb2NkYW5hZmlpeHppIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTgyMDcxMzUsImV4cCI6MjA3Mzc4MzEzNX0.l071CVCjnuKGmhZiNSpkGqbOh17ls6atb3aDSnC1vzs");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
                try (OutputStream os = conn.getOutputStream()) {
                    os.write(payload.getBytes(StandardCharsets.UTF_8));
                }
                int responseCode = conn.getResponseCode();
                // Optionally log responseCode
            } catch (Exception e) {
                // Optionally log error
            }
        }
    }
}
