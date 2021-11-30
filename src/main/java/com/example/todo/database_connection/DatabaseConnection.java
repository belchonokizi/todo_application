package com.example.todo.database_connection;

import com.example.todo.model.ToDo;
import org.springframework.beans.factory.annotation.Value;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {

    @Value("${spring.datasource.jdbc-url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String userName;

    @Value("${spring.datasource.password}")
    private String password;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public List<ToDo> getAllToDos() {
        List<ToDo> toDos = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM TODO")) {
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long parentId = resultSet.getLong("parent_id");
                String description = resultSet.getString("description");
                LocalDateTime createdAt = LocalDateTime.parse(resultSet.getString("created_at"), FORMATTER);
                LocalDateTime deadline = LocalDateTime.parse(resultSet.getString("deadline"), FORMATTER);
                Boolean isDone = resultSet.getBoolean("is_done");
                toDos.add(new ToDo(id, parentId, description, createdAt, deadline, isDone));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toDos;
    }

    public ToDo getToDo(Long id) {
        String description = "";
        LocalDateTime createdAt = null;
        LocalDateTime deadline = null;
        Boolean isDone = false;
        try (Connection connection = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM TODO WHERE ID = " + id)) {
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                description = resultSet.getString("description");
                createdAt = LocalDateTime.parse(resultSet.getString("created_at"), FORMATTER);
                deadline = LocalDateTime.parse(resultSet.getString("deadline"), FORMATTER);
                isDone = resultSet.getBoolean("is_done");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ToDo(id, null, description, createdAt, deadline, isDone);
    }

    public List<ToDo> getChildrenToDo(Long parentId) {
        List<ToDo> childrenToDo = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, userName, password);
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM TODO WHERE PARENT_ID = " + parentId)) {
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String description = resultSet.getString("description");
                LocalDateTime createdAt = LocalDateTime.parse(resultSet.getString("created_at"), FORMATTER);
                LocalDateTime deadline = LocalDateTime.parse(resultSet.getString("deadline"), FORMATTER);
                Boolean isDone = resultSet.getBoolean("is_done");
                childrenToDo.add(new ToDo(id, parentId, description, createdAt, deadline, isDone));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return childrenToDo;
    }

    public void setDoneTrue(Long id) {
        try (Connection connection = DriverManager.getConnection(url, userName, password);
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE TODO SET IS_DONE = TRUE WHERE ID = " + id)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addToDo(ToDo entity) {
        try (Connection connection = DriverManager.getConnection(url, userName, password);
             PreparedStatement preparedStatement = connection.prepareStatement(("INSERT INTO TODO (ID, PARENT_ID, DESCRIPTION, CREATED_AT, DEADLINE, IS_DONE) VALUES " +
                     "(?, ?, ?, ?, ?, ?)"))) {
            preparedStatement.setLong(1, entity.getId());
            preparedStatement.setLong(2, entity.getParentId());
            preparedStatement.setString(3, entity.getDescription());
            preparedStatement.setObject(4, entity.getCreatedAt());
            preparedStatement.setObject(5, entity.getDeadline());
            preparedStatement.setBoolean(6, entity.isDone());
            preparedStatement.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}


