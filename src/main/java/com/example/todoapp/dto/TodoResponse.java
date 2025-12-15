package com.example.todoapp.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class TodoResponse {

    private Long id;          // DB ID
    private Integer displayId; // UI ID
    private String title;
    private String description;
    private LocalDate dueDate;
    private LocalTime dueTime;
    private String category;
    private String priority;
    private boolean completed;

    /* Getters & Setters */

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getDisplayId() { return displayId; }
    public void setDisplayId(Integer displayId) { this.displayId = displayId; }

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

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}
