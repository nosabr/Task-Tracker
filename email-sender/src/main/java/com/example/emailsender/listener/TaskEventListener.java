package com.example.emailsender.listener;

import com.example.emailsender.dto.DailyReportEvent;
import com.example.emailsender.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TaskEventListener {

    private final EmailService emailService;

    @KafkaListener(
            topics = "task-events",
            groupId = "email-sender-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleDailyReport(DailyReportEvent event) {
        log.info("Received DailyReportEvent for email={}, tasks={}", event.email(), event.tasks().size());
        try {
            emailService.sendDailyReport(event.email(), event.username(), event.tasks());
        } catch (Exception e) {
            log.error("Failed to process DailyReportEvent for {}: {}", event.email(), e.getMessage());
        }
    }
}
