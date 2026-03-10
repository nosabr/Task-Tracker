package com.example.taskservice.mapper;

import com.example.taskservice.document.Task;
import com.example.taskservice.dto.CreateTaskRequest;
import com.example.taskservice.dto.TaskResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class TaskMapper {
    public TaskResponse toTaskResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getPriority().toString(),
                task.getStatus().toString(),
                task.getUserId(),
                task.getEmail(),
                task.getCreatedDate(),
                task.getUpdatedDate()
        );
    }

    public Task toDocument(CreateTaskRequest request, Long userId, String email) {
        return Task.builder()
                .title(request.title())
                .description(request.description())
                .priority(request.priority())
                .userId(userId)
                .email(email)
                .build();
    }
}
