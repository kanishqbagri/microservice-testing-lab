package com.kb.jarvis.core.controller;

import com.kb.jarvis.core.context.ComprehensiveContextService;
import com.kb.jarvis.core.execution.ActionExecutionEngine;
import com.kb.jarvis.core.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Action Execution Controller
 * Handles execution of actions based on comprehensive context analysis
 */
@RestController
@RequestMapping("/api/jarvis/execute")
@CrossOrigin(origins = "*")
public class ActionExecutionController {

    private static final Logger logger = LoggerFactory.getLogger(ActionExecutionController.class);

    @Autowired
    private ComprehensiveContextService comprehensiveContextService;

    @Autowired
    private ActionExecutionEngine actionExecutionEngine;

    /**
     * Execute command with comprehensive context analysis and action execution
     */
    @PostMapping("/command")
    public ResponseEntity<Map<String, Object>> executeCommand(@RequestBody Map<String, String> request) {
        String command = request.get("command");
        if (command == null || command.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Command is required"));
        }

        logger.info("Executing command: {}", command);

        try {
            // Step 1: Analyze command with comprehensive context
            ComprehensiveContext context = comprehensiveContextService.analyzeCommand(command);
            
            // Step 2: Execute actions based on the context
            CompletableFuture<ExecutionResult> executionFuture = actionExecutionEngine.executeActions(context);
            
            // Step 3: Return immediate response with context analysis
            Map<String, Object> response = Map.of(
                "command", command,
                "contextAnalysis", context,
                "executionStatus", "IN_PROGRESS",
                "message", "Command analysis completed. Execution started.",
                "timestamp", java.time.LocalDateTime.now()
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error executing command: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of(
                "error", "Command execution failed: " + e.getMessage(),
                "command", command
            ));
        }
    }

    /**
     * Execute command and wait for completion
     */
    @PostMapping("/command/sync")
    public ResponseEntity<Map<String, Object>> executeCommandSync(@RequestBody Map<String, String> request) {
        String command = request.get("command");
        if (command == null || command.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Command is required"));
        }

        logger.info("Executing command synchronously: {}", command);

        try {
            // Step 1: Analyze command with comprehensive context
            ComprehensiveContext context = comprehensiveContextService.analyzeCommand(command);
            
            // Step 2: Execute actions and wait for completion
            ExecutionResult executionResult = actionExecutionEngine.executeActions(context).join();
            
            // Step 3: Return complete response
            Map<String, Object> response = Map.of(
                "command", command,
                "contextAnalysis", context,
                "executionResult", executionResult,
                "executionStatus", executionResult.isSuccess() ? "COMPLETED" : "FAILED",
                "message", executionResult.getMessage(),
                "timestamp", java.time.LocalDateTime.now()
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error executing command synchronously: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of(
                "error", "Command execution failed: " + e.getMessage(),
                "command", command
            ));
        }
    }

    /**
     * Execute a specific action
     */
    @PostMapping("/action")
    public ResponseEntity<Map<String, Object>> executeAction(@RequestBody Map<String, Object> request) {
        try {
            String actionTypeStr = (String) request.get("actionType");
            String serviceName = (String) request.get("serviceName");
            String testTypeStr = (String) request.get("testType");
            @SuppressWarnings("unchecked")
            Map<String, Object> parameters = (Map<String, Object>) request.getOrDefault("parameters", Map.of());

            if (actionTypeStr == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "actionType is required"));
            }

            ActionType actionType = ActionType.valueOf(actionTypeStr);
            TestType testType = testTypeStr != null ? TestType.valueOf(testTypeStr) : null;

            logger.info("Executing action: {} for service: {} with test type: {}", actionType, serviceName, testType);

            CompletableFuture<ExecutionResult> executionFuture = actionExecutionEngine.executeAction(
                actionType, serviceName, testType, parameters);

            ExecutionResult result = executionFuture.join();

            Map<String, Object> response = Map.of(
                "actionType", actionType,
                "serviceName", serviceName,
                "testType", testType,
                "executionResult", result,
                "executionStatus", result.isSuccess() ? "COMPLETED" : "FAILED",
                "message", result.getMessage(),
                "timestamp", java.time.LocalDateTime.now()
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error executing action: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of(
                "error", "Action execution failed: " + e.getMessage()
            ));
        }
    }

    /**
     * Get execution status
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getExecutionStatus() {
        try {
            Map<String, ExecutionResult> activeExecutions = actionExecutionEngine.getActiveExecutions();
            
            Map<String, Object> response = Map.of(
                "activeExecutions", activeExecutions.size(),
                "executions", activeExecutions,
                "timestamp", java.time.LocalDateTime.now()
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error getting execution status: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of(
                "error", "Failed to get execution status: " + e.getMessage()
            ));
        }
    }

    /**
     * Cancel execution
     */
    @PostMapping("/cancel/{executionId}")
    public ResponseEntity<Map<String, Object>> cancelExecution(@PathVariable String executionId) {
        try {
            boolean cancelled = actionExecutionEngine.cancelExecution(executionId);
            
            Map<String, Object> response = Map.of(
                "executionId", executionId,
                "cancelled", cancelled,
                "message", cancelled ? "Execution cancelled successfully" : "Execution not found or already completed",
                "timestamp", java.time.LocalDateTime.now()
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error cancelling execution: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of(
                "error", "Failed to cancel execution: " + e.getMessage()
            ));
        }
    }

    /**
     * Get available actions
     */
    @GetMapping("/actions")
    public ResponseEntity<Map<String, Object>> getAvailableActions() {
        try {
            Map<String, Object> response = Map.of(
                "actionTypes", ActionType.values(),
                "testTypes", TestType.values(),
                "services", new String[]{"user-service", "product-service", "order-service", "notification-service", "gateway-service"},
                "timestamp", java.time.LocalDateTime.now()
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error getting available actions: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of(
                "error", "Failed to get available actions: " + e.getMessage()
            ));
        }
    }
}
