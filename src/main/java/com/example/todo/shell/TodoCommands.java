package com.example.todo.shell;

import com.example.todo.model.ToDo;
import com.example.todo.service.ToDoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;

import java.util.UUID;

@ShellComponent
public class TodoCommands {
    private final ToDoServiceImpl toDoService;
    private String token;

    @Autowired
    public TodoCommands(ToDoServiceImpl toDoService) {
        this.toDoService = toDoService;
    }

    @ShellMethod()
    public void create(@ShellOption({"-D", "--description"}) String description, @ShellOption({"-Dl", "--deadline"}) String deadline) {
        toDoService.createToDo(new ToDo(description, deadline));
    }

    @ShellMethod
    public void createChild(@ShellOption({"-P", "--parentId"}) long parentId,
                            @ShellOption({"-D", "--description"}) String description,
                            @ShellOption({"-Dl", "--deadline"}) String deadline) {
        toDoService.createToDo(new ToDo(parentId, description, deadline));
    }

    @ShellMethod
    public void markIsDone(@ShellOption({"-id"}) long id) {
        toDoService.markAsDone(id);
    }

    @ShellMethod
    public void getInProgressTodos() {
        toDoService.getInProgressToDos();
    }

    @ShellMethod("Authenticate and obtain token")
    public void login(@ShellOption String userName, @ShellOption String password) {
        token = UUID.randomUUID().toString();
    }

    @ShellMethodAvailability({"create", "createChild", "markIsDone", "getInProgressTodos"})
    public void listAvailable() {
        if (token != null) {
            Availability.available();
        } else Availability.unavailable("Can`t run without auth token");
    }
}
