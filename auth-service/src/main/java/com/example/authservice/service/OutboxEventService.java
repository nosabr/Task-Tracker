package com.example.authservice.service;

import com.example.authservice.entity.EventType;
import com.example.authservice.entity.OutboxEvent;
import com.example.authservice.entity.User;
import com.example.authservice.event.UserRegisterEvent;
import com.example.authservice.repository.OutboxEventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OutboxEventService {

    private final OutboxEventRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public void createOutboxEvent(User user, UserRegisterEvent event) {
        try {
            OutboxEvent outboxEvent = OutboxEvent.builder()
                    .eventType(EventType.USER_REGISTERED)
                    .aggregateId(user.getId())
                    .payload(objectMapper.writeValueAsString(event))
                    .sent(false)
                    .build();

            outboxRepository.save(outboxEvent);

        } catch (JsonProcessingException ex) {
            log.error("Failed to serialize event: {}", ex.getMessage());
            throw new RuntimeException("Failed to create outbox event", ex);
        }
    }
}
