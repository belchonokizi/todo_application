package com.example.todo.model;

import com.example.todo.service.ToDoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class ToDo {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Long id;
    private Long parentId;

    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime deadline;
    private boolean isDone;

    private ToDoServiceImpl toDoService;

    @Autowired
    public ToDo(ToDoServiceImpl toDoService) {
        this.toDoService = toDoService;
    }

    public ToDo(String description, String deadline) {
        this(null, description, deadline);
    }

    public ToDo(Long parentId, String description, String deadline) {
        this.id = toDoService.generateId();
        this.parentId = parentId;
        this.description = description;
        this.createdAt = LocalDateTime.now();
        this.deadline = LocalDateTime.parse(deadline, FORMATTER);
        this.isDone = false;
    }

    public ToDo(Long id, Long parentId, String description, LocalDateTime createdAt, LocalDateTime deadline, boolean isDone) {
        this.id = id;
        this.parentId = parentId;
        this.description = description;
        this.createdAt = createdAt;
        this.deadline = deadline;
        this.isDone = isDone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    @Override
    public String toString() {
        return "ToDo{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", deadline=" + deadline +
                ", isDone=" + isDone +
                '}';
    }
}
