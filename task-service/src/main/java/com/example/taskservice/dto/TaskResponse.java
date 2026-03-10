package com.example.taskservice.dto;

import java.time.LocalDateTime;

public record TaskResponse(
        String id,
        String title,
        String description,
        String  priority,
        String status,
        Long userId,
        String email,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
