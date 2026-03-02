package com.example.scheduler.service;

import com.example.scheduler.dto.TaskResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceClient {

    private final WebClient webClient;

    @Value("${services.task-service.url}")
    private String taskServiceUrl;

    public List<TaskResponse> getAllTasks() {
        log.info("Fetching all tasks from task-service");
        return webClient.get()
                .uri(taskServiceUrl + "/internal/tasks/all")
                .retrieve()
                .bodyToFlux(TaskResponse.class)
                .collectList()
                .block();
    }
}
