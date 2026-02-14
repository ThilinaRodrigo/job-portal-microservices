package com.jobportal.application_service.service;

import com.jobportal.events.JobPortalEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishJobCreated(JobPortalEvent event) {
        kafkaTemplate.send("job-applied-topic", event);
    }
}

