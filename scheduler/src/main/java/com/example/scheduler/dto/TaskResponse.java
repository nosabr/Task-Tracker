package com.example.scheduler.dto;

import java.time.LocalDateTime;

public record TaskResponse(
        String id,
        String title,
        String description,
        String status,
        Long userId,
        String email,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

