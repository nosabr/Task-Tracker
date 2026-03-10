package com.example.taskservice.service;

import com.example.taskservice.document.Task;
import com.example.taskservice.document.TaskPriority;
import com.example.taskservice.document.TaskStatus;
import com.example.taskservice.dto.CreateTaskRequest;
import com.example.taskservice.dto.TaskResponse;
import com.example.taskservice.dto.UpdateTaskRequest;
import com.example.taskservice.exception.TaskNotFoundException;
import com.example.taskservice.mapper.TaskMapper;
import com.example.taskservice.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock TaskRepository taskRepository;
    @Mock TaskMapper taskMapper;

    @InjectMocks
    TaskService taskService;

    // -------------------------------------------------------------------------
    // getAllTasksByUserId
    // -------------------------------------------------------------------------

    @Test
    void getAllTasksByUserId_returnsMappedList() {
        Task task = buildTask("1", 10L);
        TaskResponse response = buildResponse("1", 10L);

        when(taskRepository.findByUserId(10L)).thenReturn(List.of(task));
        when(taskMapper.toTaskResponse(task)).thenReturn(response);

        List<TaskResponse> result = taskService.getAllTasksByUserId(10L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).id()).isEqualTo("1");
    }

    @Test
    void getAllTasksByUserId_returnsEmptyList_whenNoTasks() {
        when(taskRepository.findByUserId(99L)).thenReturn(List.of());

        assertThat(taskService.getAllTasksByUserId(99L)).isEmpty();
    }

    // -------------------------------------------------------------------------
    // getTaskByIdAndUserId
    // -------------------------------------------------------------------------

    @Test
    void getTaskByIdAndUserId_returnsTask_whenFound() {
        Task task = buildTask("abc", 10L);
        TaskResponse response = buildResponse("abc", 10L);

        when(taskRepository.findByIdAndUserId("abc", 10L)).thenReturn(Optional.of(task));
        when(taskMapper.toTaskResponse(task)).thenReturn(response);

        assertThat(taskService.getTaskByIdAndUserId("abc", 10L).id()).isEqualTo("abc");
    }

    @Test
    void getTaskByIdAndUserId_throwsNotFound_whenMissing() {
        when(taskRepository.findByIdAndUserId("xyz", 10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.getTaskByIdAndUserId("xyz", 10L))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining("xyz");
    }

    // -------------------------------------------------------------------------
    // createTask
    // -------------------------------------------------------------------------

    @Test
    void createTask_savesAndReturnsMappedResponse() {
        CreateTaskRequest request = new CreateTaskRequest("New task", "desc", TaskPriority.HIGH);
        Task document = buildTask(null, 5L);
        Task saved = buildTask("newId", 5L);
        TaskResponse response = buildResponse("newId", 5L);

        when(taskMapper.toDocument(request, 5L, "user@example.com")).thenReturn(document);
        when(taskRepository.save(document)).thenReturn(saved);
        when(taskMapper.toTaskResponse(saved)).thenReturn(response);

        TaskResponse result = taskService.createTask(request, 5L, "user@example.com");

        assertThat(result.id()).isEqualTo("newId");
        verify(taskRepository).save(document);
    }

    // -------------------------------------------------------------------------
    // updateTask
    // -------------------------------------------------------------------------

    @Test
    void updateTask_updatesFieldsAndSaves() {
        Task task = buildTask("t1", 3L);
        UpdateTaskRequest request = new UpdateTaskRequest("Updated", "new desc", TaskPriority.CRITICAL);
        Task saved = buildTask("t1", 3L);
        TaskResponse response = buildResponse("t1", 3L);

        when(taskRepository.findByIdAndUserId("t1", 3L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(saved);
        when(taskMapper.toTaskResponse(saved)).thenReturn(response);

        taskService.updateTask("t1", request, 3L);

        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository).save(captor.capture());
        assertThat(captor.getValue().getTitle()).isEqualTo("Updated");
        assertThat(captor.getValue().getDescription()).isEqualTo("new desc");
        assertThat(captor.getValue().getPriority()).isEqualTo(TaskPriority.CRITICAL);
    }

    @Test
    void updateTask_throwsNotFound_whenMissing() {
        when(taskRepository.findByIdAndUserId("bad", 1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.updateTask("bad",
                new UpdateTaskRequest("x", "y", TaskPriority.LOW), 1L))
                .isInstanceOf(TaskNotFoundException.class);
    }

    // -------------------------------------------------------------------------
    // deleteTask
    // -------------------------------------------------------------------------

    @Test
    void deleteTask_deletesTask_whenFound() {
        Task task = buildTask("d1", 2L);
        when(taskRepository.findByIdAndUserId("d1", 2L)).thenReturn(Optional.of(task));

        taskService.deleteTask("d1", 2L);

        verify(taskRepository).delete(task);
    }

    @Test
    void deleteTask_throwsNotFound_whenMissing() {
        when(taskRepository.findByIdAndUserId("missing", 2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.deleteTask("missing", 2L))
                .isInstanceOf(TaskNotFoundException.class);

        verify(taskRepository, never()).delete(any());
    }

    // -------------------------------------------------------------------------
    // completeTask / incompleteTask
    // -------------------------------------------------------------------------

    @Test
    void completeTask_setsStatusToCompleted() {
        Task task = buildTask("c1", 1L);
        when(taskRepository.findByIdAndUserId("c1", 1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toTaskResponse(task)).thenReturn(buildResponse("c1", 1L));

        taskService.completeTask("c1", 1L);

        assertThat(task.getStatus()).isEqualTo(TaskStatus.COMPLETED);
    }

    @Test
    void incompleteTask_setsStatusToInProgress() {
        Task task = buildTask("i1", 1L);
        task.setStatus(TaskStatus.COMPLETED);

        when(taskRepository.findByIdAndUserId("i1", 1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toTaskResponse(task)).thenReturn(buildResponse("i1", 1L));

        taskService.incompleteTask("i1", 1L);

        assertThat(task.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
    }

    // -------------------------------------------------------------------------
    // helpers
    // -------------------------------------------------------------------------

    private Task buildTask(String id, Long userId) {
        return Task.builder()
                .id(id)
                .title("Test task")
                .description("desc")
                .status(TaskStatus.IN_PROGRESS)
                .priority(TaskPriority.MEDIUM)
                .userId(userId)
                .email("user@example.com")
                .build();
    }

    private TaskResponse buildResponse(String id, Long userId) {
        return new TaskResponse(id, "Test task", "desc", "MEDIUM", "IN_PROGRESS",
                userId, "user@example.com", null, null);
    }
}