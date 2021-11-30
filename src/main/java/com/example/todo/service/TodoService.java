package com.example.todo.service;

import com.example.todo.model.ToDo;

import java.time.LocalDateTime;
import java.util.List;

public interface TodoService {

    ToDo createToDo(Long id, Long parentId, String description, LocalDateTime createdAt, LocalDateTime deadline, boolean isDone, List<ToDo> children);

    void markAsDone(Long id);

    List<ToDo> getUnfulfilledToDos();

    void sendMessageAboutDeadline();

    void sendMessageAboutIllegalOperation();

    void sendMessageAboutAccessError();
}
