package com.example.taskservice.dto;

import com.example.taskservice.document.TaskPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateTaskRequest(
        @NotBlank @Size(max = 25) String title,
        @Size(max = 255) String description,
        TaskPriority priority
) {
}
