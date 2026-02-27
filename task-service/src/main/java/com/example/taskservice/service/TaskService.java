package com.example.taskservice.service;

import com.example.taskservice.document.Task;
import com.example.taskservice.document.TaskStatus;
import com.example.taskservice.dto.CreateTaskRequest;
import com.example.taskservice.dto.TaskResponse;
import com.example.taskservice.exception.TaskNotFoundException;
import com.example.taskservice.mapper.TaskMapper;
import com.example.taskservice.repository.TaskRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public List<TaskResponse> getAllTasksByUserId(Long userId) {
        return taskRepository.findByUserId(userId).stream().map(taskMapper::toTaskResponse).toList();
    }

    public TaskResponse getTaskByIdAndUserId(String id, Long userId) {
        return taskRepository.findByIdAndUserId(id, userId).
                map(taskMapper::toTaskResponse).
                orElseThrow(() -> new TaskNotFoundException("Task not Found: " + id));
    }

    public TaskResponse createTask(CreateTaskRequest request, Long userId) {
        Task saved = taskRepository.save(taskMapper.toDocument(request, userId));
        return taskMapper.toTaskResponse(saved);
    }

    public TaskResponse updateTask(String id, CreateTaskRequest request, Long userId) {
        Task task = taskRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found: " + id));
        task.setTitle(request.title());
        task.setDescription(request.description());
        Task saved = taskRepository.save(task);
        return  taskMapper.toTaskResponse(saved);
    }

    public void deleteTask(String id, Long userId) {
        Task task = taskRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found: " + id));
        taskRepository.delete(task);
    }

    public TaskResponse completeTask(String id, Long userId) {
        Task task = taskRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found: " + id));
        task.setStatus(TaskStatus.COMPLETED);
        Task saved = taskRepository.save(task);
        return taskMapper.toTaskResponse(saved);
    }

    public TaskResponse incompleteTask(String id, Long userId) {
        Task task = taskRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found: " + id));
        task.setStatus(TaskStatus.IN_PROGRESS);
        Task saved = taskRepository.save(task);
        return taskMapper.toTaskResponse(saved);
    }
}
