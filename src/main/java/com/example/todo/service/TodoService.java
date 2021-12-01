package com.example.todo.service;

import com.example.todo.model.ToDo;

import java.util.List;

public interface TodoService {

    void createToDo(ToDo entity);

    void markAsDone(Long id);

    List<ToDo> getInProgressToDos();

    void sendMessageAboutDeadline();

    void sendMessageAboutIllegalOperation();

    void sendMessageAboutAccessError();
}
