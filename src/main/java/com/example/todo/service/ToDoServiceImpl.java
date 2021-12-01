package com.example.todo.service;

import com.example.todo.model.ToDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.util.Comparator.comparing;

@Service
public class ToDoServiceImpl implements TodoService {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ToDoServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createToDo(ToDo entity) {
        jdbcTemplate.update(
                "INSERT INTO TODO VALUES (?, ?, ?, ?, ?, ?)", entity.getId(), entity.getParentId(), entity.getDescription(),
                entity.getCreatedAt(), entity.getDeadline(), entity.isDone());
    }

    @Override
    public void markAsDone(Long id) {
        ToDo todo = getTodoById(id);
        List<ToDo> children = getTodoByParentId(id);
        for (ToDo child : children) {
            if (!child.isDone()) {
                return;
            }
        }
        todo.setDone(true);
        jdbcTemplate.update("UPDATE TODO SET IS_DONE = TRUE WHERE ID = " + id);
    }

    @Override
    public List<ToDo> getInProgressToDos() {
        String query = "SELECT * FROM TODO WHERE IS_DONE = FALSE";
        List<ToDo> toDoList = jdbcTemplate.query(query, new Object[]{}, new TodoRowMapper());
        toDoList.sort(comparing(ToDo::getId).thenComparing(ToDo::getParentId).thenComparing(ToDo::getDeadline));
        return toDoList;
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

    private ToDo getTodoById(long id) {
        String query = "SELECT * FROM TODO WHERE ID = ?";
        return jdbcTemplate.queryForObject(query, new Object[] {id}, new TodoRowMapper());
    }

    private List<ToDo> getTodoByParentId(long parentId) {
        String query = "SELECT * FROM TODO WHERE PARENT_ID = ?";
        return jdbcTemplate.query(query, new Object[] {parentId}, new TodoRowMapper());
    }

    public Long generateId() {
        Long maxId = jdbcTemplate.queryForObject("SELECT MAX(ID) FROM TODO", Long.class);
        return maxId++;
    }

    static class TodoRowMapper implements RowMapper<ToDo> {
        final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        @Override
        public ToDo mapRow(ResultSet rs, int rowNum) throws SQLException {
            Long id = rs.getLong("ID");
            Long parentId = rs.getLong("PARENT_ID");
            String description = rs.getString("DESCRIPTION");
            LocalDateTime createdAt = LocalDateTime.parse(rs.getString("CREATED_AT"), FORMATTER);
            LocalDateTime deadline = LocalDateTime.parse(rs.getString("DEADLINE"), FORMATTER);
            Boolean isDone = rs.getBoolean("IS_DONE");

            return new ToDo(id, parentId, description, createdAt, deadline, isDone);
        }
    }
}
