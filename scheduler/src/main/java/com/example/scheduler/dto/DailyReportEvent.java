package com.example.scheduler.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DailyReportEvent(
        String email,
        List<TaskInfo> tasks
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record TaskInfo(
            String id,
            String title,
            String description,
            String status  // TaskStatus.name() → "IN_PROGRESS", "DONE" etc.
    ) {}
}
