package com.example.todoapp.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.time.LocalTime;

public class TodoRequest {

    @NotBlank
    private String title;

    private String description;
    private LocalDate dueDate;
    private LocalTime dueTime;

    @NotBlank
    private String category;

    @NotBlank
    private String priority;

    /* Getters & Setters */

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public LocalTime getDueTime() { return dueTime; }
    public void setDueTime(LocalTime dueTime) { this.dueTime = dueTime; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
}
