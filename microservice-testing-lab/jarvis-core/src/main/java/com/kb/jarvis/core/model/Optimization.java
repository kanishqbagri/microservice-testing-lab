package com.kb.jarvis.core.model;

// TEMPORARILY COMMENTED OUT FOR QUICK AI INTEGRATION TESTING
// TODO: Convert to manual implementation after AI integration testing
/*
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class Optimization {
    private String area;
    private String description;
    private double expectedImprovement;
    private List<String> actions;
    private Priority priority;
}
*/

// Temporary placeholder for compilation
public class Optimization {
    private String area;
    private String description;
    private double expectedImprovement;
    private java.util.List<String> actions;
    private Priority priority;
    
    public Optimization() {}
    
    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getExpectedImprovement() { return expectedImprovement; }
    public void setExpectedImprovement(double expectedImprovement) { this.expectedImprovement = expectedImprovement; }
    public java.util.List<String> getActions() { return actions; }
    public void setActions(java.util.List<String> actions) { this.actions = actions; }
    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }
    
    public double getImpact() { return expectedImprovement; } // Alias for expectedImprovement
    
    // Builder pattern
    public static OptimizationBuilder builder() {
        return new OptimizationBuilder();
    }
    
    // Builder class
    public static class OptimizationBuilder {
        private String area;
        private String description;
        private double expectedImprovement;
        private java.util.List<String> actions;
        private Priority priority;
        
        public OptimizationBuilder area(String area) { this.area = area; return this; }
        public OptimizationBuilder description(String description) { this.description = description; return this; }
        public OptimizationBuilder expectedImprovement(double expectedImprovement) { this.expectedImprovement = expectedImprovement; return this; }
        public OptimizationBuilder actions(java.util.List<String> actions) { this.actions = actions; return this; }
        public OptimizationBuilder priority(Priority priority) { this.priority = priority; return this; }
        public OptimizationBuilder name(String name) { this.area = name; return this; } // Alias for area
        public OptimizationBuilder impact(String impact) { return this; } // Placeholder
        public OptimizationBuilder confidence(double confidence) { return this; } // Placeholder
        
        public Optimization build() {
            Optimization opt = new Optimization();
            opt.setArea(area);
            opt.setDescription(description);
            opt.setExpectedImprovement(expectedImprovement);
            opt.setActions(actions);
            opt.setPriority(priority);
            return opt;
        }
    }
} 