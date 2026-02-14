package com.jobportal.notification_service;

import com.jobportal.events.JobAppliedEvent;
import com.jobportal.events.JobCreatedEvent;
import com.jobportal.events.JobPortalEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    @KafkaListener(
            topics = { "job-created-topic", "job-applied-topic" },
            groupId = "notification-group"
    )
    public void consume(JobPortalEvent event) {

        if (event instanceof JobCreatedEvent created) {
            handleJobCreated(created);
        } else if (event instanceof JobAppliedEvent applied) {
            handleJobApplied(applied);
        } else {
            System.out.println("Unknown event type: " + event.getClass());
        }
    }

    private void handleJobCreated(JobCreatedEvent event) {
        System.out.println("Job created: " + event.getJobId());
    }

    private void handleJobApplied(JobAppliedEvent event) {
        System.out.println("Job applied by: " + event.getFirstName() + " " + event.getLastName() + " for job ID: " + event.getJobId()+"with job title: " + event.getTitle() + " with email: " + event.getEmail());
    }
}
