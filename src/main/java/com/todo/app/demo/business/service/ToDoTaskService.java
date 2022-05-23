package com.todo.app.demo.business.service;

import com.todo.app.demo.model.ToDoTask;

import java.util.List;
import java.util.Optional;

public interface ToDoTaskService {

    Optional<ToDoTask> getToDoTaskById(Long id);

    List<ToDoTask> getAllToDoTasks();

    ToDoTask postToDoTask(ToDoTask newToDoTask);

    void deleteToDoTask(Long id);

}
