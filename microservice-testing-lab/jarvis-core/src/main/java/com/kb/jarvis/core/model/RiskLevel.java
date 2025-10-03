package com.kb.jarvis.core.model;

public enum RiskLevel {
    LOW, MEDIUM, HIGH, CRITICAL;
    
    public String getDisplayName() {
        return this.name().charAt(0) + this.name().substring(1).toLowerCase();
    }
} 