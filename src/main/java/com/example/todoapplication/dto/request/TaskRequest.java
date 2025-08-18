package com.example.todoapplication.dto.request;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.Date;

public class TaskRequest {
    @NotBlank(message = "Title cannot be blank")
    @Size(max = 100, message = "Title cannot exceed 100 characters")
    private String title;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @FutureOrPresent(message = "Due date must be in the present or future")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dueDate;  // or Date if not using Java 8+

    private boolean completed = false;

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @FutureOrPresent(message = "Due date cannot be in the past")
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }


    @FutureOrPresent(message = "Due date cannot be in the past")
    public LocalDate getDueDate() {
        return this.dueDate;
    }


}
