package com.jobportal.notification_service.controller;

import com.jobportal.notification_service.entity.Notification;
import com.jobportal.notification_service.service.INotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final INotificationService notificationService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getNotificationsByUserId(
            @PathVariable Long userId,
            @RequestParam(required = false) String email) {
        return ResponseEntity.ok(notificationService.getNotificationsForUser(userId, email));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<List<Notification>> getNotificationsByEmail(@PathVariable String email) {
        return ResponseEntity.ok(notificationService.getNotificationsForUser(null, email));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Notification> markAsRead(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.markAsRead(id));
    }

    @PutMapping("/user/{userId}/read-all")
    public ResponseEntity<Void> markAllAsRead(
            @PathVariable Long userId,
            @RequestParam(required = false) String email) {
        notificationService.markAllAsRead(userId, email);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(
            @PathVariable Long userId,
            @RequestParam(required = false) String email) {
        long count = notificationService.getUnreadCount(userId, email);
        return ResponseEntity.ok(Map.of("unreadCount", count));
    }
}
