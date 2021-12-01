package com.example.todo.service;

import com.example.todo.database_connection.DatabaseConnection;
import com.example.todo.model.ToDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ToDoServiceImpl implements TodoService {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ToDoServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final DatabaseConnection CONNECTION = new DatabaseConnection();

    @Override
    public void createToDo(ToDo entity) {
        CONNECTION.addToDo(entity);
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
    public List<ToDo> getInProgressToDos() {
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
}
