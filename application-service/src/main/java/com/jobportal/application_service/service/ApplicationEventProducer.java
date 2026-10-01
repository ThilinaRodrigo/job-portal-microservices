package com.jobportal.application_service.service;

import com.jobportal.events.JobPortalEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class ApplicationEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishJobCreated(JobPortalEvent event) {
        CompletableFuture.runAsync(() -> {
            try {
                kafkaTemplate.send("job-applied-topic", event);
            } catch (Exception e) {
                System.err.println("Non-critical Kafka notification error: " + e.getMessage());
            }
        });
    }
}

