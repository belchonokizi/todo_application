package com.example.todo.model;

import java.time.LocalDateTime;

public class ToDo {
    private Long id;
    private Long parentId;

    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime deadline;
    private boolean isDone;

    private ToDo child;

    public ToDo() {
    }

    public ToDo(Long id, Long parentId, String description, LocalDateTime createdAt, LocalDateTime deadline, boolean isDone, ToDo child) {
        this.id = id;
        this.parentId = parentId;
        this.description = description;
        this.createdAt = createdAt;
        this.deadline = deadline;
        this.isDone = isDone;
        this.child = child;
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

    public ToDo getChild() {
        return child;
    }

    public void setChildren(ToDo child) {
        this.child = child;
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
                ", child=" + child +
                '}';
    }
}
