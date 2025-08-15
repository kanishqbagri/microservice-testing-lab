package com.kb.gateway_service.dto;

import java.util.List;

public class GatewayStatusResponse {
    private String status;
    private String version;
    private List<String> activeRoutes;
    private long uptime;

    public GatewayStatusResponse() {
    }

    public GatewayStatusResponse(String status, String version, List<String> activeRoutes, long uptime) {
        this.status = status;
        this.version = version;
        this.activeRoutes = activeRoutes;
        this.uptime = uptime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<String> getActiveRoutes() {
        return activeRoutes;
    }

    public void setActiveRoutes(List<String> activeRoutes) {
        this.activeRoutes = activeRoutes;
    }

    public long getUptime() {
        return uptime;
    }

    public void setUptime(long uptime) {
        this.uptime = uptime;
    }
} 