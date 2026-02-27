package com.example.taskservice.controller;

import com.example.taskservice.dto.CreateTaskRequest;
import com.example.taskservice.dto.TaskResponse;
import com.example.taskservice.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks(Authentication authentication) {
        return ResponseEntity.ok(taskService.getAllTasksByUserId(getUserId(authentication)));
    }
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(
            @PathVariable String id,
            Authentication authentication) {
        return ResponseEntity.ok(taskService.getTaskByIdAndUserId(id, getUserId(authentication)));
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @Valid @RequestBody CreateTaskRequest request,
            Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(taskService.createTask(request, getUserId(authentication)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @Valid @RequestBody CreateTaskRequest request,
            @PathVariable String id,
            Authentication authentication) {
        return ResponseEntity.ok(taskService.updateTask(id, request, getUserId(authentication)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable String id,
            Authentication authentication) {
        taskService.deleteTask(id, getUserId(authentication));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<TaskResponse> completeTask(
            @PathVariable String id,
            Authentication authentication) {
        return ResponseEntity.ok(taskService.completeTask(id, getUserId(authentication)));
    }

    @PatchMapping("/{id}/incomplete")
    public ResponseEntity<TaskResponse> incompleteTask(
            @PathVariable String id,
            Authentication authentication) {
        return ResponseEntity.ok(taskService.incompleteTask(id, getUserId(authentication)));
}

    private Long getUserId(Authentication authentication) {
        return (Long) authentication.getPrincipal();
    }
}
