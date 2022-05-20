package com.todo.app.demo.business.service.impl;


import com.todo.app.demo.business.mapper.ToDoTaskMapper;
import com.todo.app.demo.business.repository.ToDoRepository;
import com.todo.app.demo.business.repository.model.ToDoTaskDAO;
import com.todo.app.demo.business.service.ToDoTaskService;
import com.todo.app.demo.model.ToDoTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ToDoTaskServiceImpl implements ToDoTaskService {

    @Autowired
    ToDoRepository repository;

    @Autowired
    ToDoTaskMapper toDoTaskMapper;


    @Override
    public Optional<ToDoTask> getToDoTaskById(Long id) {
        Optional<ToDoTask> toDoTaskById = repository.findById(id)
                .flatMap(toDoTask -> Optional.ofNullable(toDoTaskMapper
                        .taskDaoToTaskModel(toDoTask)));
//        log.info("ToDo Task with id {} is {}", id, toDoTaskById);
        System.out.println(("ToDo Task with id " + id + " is: " + toDoTaskById));
        return toDoTaskById;
    }

    @Override
    public List<ToDoTask> getAllToDoTasks() {
        List<ToDoTaskDAO> toDoTaskDAOList = repository.findAll();
////        log.info("Get list of Project Hiring Statuses. Size is: {}", toDoTaskDAOList::size);
        return toDoTaskDAOList.stream().map(toDoTaskMapper::taskDaoToTaskModel).collect(Collectors.toList());
    }

    @Override
    public ToDoTask postToDoTask(ToDoTask newToDoTask) {
        return null;
    }
}
