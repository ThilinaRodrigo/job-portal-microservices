package com.jobportal.job_service.service;

import com.jobportal.events.JobCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishJobCreated(JobCreatedEvent event) {
        kafkaTemplate.send("job-created-topic", event);
    }
}
