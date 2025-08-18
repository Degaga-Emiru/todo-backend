package com.example.todoapplication.service.impl;

import com.example.todoapplication.dto.request.TaskRequest;
import com.example.todoapplication.dto.response.TaskResponse;
import com.example.todoapplication.exception.CustomException;
import com.example.todoapplication.model.Task;
import com.example.todoapplication.model.User;
import com.example.todoapplication.repository.TaskRepository;
import com.example.todoapplication.repository.UserRepository;
import com.example.todoapplication.service.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public TaskResponse createTask(TaskRequest taskRequest, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found"));

        Task task = new Task();
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setCompleted(taskRequest.isCompleted());
        task.setDueDate(taskRequest.getDueDate()); // Make sure this line exists
        task.setUser(user);
        Task savedTask = taskRepository.save(task);
        return convertToTaskResponse(savedTask);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }
    @Override
    public Optional<Task> findByIdAndUserId(Long taskId, Long userId) {
        return taskRepository.findByIdAndUserId(taskId, userId);
    }
    @Override
    public List<Task> findByUserId(Long userId) {
        return taskRepository.findByUserId(userId);
    }
    @Override
    public Task save(Task task) {
        return taskRepository.save(task);
    }
    @Override
    public void deleteById(Long id) {

        taskRepository.deleteById(id);
    }
    @Override
    public void deleteByIdAndUserId(Long taskId, Long userId) {
        taskRepository.deleteByIdAndUserId(taskId, userId);
    }
    @Override
    public boolean existsByIdAndUserId(Long taskId, Long userId) {
        return taskRepository.existsByIdAndUserId(taskId, userId);
    }
    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    @Transactional
    public TaskResponse updateTask(Long taskId, TaskRequest taskRequest, Long userId) {
        Task task = taskRepository.findByIdAndUserId(taskId, userId)
                .orElseThrow(() -> new CustomException("Task not found or you don't have permission"));

        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setCompleted(taskRequest.isCompleted());
        task.setUpdatedAt(LocalDateTime.now());
        task.setDueDate(taskRequest.getDueDate());
        Task updatedTask = taskRepository.save(task);
        return convertToTaskResponse(updatedTask);
    }

    @Override
    @Transactional
    public void deleteTask(Long taskId, Long userId) {
        Task task = taskRepository.findByIdAndUserId(taskId, userId)
                .orElseThrow(() -> new CustomException("Task not found or you don't have permission"));
        taskRepository.delete(task);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponse getTaskById(Long taskId, Long userId) {
        Task task = taskRepository.findByIdAndUserId(taskId, userId)
                .orElseThrow(() -> new CustomException("Task not found or you don't have permission"));
        return convertToTaskResponse(task);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getAllTasksByUserId(Long userId) {
        return taskRepository.findByUserId(userId).stream()
                .map(this::convertToTaskResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(this::convertToTaskResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TaskResponse toggleTaskCompletion(Long taskId, Long userId) {
        Task task = taskRepository.findByIdAndUserId(taskId, userId)
                .orElseThrow(() -> new CustomException("Task not found or you don't have permission"));

        task.setCompleted(!task.isCompleted());
        task.setUpdatedAt(LocalDateTime.now());

        Task updatedTask = taskRepository.save(task);
        return convertToTaskResponse(updatedTask);
    }

    private TaskResponse convertToTaskResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.isCompleted(),
                task.getCreatedAt(),
                task.getUpdatedAt(),
                task.getDueDate(),
                task.getUser().getId(),
                task.getUser().getUsername()
        );
    }
}
