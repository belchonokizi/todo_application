package com.example.todo.service;

import com.example.todo.database_connection.DatabaseConnection;
import com.example.todo.model.ToDo;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ToDoServiceImpl implements TodoService{
    private static final DatabaseConnection CONNECTION = new DatabaseConnection();

    @Override
    public ToDo createToDo(Long id, Long parentId, String description, LocalDateTime createdAt, LocalDateTime deadline, boolean isDone, List<ToDo> children) {
        return new ToDo(id, parentId, description, createdAt, deadline, isDone, children);
    }

    @Override
    public void markAsDone(Long id) {
        ToDo toDo = CONNECTION.getToDo(id);
        List<ToDo> childrenToDo = CONNECTION.getChildrenToDo(id);
        for (ToDo child : childrenToDo) {
            if (!child.isDone()) {
                return;
            }
        }
        toDo.setDone(true);
        CONNECTION.setDoneTrue(id);
    }

    @Override
    public List<ToDo> getUnfulfilledToDos() {
        List<ToDo> allToDos = CONNECTION.getAllToDos();
        List<ToDo> doneToDos = allToDos.stream().filter(ToDo::isDone).collect(Collectors.toList());
        allToDos.removeAll(doneToDos);
        allToDos.sort(Comparator.comparing(ToDo::getId).thenComparing(ToDo::getParentId).thenComparing(ToDo::getDeadline));
        return allToDos;
    }

    @Override
    public void sendMessageAboutDeadline() {

    }

    @Override
    public void sendMessageAboutIllegalOperation() {

    }

    @Override
    public void sendMessageAboutAccessError() {

    }

    public static void main(String[] args) {
        ToDoServiceImpl toDoService = new ToDoServiceImpl();
//        toDoService.getUnfulfilledToDos().forEach(System.out::println);
        toDoService.markAsDone(1L);
    }
}
