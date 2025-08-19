package com.kb.jarvis.core;

import com.kb.jarvis.core.model.RiskAssessment;
import com.kb.jarvis.core.model.RiskLevel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Lombok Annotation Processing Test")
public class LombokTest {

    @Test
    @DisplayName("Test Lombok @Data and @Builder annotations")
    public void testLombokAnnotations() {
        // Test if Lombok generated methods exist
        try {
            // Test builder pattern
            RiskAssessment riskAssessment = RiskAssessment.builder()
                .level(RiskLevel.HIGH)
                .riskFactors(java.util.Arrays.asList("High load", "Network latency"))
                .mitigationStrategy("Implement caching and load balancing")
                .build();
            
            // Test getter methods
            assertEquals(RiskLevel.HIGH, riskAssessment.getLevel());
            assertEquals(2, riskAssessment.getRiskFactors().size());
            assertEquals("Implement caching and load balancing", riskAssessment.getMitigationStrategy());
            
            // Test setter methods
            riskAssessment.setLevel(RiskLevel.MEDIUM);
            assertEquals(RiskLevel.MEDIUM, riskAssessment.getLevel());
            
            System.out.println("✅ Lombok annotations working correctly!");
            System.out.println("  - Builder pattern: ✅ Working");
            System.out.println("  - Getter methods: ✅ Working");
            System.out.println("  - Setter methods: ✅ Working");
            
        } catch (Exception e) {
            System.err.println("❌ Lombok annotations not working: " + e.getMessage());
            e.printStackTrace();
            fail("Lombok annotation processing failed: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Test Lombok @Data annotation methods")
    public void testLombokDataMethods() {
        try {
            RiskAssessment risk1 = RiskAssessment.builder()
                .level(RiskLevel.HIGH)
                .riskFactors(java.util.Arrays.asList("Factor1"))
                .mitigationStrategy("Strategy1")
                .build();
                
            RiskAssessment risk2 = RiskAssessment.builder()
                .level(RiskLevel.HIGH)
                .riskFactors(java.util.Arrays.asList("Factor1"))
                .mitigationStrategy("Strategy1")
                .build();
                
            RiskAssessment risk3 = RiskAssessment.builder()
                .level(RiskLevel.LOW)
                .riskFactors(java.util.Arrays.asList("Factor2"))
                .mitigationStrategy("Strategy2")
                .build();
            
            // Test equals method
            assertEquals(risk1, risk2);
            assertNotEquals(risk1, risk3);
            
            // Test hashCode method
            assertEquals(risk1.hashCode(), risk2.hashCode());
            assertNotEquals(risk1.hashCode(), risk3.hashCode());
            
            // Test toString method
            String toString = risk1.toString();
            assertTrue(toString.contains("HIGH"));
            assertTrue(toString.contains("Factor1"));
            
            System.out.println("✅ Lombok @Data methods working correctly!");
            System.out.println("  - equals(): ✅ Working");
            System.out.println("  - hashCode(): ✅ Working");
            System.out.println("  - toString(): ✅ Working");
            
        } catch (Exception e) {
            System.err.println("❌ Lombok @Data methods not working: " + e.getMessage());
            e.printStackTrace();
            fail("Lombok @Data methods failed: " + e.getMessage());
        }
    }
}
