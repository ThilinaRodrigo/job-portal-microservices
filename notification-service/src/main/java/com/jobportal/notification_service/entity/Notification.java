package com.jobportal.notification_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long recipientId;
    private String recipientEmail;
    private String recipientRole;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String message;

    private String type; // e.g. JOB_APPLICATION, STATUS_UPDATE, JOB_CREATED

    private boolean isRead;

    private LocalDateTime createdAt;

    private Long relatedEntityId;
}
