package com.kb.jarvis.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureWebMvc
@DisplayName("Simple Integration Tests")
public class SimpleIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Application Context Loads")
    public void testApplicationContextLoads() {
        // This test verifies that the Spring application context loads successfully
        assertTrue(true, "Application context should load successfully");
    }

    @Test
    @DisplayName("Health Check Endpoint")
    public void testHealthCheckEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/actuator/health"))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Basic NLP Parsing Endpoint")
    public void testNLPParseIntentEndpoint() throws Exception {
        String requestBody = "{\"input\": \"Run tests for user-service\"}";
        
        mockMvc.perform(MockMvcRequestBuilders.post("/api/nlp/parse-intent")
               .contentType(MediaType.APPLICATION_JSON)
               .content(requestBody))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Jarvis Command Endpoint")
    public void testJarvisCommandEndpoint() throws Exception {
        String requestBody = "{\"input\": \"Check system health\"}";
        
        mockMvc.perform(MockMvcRequestBuilders.post("/api/jarvis/command")
               .contentType(MediaType.APPLICATION_JSON)
               .content(requestBody))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Comprehensive NLP Analysis Endpoint")
    public void testComprehensiveNLPAnalysisEndpoint() throws Exception {
        String requestBody = "{\"input\": \"Run performance tests on order-service\"}";
        
        mockMvc.perform(MockMvcRequestBuilders.post("/api/nlp/comprehensive-analysis")
               .contentType(MediaType.APPLICATION_JSON)
               .content(requestBody))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
