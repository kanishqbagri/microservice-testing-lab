package com.kb.notification_service.controller;

import com.kb.notification_service.dto.NotificationRequest;
import com.kb.notification_service.dto.NotificationResponse;
import com.kb.notification_service.entity.NotificationStatus;
import com.kb.notification_service.entity.NotificationType;
import com.kb.notification_service.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<NotificationResponse> createNotification(@Valid @RequestBody NotificationRequest notificationRequest) {
        NotificationResponse response = notificationService.createNotification(notificationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/send")
    public ResponseEntity<NotificationResponse> sendNotification(@Valid @RequestBody NotificationRequest notificationRequest) {
        notificationService.sendNotification(notificationRequest);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> getNotificationById(@PathVariable Long id) {
        NotificationResponse response = notificationService.getNotificationById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponse>> getNotificationsByUserId(@PathVariable Long userId) {
        List<NotificationResponse> responses = notificationService.getNotificationsByUserId(userId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/user/{userId}/paginated")
    public ResponseEntity<Page<NotificationResponse>> getNotificationsByUserIdPaginated(
            @PathVariable Long userId, Pageable pageable) {
        Page<NotificationResponse> responses = notificationService.getNotificationsByUserIdPaginated(userId, pageable);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<List<NotificationResponse>> getNotificationsByUserIdAndStatus(
            @PathVariable Long userId, @PathVariable NotificationStatus status) {
        List<NotificationResponse> responses = notificationService.getNotificationsByUserIdAndStatus(userId, status);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<NotificationResponse>> getNotificationsByStatus(@PathVariable NotificationStatus status) {
        List<NotificationResponse> responses = notificationService.getNotificationsByStatus(status);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<NotificationResponse>> getNotificationsByType(@PathVariable NotificationType type) {
        List<NotificationResponse> responses = notificationService.getNotificationsByType(type);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<List<NotificationResponse>> getNotificationsByEmail(@PathVariable String email) {
        List<NotificationResponse> responses = notificationService.getNotificationsByEmail(email);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<NotificationResponse>> getNotificationsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<NotificationResponse> responses = notificationService.getNotificationsByDateRange(startDate, endDate);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<NotificationResponse> updateNotificationStatus(
            @PathVariable Long id, @RequestParam NotificationStatus status) {
        NotificationResponse response = notificationService.updateNotificationStatus(id, status);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationResponse> markAsRead(@PathVariable Long id) {
        NotificationResponse response = notificationService.markAsRead(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/sent")
    public ResponseEntity<NotificationResponse> markAsSent(@PathVariable Long id) {
        NotificationResponse response = notificationService.markAsSent(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}/unread-count")
    public ResponseEntity<Long> getUnreadCountByUserId(@PathVariable Long userId) {
        long count = notificationService.getUnreadCountByUserId(userId);
        return ResponseEntity.ok(count);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }
} 