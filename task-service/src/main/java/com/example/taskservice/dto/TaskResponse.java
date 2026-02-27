package com.example.taskservice.dto;

import java.time.LocalDateTime;

public record TaskResponse(
        String id,
        String title,
        String description,
        String status,
        Long userId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
