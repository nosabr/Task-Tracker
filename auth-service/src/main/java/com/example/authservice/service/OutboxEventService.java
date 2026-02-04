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
    private final ObjectMapper objectMapper;
    private final OutboxEventRepository outboxEventRepository;


    public void createOutboxEvent(User user, UserRegisterEvent event) {
        try {
            OutboxEvent outboxEvent = OutboxEvent.builder()
                    .eventType(EventType.USER_REGISTERED)
                    .aggregateId(user.getId())
                    .payload(objectMapper.writeValueAsString(event))
                    .sent(false)
                    .build();
            outboxEventRepository.save(outboxEvent);
            log.info("User created and event saved to outbox: {}", user.getEmail());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to create outbox event");
        }
    }
}
