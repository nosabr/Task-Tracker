package com.example.scheduler.service;

import com.example.scheduler.dto.DailyReportEvent;
import com.example.scheduler.dto.TaskResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportScheduler {

    private final TaskServiceClient taskServiceClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Scheduled(cron = "${scheduler.daily-report.cron}")
    public void sendDailyReports() {
        log.info("Starting daily report job");

        List<TaskResponse> allTasks = taskServiceClient.getAllTasks();

        if (allTasks.isEmpty()) {
            log.info("No tasks found, skipping daily report");
            return;
        }

        // Группируем задачи по email пользователя
        Map<String, List<TaskResponse>> tasksByEmail = allTasks.stream()
                .collect(Collectors.groupingBy(TaskResponse::email));

        tasksByEmail.forEach((email, tasks) -> {
            List<DailyReportEvent.TaskInfo> taskInfos = tasks.stream()
                    .map(task -> new DailyReportEvent.TaskInfo(
                            task.id(),
                            task.title(),
                            task.description(),
                            task.status()
                    ))
                    .toList();

            DailyReportEvent event = new DailyReportEvent(email, taskInfos);

            kafkaTemplate.send("task-events", email, event);
            log.info("Published DailyReportEvent for {} with {} tasks", email, taskInfos.size());
        });

        log.info("Daily report job finished, sent reports to {} users", tasksByEmail.size());
    }
}
