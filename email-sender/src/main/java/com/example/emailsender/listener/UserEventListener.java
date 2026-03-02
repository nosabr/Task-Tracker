package com.example.emailsender.listener;


import com.example.emailsender.dto.UserRegisteredEvent;
import com.example.emailsender.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserEventListener {

    private final EmailService emailService;

    @KafkaListener(
            topics = "user-events",
            groupId = "email-sender-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleUserRegistered(UserRegisteredEvent event) {
        log.info("Received UserRegisteredEvent for userId={}, email={}", event.userId(), event.email());
        try {
            emailService.sendWelcomeEmail(event.email());
        } catch (Exception e) {
            log.error("Failed to process UserRegisteredEvent for {}: {}", event.email(), e.getMessage());
        }
    }
}
