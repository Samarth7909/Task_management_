package com.taskmanager.model;

import java.time.LocalDateTime;

public class Task {

    private Long id;
    private String name;
    private String description;
    private TaskStatus status;
    private LocalDateTime createdDate;

    public enum TaskStatus {
        PENDING,
        COMPLETED
    }

    public Task() {}

    public Task(Long id, String name, String description, TaskStatus status, LocalDateTime createdDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.createdDate = createdDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
}
