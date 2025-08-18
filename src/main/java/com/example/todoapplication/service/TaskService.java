package com.example.todoapplication.service;

import com.example.todoapplication.dto.request.TaskRequest;
import com.example.todoapplication.dto.response.TaskResponse;
import com.example.todoapplication.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    TaskResponse createTask(TaskRequest taskRequest, Long userId);
    TaskResponse updateTask(Long taskId, TaskRequest taskRequest, Long userId);
    void deleteTask(Long taskId, Long userId);
    TaskResponse getTaskById(Long taskId, Long userId);
    List<TaskResponse> getAllTasksByUserId(Long userId);
    List<TaskResponse> getAllTasks();
    TaskResponse toggleTaskCompletion(Long taskId, Long userId);
    Optional<Task> findById(Long id);
    Optional<Task> findByIdAndUserId(Long taskId, Long userId);
    List<Task> findByUserId(Long userId);
    Task save(Task task);
    void deleteById(Long id);
    void deleteByIdAndUserId(Long id, Long userId);
    boolean existsByIdAndUserId(Long id, Long userId);
    List<Task> findAll();
}