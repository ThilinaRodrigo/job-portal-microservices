package com.jobportal.notification_service;

import com.jobportal.events.JobApplicationStatusChangedEvent;
import com.jobportal.events.JobAppliedEvent;
import com.jobportal.events.JobCreatedEvent;
import com.jobportal.events.JobPortalEvent;
import com.jobportal.notification_service.entity.Notification;
import com.jobportal.notification_service.service.INotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationListener {

    private final INotificationService notificationService;

    @KafkaListener(
            topics = { "job-created-topic", "job-applied-topic", "job-status-topic" },
            groupId = "notification-group"
    )
    public void consume(JobPortalEvent event) {
        log.info("Received Kafka Event: {}", event.getClass().getSimpleName());

        if (event instanceof JobCreatedEvent created) {
            handleJobCreated(created);
        } else if (event instanceof JobAppliedEvent applied) {
            handleJobApplied(applied);
        } else if (event instanceof JobApplicationStatusChangedEvent statusChanged) {
            handleStatusChanged(statusChanged);
        } else {
            log.warn("Unknown event type: {}", event.getClass());
        }
    }

    private void handleJobCreated(JobCreatedEvent event) {
        log.info("Job created event received: Job ID {} - {}", event.getJobId(), event.getTitle());

        if (event.getEmployerEmail() != null) {
            Notification employerNotification = Notification.builder()
                    .recipientEmail(event.getEmployerEmail())
                    .recipientRole("EMPLOYER")
                    .title("Job Position Published")
                    .message("Your job posting \"" + event.getTitle() + "\" is now live and accepting candidate applications.")
                    .type("JOB_CREATED")
                    .isRead(false)
                    .createdAt(LocalDateTime.now())
                    .relatedEntityId(event.getJobId())
                    .build();

            notificationService.saveNotification(employerNotification);
        }
    }

    private void handleJobApplied(JobAppliedEvent event) {
        log.info("Job applied event received: Candidate {} for Job ID {}", event.getEmail(), event.getJobId());

        String applicantName = (event.getFirstName() + " " + (event.getLastName() != null ? event.getLastName() : "")).trim();
        if (applicantName.isEmpty()) applicantName = "A candidate";

        // 1. Notify Employer
        if (event.getEmployerEmail() != null && !event.getEmployerEmail().isEmpty()) {
            Notification employerNotif = Notification.builder()
                    .recipientEmail(event.getEmployerEmail())
                    .recipientRole("EMPLOYER")
                    .title("New Application Received")
                    .message(applicantName + " applied for your position \"" + event.getTitle() + "\".")
                    .type("JOB_APPLICATION")
                    .isRead(false)
                    .createdAt(LocalDateTime.now())
                    .relatedEntityId(event.getJobId())
                    .build();

            notificationService.saveNotification(employerNotif);
        }

        // 2. Notify Candidate
        if (event.getEmail() != null && !event.getEmail().isEmpty()) {
            Notification candidateNotif = Notification.builder()
                    .recipientEmail(event.getEmail())
                    .recipientRole("JOBSEEKER")
                    .title("Application Submitted Successfully")
                    .message("Your application for \"" + event.getTitle() + "\" at " + (event.getEmployerName() != null ? event.getEmployerName() : "the employer") + " has been submitted.")
                    .type("JOB_APPLICATION")
                    .isRead(false)
                    .createdAt(LocalDateTime.now())
                    .relatedEntityId(event.getJobId())
                    .build();

            notificationService.saveNotification(candidateNotif);
        }
    }

    private void handleStatusChanged(JobApplicationStatusChangedEvent event) {
        log.info("Application status change event received: Application ID {} -> {}", event.getApplicationId(), event.getNewStatus());

        if (event.getApplicantEmail() != null && !event.getApplicantEmail().isEmpty()) {
            String statusFormatted = event.getNewStatus() != null ? event.getNewStatus().replace("_", " ") : "UPDATED";
            Notification candidateNotif = Notification.builder()
                    .recipientEmail(event.getApplicantEmail())
                    .recipientRole("JOBSEEKER")
                    .title("Application Status: " + statusFormatted)
                    .message("Your application status for \"" + event.getJobTitle() + "\" has been updated to " + statusFormatted + ".")
                    .type("APPLICATION_STATUS")
                    .isRead(false)
                    .createdAt(LocalDateTime.now())
                    .relatedEntityId(event.getApplicationId())
                    .build();

            notificationService.saveNotification(candidateNotif);
        }
    }
}
