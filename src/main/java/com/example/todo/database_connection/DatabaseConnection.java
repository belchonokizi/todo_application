package com.example.todo.database_connection;

import com.example.todo.model.ToDo;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {
    private static final DatabaseConnection DATABASE_CONNECTION = new DatabaseConnection();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final String CONNECTION_URL = "jdbc:h2:C:\\Users\\belch\\demo\\src\\main\\java\\database\\ToDos;MV_STORE=false";
    private static final String USER_NAME = "bestuser";
    private static final String PASSWORD = "bestuser";

    public List<ToDo> getAllToDos() {
        List<ToDo> toDos = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(CONNECTION_URL, USER_NAME, PASSWORD)) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM TODO");
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long parentId = resultSet.getLong("parent_id");
                String description = resultSet.getString("description");
                LocalDateTime createdAt = LocalDateTime.parse(resultSet.getString("created_at"), FORMATTER);
                LocalDateTime deadline = LocalDateTime.parse(resultSet.getString("deadline"), FORMATTER);
                Boolean isDone = resultSet.getBoolean("is_done");
                //вытащить из базы детей
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
        try (Connection connection = DriverManager.getConnection(CONNECTION_URL, USER_NAME, PASSWORD)) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM TODO WHERE ID = " + id);
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

        List<ToDo> children = DATABASE_CONNECTION.getChildrenToDo(id);

        return new ToDo(id, null, description, createdAt, deadline, isDone, children);
    }

    public List<ToDo> getChildrenToDo(Long parentId) {
        List<ToDo> childrenToDo = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(CONNECTION_URL, USER_NAME, PASSWORD)) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM TODO WHERE PARENT_ID = " + parentId);
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
        try (Connection connection = DriverManager.getConnection(CONNECTION_URL, USER_NAME, PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE TODO SET IS_DONE = TRUE WHERE ID = " + id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
//        DATABASE_CONNECTION.getAllToDos().forEach(System.out::println);
        System.out.println(DATABASE_CONNECTION.getToDo(1L));
//        DATABASE_CONNECTION.getChildrenToDo(1l).forEach(System.out::println);
    }
}


