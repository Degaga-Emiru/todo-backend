package com.example.todoapplication.controller;

import com.example.todoapplication.dto.request.TaskRequest;
import com.example.todoapplication.dto.response.TaskResponse;
import com.example.todoapplication.exception.CustomException;
import com.example.todoapplication.model.Task;
import com.example.todoapplication.model.User;
import com.example.todoapplication.repository.UserRepository;
import com.example.todoapplication.security.UserDetailsImpl;
import com.example.todoapplication.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;
    private final UserRepository userRepository;

    public TaskController(TaskService taskService, UserRepository userRepository) {
        this.taskService = taskService;
        this.userRepository = userRepository;
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<TaskResponse>> getAllTasks(Authentication authentication) {
        List<Task> tasks;
        if (isAdmin(authentication)) {
            tasks = taskService.findAll();
        } else {
            tasks = taskService.findByUserId(getCurrentUserId(authentication));
        }

        List<TaskResponse> taskResponses = tasks.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(taskResponses);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id, Authentication authentication) {
        Task task = taskService.findById(id)
                .orElseThrow(() -> new CustomException("Task not found with id: " + id));

        if (!isAdmin(authentication)) {
            checkTaskOwnership(task, authentication);
        }

        return ResponseEntity.ok(convertToResponse(task));
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TaskResponse> createTask(
            @RequestBody TaskRequest taskRequest,
            Authentication authentication) {

        Long currentUserId = getCurrentUserId(authentication);
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new CustomException("User not found with id: " + currentUserId));

        Task task = new Task();
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setCompleted(taskRequest.isCompleted());
        task.setDueDate(taskRequest.getDueDate());
        task.setUser(user);

        Task createdTask = taskService.save(task);
        return ResponseEntity.ok(convertToResponse(createdTask));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long id,
            @RequestBody TaskRequest taskRequest,
            Authentication authentication) {
        Task existingTask = taskService.findById(id)
                .orElseThrow(() -> new CustomException("Task not found with id: " + id));

        if (!isAdmin(authentication)) {
            checkTaskOwnership(existingTask, authentication);
        }

        existingTask.setTitle(taskRequest.getTitle());
        existingTask.setDescription(taskRequest.getDescription());
        existingTask.setCompleted(taskRequest.isCompleted());

        Task updatedTask = taskService.save(existingTask);
        return ResponseEntity.ok(convertToResponse(updatedTask));
    }

    @PatchMapping("/{id}/complete")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<TaskResponse> markTaskAsComplete(
            @PathVariable Long id,
            @RequestParam boolean completed,
            Authentication authentication) {
        Task existingTask = taskService.findById(id)
                .orElseThrow(() -> new CustomException("Task not found with id: " + id));

        if (!isAdmin(authentication)) {
            checkTaskOwnership(existingTask, authentication);
        }

        existingTask.setCompleted(completed);
        Task updatedTask = taskService.save(existingTask);
        return ResponseEntity.ok(convertToResponse(updatedTask));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long id,
            Authentication authentication) {
        Task task = taskService.findById(id)
                .orElseThrow(() -> new CustomException("Task not found with id: " + id));

        if (!isAdmin(authentication)) {
            checkTaskOwnership(task, authentication);
        }

        taskService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private TaskResponse convertToResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.isCompleted(),
                task.getCreatedAt(),
                task.getUpdatedAt(),
                task.getDueDate(),
                task.getUser().getId(),
                task.getUser().getUsername());
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    }

    private Long getCurrentUserId(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getId();
    }

    private void checkTaskOwnership(Task task, Authentication authentication) {
        if (!task.getUser().getId().equals(getCurrentUserId(authentication))) {
            throw new CustomException("You don't have permission to access this task");
        }
    }
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<List<TaskResponse>> getTasksByUserId(
            @PathVariable Long userId,
            Authentication authentication) {

        List<Task> tasks = taskService.findByUserId(userId);
        List<TaskResponse> taskResponses = tasks.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(taskResponses);
    }
}