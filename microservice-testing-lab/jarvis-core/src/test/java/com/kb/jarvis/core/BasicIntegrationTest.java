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
@DisplayName("Basic Integration Tests")
public class BasicIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Application Context Loads Successfully")
    public void testApplicationContextLoads() {
        assertTrue(true, "Application context should load successfully");
    }

    @Test
    @DisplayName("Health Check Endpoint Works")
    public void testHealthCheckEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/actuator/health"))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("NLP Parse Intent Endpoint Responds")
    public void testNLPParseIntentEndpoint() throws Exception {
        String requestBody = "{\"input\": \"Run tests for user-service\"}";
        
        mockMvc.perform(MockMvcRequestBuilders.post("/api/nlp/parse-intent")
               .contentType(MediaType.APPLICATION_JSON)
               .content(requestBody))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Jarvis Command Endpoint Responds")
    public void testJarvisCommandEndpoint() throws Exception {
        String requestBody = "{\"input\": \"Check system health\"}";
        
        mockMvc.perform(MockMvcRequestBuilders.post("/api/jarvis/command")
               .contentType(MediaType.APPLICATION_JSON)
               .content(requestBody))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Comprehensive NLP Analysis Endpoint Responds")
    public void testComprehensiveNLPAnalysisEndpoint() throws Exception {
        String requestBody = "{\"input\": \"Run performance tests on order-service\"}";
        
        mockMvc.perform(MockMvcRequestBuilders.post("/api/nlp/comprehensive-analysis")
               .contentType(MediaType.APPLICATION_JSON)
               .content(requestBody))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Sentiment Analysis Endpoint Responds")
    public void testSentimentAnalysisEndpoint() throws Exception {
        String requestBody = "{\"input\": \"This is a great testing framework\"}";
        
        mockMvc.perform(MockMvcRequestBuilders.post("/api/nlp/analyze-sentiment")
               .contentType(MediaType.APPLICATION_JSON)
               .content(requestBody))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Complexity Analysis Endpoint Responds")
    public void testComplexityAnalysisEndpoint() throws Exception {
        String requestBody = "{\"input\": \"Run comprehensive integration tests for all services with performance monitoring\"}";
        
        mockMvc.perform(MockMvcRequestBuilders.post("/api/nlp/analyze-complexity")
               .contentType(MediaType.APPLICATION_JSON)
               .content(requestBody))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
