package com.example.taskservice.repository;


import com.example.taskservice.document.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends MongoRepository<Task,String> {
    List<Task> findByUserId(Long userId);
    Optional<Task> findByIdAndUserId(String id, Long userId);
    void deleteByUserIdAndId(Long userId,String taskId);
}
