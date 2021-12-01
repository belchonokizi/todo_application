package com.example.todo.shell;

import com.example.todo.model.ToDo;
import com.example.todo.service.ToDoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class TodoCommands {
    private ToDoServiceImpl toDoService;

    @Autowired
    public TodoCommands(ToDoServiceImpl toDoService) {
        this.toDoService = toDoService;
    }

    @ShellMethod
    public void create(@ShellOption({"-D", "--description"}) String description, @ShellOption({"-Dl", "--deadline"}) String deadline) {
        toDoService.createToDo(new ToDo(description, deadline));
    }

    public void createChild(@ShellOption({"-P", "--parentId"}) long parentId,
                            @ShellOption({"-D", "--description"}) String description,
                            @ShellOption({"-Dl", "--deadline"}) String deadline) {
        toDoService.createToDo(new ToDo(parentId, description, deadline));
    }
}
