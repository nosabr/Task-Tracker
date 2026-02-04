package com.example.authservice.repository;

import com.example.authservice.entity.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent,Long> {

    List<OutboxEvent> findBySentFalseOrderByCreatedAtAsc();

    @Query("SELECT o FROM OutboxEvent o WHERE o.sent = false AND o.createdAt < :before")
    List<OutboxEvent> findUnsentOlderThan(@Param("before") LocalDateTime before);
}
