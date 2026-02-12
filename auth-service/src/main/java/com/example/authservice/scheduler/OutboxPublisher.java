package com.example.authservice.scheduler;

import com.example.authservice.entity.OutboxEvent;
import com.example.authservice.event.UserDeletedEvent;
import com.example.authservice.event.UserRegisterEvent;
import com.example.authservice.repository.OutboxEventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class OutboxPublisher {
    private final OutboxEventRepository outboxEventRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedRate = 5000)
    @Transactional
    public void publishEvents(){
        List<OutboxEvent> outboxEvents = outboxEventRepository.findBySentFalseOrderByCreatedAtAsc();
        if(outboxEvents.isEmpty()){
            return;
        }
        log.info("Publishing events to Kafka :{}", outboxEvents.size());

        for(OutboxEvent event : outboxEvents){
            try{
                Object payload = deserializeEvent(event);
                kafkaTemplate.send("user-events", payload).get(5, TimeUnit.SECONDS);
                event.setSent(true);
                event.setSentAt(LocalDateTime.now());
                outboxEventRepository.save(event);
            } catch(Exception e){
                log.error("Error while sending event to Kafka :{}",e.getMessage());
            }
        }


    }

    private Object deserializeEvent(OutboxEvent event) throws JsonProcessingException {
        return switch (event.getEventType()) {
            case USER_REGISTERED ->
                    objectMapper.readValue(event.getPayload(), UserRegisterEvent.class);
            case USER_DELETED ->
                    objectMapper.readValue(event.getPayload(), UserDeletedEvent.class);
            default ->
                    throw new IllegalArgumentException("Unknown event type: " + event.getEventType());
        };
    }
}
