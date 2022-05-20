package com.todo.app.demo.business.mapper;

import com.todo.app.demo.business.repository.model.ToDoTaskDAO;
import com.todo.app.demo.model.ToDoTask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ToDoTaskMapper {

//    ToDoTaskMapper INSTANCE = Mappers.getMapper(ToDoTaskMapper.class);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "taskDescription", target = "taskDescription"),
            @Mapping(source = "status", target = "status"),
            @Mapping(source = "taskPriority", target = "taskPriority"),
    })
    ToDoTaskDAO taskModelTOTaskDAO(ToDoTask toDoTask);


    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "taskDescription", target = "taskDescription"),
            @Mapping(source = "status", target = "status"),
            @Mapping(source = "taskPriority", target = "taskPriority"),
    })
    ToDoTask taskDaoToTaskModel(ToDoTaskDAO toDoTaskDAO);

}
