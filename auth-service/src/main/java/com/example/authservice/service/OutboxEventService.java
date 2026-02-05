package com.example.authservice.service;

import com.example.authservice.entity.EventType;
import com.example.authservice.entity.OutboxEvent;
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

    public void saveEvent(EventType type, Long aggregateId, Object event) {
        try {
            OutboxEvent outboxEvent = OutboxEvent.builder()
                    .eventType(type)
                    .aggregateId(aggregateId)
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
