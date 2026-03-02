package com.example.taskservice.controller;

import com.example.taskservice.dto.CreateTaskRequest;
import com.example.taskservice.dto.TaskResponse;
import com.example.taskservice.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks(@RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(taskService.getAllTasksByUserId(userId));
    }
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(
            @PathVariable String id,
            @RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(taskService.getTaskByIdAndUserId(id, userId));
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @Valid @RequestBody CreateTaskRequest request,
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-User-Email")  String email) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskService.createTask(request, userId, email));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @Valid @RequestBody CreateTaskRequest request,
            @PathVariable String id,
            @RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(taskService.updateTask(id, request, userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable String id,
            @RequestHeader("X-User-Id") Long userId) {
        taskService.deleteTask(id, userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<TaskResponse> completeTask(
            @PathVariable String id,
            @RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(taskService.completeTask(id, userId));
    }

    @PatchMapping("/{id}/incomplete")
    public ResponseEntity<TaskResponse> incompleteTask(
            @PathVariable String id,
            @RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(taskService.incompleteTask(id, userId));
}
}
