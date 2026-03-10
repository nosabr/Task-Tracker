package com.example.taskservice.mapper;

import com.example.taskservice.document.Task;
import com.example.taskservice.document.TaskPriority;
import com.example.taskservice.document.TaskStatus;
import com.example.taskservice.dto.CreateTaskRequest;
import com.example.taskservice.dto.TaskResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class TaskMapperTest {

    private TaskMapper taskMapper;

    @BeforeEach
    void setUp() {
        taskMapper = new TaskMapper();
    }

    // -------------------------------------------------------------------------
    // toTaskResponse
    // -------------------------------------------------------------------------

    @Test
    void toTaskResponse_mapsAllFieldsCorrectly() {
        LocalDateTime now = LocalDateTime.now();
        Task task = Task.builder()
                .id("abc123")
                .title("Fix bug")
                .description("Critical issue")
                .status(TaskStatus.IN_PROGRESS)
                .priority(TaskPriority.HIGH)
                .userId(42L)
                .email("user@example.com")
                .createdDate(now)
                .updatedDate(now)
                .build();

        TaskResponse response = taskMapper.toTaskResponse(task);

        assertThat(response.id()).isEqualTo("abc123");
        assertThat(response.title()).isEqualTo("Fix bug");
        assertThat(response.description()).isEqualTo("Critical issue");
        assertThat(response.status()).isEqualTo("IN_PROGRESS");
        assertThat(response.priority()).isEqualTo("HIGH");
        assertThat(response.userId()).isEqualTo(42L);
        assertThat(response.email()).isEqualTo("user@example.com");
    }

    // -------------------------------------------------------------------------
    // toDocument
    // -------------------------------------------------------------------------

    @Test
    void toDocument_mapsRequestFieldsCorrectly() {
        CreateTaskRequest request = new CreateTaskRequest("New task", "Some description", TaskPriority.LOW);

        Task task = taskMapper.toDocument(request, 7L, "owner@example.com");

        assertThat(task.getTitle()).isEqualTo("New task");
        assertThat(task.getDescription()).isEqualTo("Some description");
        assertThat(task.getPriority()).isEqualTo(TaskPriority.LOW);
        assertThat(task.getUserId()).isEqualTo(7L);
        assertThat(task.getEmail()).isEqualTo("owner@example.com");
    }

    @Test
    void toDocument_setsDefaultStatusToInProgress() {
        CreateTaskRequest request = new CreateTaskRequest("Task", null, TaskPriority.MEDIUM);

        Task task = taskMapper.toDocument(request, 1L, "user@example.com");

        assertThat(task.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
    }

    @Test
    void toDocument_doesNotSetId() {
        CreateTaskRequest request = new CreateTaskRequest("Task", null, TaskPriority.MEDIUM);

        Task task = taskMapper.toDocument(request, 1L, "user@example.com");

        // ID должен присваиваться MongoDB, не маппером
        assertThat(task.getId()).isNull();
    }
}