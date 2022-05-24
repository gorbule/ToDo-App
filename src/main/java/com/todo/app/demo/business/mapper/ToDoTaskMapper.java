package com.todo.app.demo.business.mapper;

import com.todo.app.demo.business.repository.model.ToDoTaskDAO;
import com.todo.app.demo.model.ToDoTask;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ToDoTaskMapper {

    ToDoTaskDAO taskModelTOTaskDAO(ToDoTask toDoTask);
    ToDoTask taskDaoToTaskModel(ToDoTaskDAO toDoTaskDAO);

}
