package com.example.todoapplication.repository;

import com.example.todoapplication.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    // Find all tasks by user ID
    List<Task> findByUserId(Long userId);

    // Find all completed/incomplete tasks for a user
    List<Task> findByUserIdAndCompleted(Long userId, boolean completed);
        Optional<Task> findByIdAndUserId(Long taskId, Long userId);


    // Check if a task exists with given ID and user ID
    boolean existsByIdAndUserId(Long id, Long userId);

    // Count all tasks for a user
    long countByUserId(Long userId);

    // Custom query to find overdue tasks
    @Query("SELECT t FROM Task t WHERE t.user = :userId AND t.dueDate < CURRENT_DATE AND t.completed = false")
    List<Task> findOverdueTasks(@Param("userId") Long userId);

    // Delete all tasks for a user
    void deleteAllByUserId(Long userId);

    void deleteById(Long userId);
    void deleteByIdAndUserId(Long id, Long userId);


}