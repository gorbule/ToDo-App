package com.todo.app.demo.business.service;

import com.todo.app.demo.model.Status;
import com.todo.app.demo.model.ToDoTask;

import java.util.List;
import java.util.Optional;


public interface ToDoTaskService {

    Optional<ToDoTask> getToDoTaskById(Long id);

    List<ToDoTask> getToDoTasksByStatus(Status status);

    List<ToDoTask> getAllToDoTasks();

    ToDoTask saveToDoTask(ToDoTask newToDoTask);

    ToDoTask updateToDoTask(ToDoTask updatedToDoTask);

    void deleteToDoTask(Long id);

}
