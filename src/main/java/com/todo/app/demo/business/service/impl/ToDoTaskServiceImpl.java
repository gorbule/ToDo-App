package com.todo.app.demo.business.service.impl;

import com.todo.app.demo.business.mapper.ToDoTaskMapper;
import com.todo.app.demo.business.repository.ToDoRepository;
import com.todo.app.demo.business.repository.model.ToDoTaskDAO;
import com.todo.app.demo.business.service.ToDoTaskService;
import com.todo.app.demo.model.ToDoTask;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
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
        log.info("ToDo Task with id {} is {}", id, toDoTaskById);
        return toDoTaskById;
    }

    @Cacheable(value = "toDoTaskList")
    @Override
    public List<ToDoTask> getAllToDoTasks() {
        List<ToDoTaskDAO> toDoTaskDAOList = repository.findAll();
        log.info("Get list of Project Hiring Statuses. Size is: {}", toDoTaskDAOList::size);
        return toDoTaskDAOList.stream().map(toDoTaskMapper::taskDaoToTaskModel).collect(Collectors.toList());
    }

    @CacheEvict(cacheNames = "toDoTaskList", allEntries = true)
    @Override
    public ToDoTask postToDoTask(ToDoTask newToDoTask) {
        if (repository.findAll().stream()
                .anyMatch(p -> p.getTaskDescription().equals(newToDoTask.getTaskDescription()))) {
            log.info("ToDo Task conflict exception is thrown: {}", HttpStatus.CONFLICT);
            throw new HttpClientErrorException(HttpStatus.CONFLICT);
        }
        ToDoTaskDAO newToDoTaskDAO = repository.save(toDoTaskMapper.taskModelTOTaskDAO(newToDoTask));
        log.info("New ToDoTask is saved: {}", newToDoTask);
        return toDoTaskMapper.taskDaoToTaskModel(newToDoTaskDAO);
    }

    @CacheEvict(cacheNames = "toDoTaskList", allEntries = true)
    @Override
    public void deleteToDoTask(Long id) {
        repository.deleteById(id);
        log.info("ToDo Task with id {} is deleted", id);
    }
}
