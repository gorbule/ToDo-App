package com.todo.app.demo.business.service.impl;

import com.todo.app.demo.business.mapper.ToDoTaskMapper;
import com.todo.app.demo.business.repository.ToDoRepository;
import com.todo.app.demo.business.repository.model.ToDoTaskDAO;
import com.todo.app.demo.business.service.ToDoTaskService;
import com.todo.app.demo.model.Status;
import com.todo.app.demo.model.TaskPriority;
import com.todo.app.demo.model.ToDoTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * ToDoTaskServiceImpl class. Holds all the methods for application business logic.
 * Methods necessary to operate with data.
 * Available 6 methods: getToDoTaskById(), getToDoTasksByStatus(), getToDoTasksByPriority(), getAllToDoTasks(),  saveToDoTask(), deleteToDoTask.
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class ToDoTaskServiceImpl implements ToDoTaskService {

    private final ToDoRepository repository;

    private final ToDoTaskMapper toDoTaskMapper;

    /**
     * Method getToDoTaskById that retrieves task from the database if valid id is provided.
     * @param id
     * @return
     */
    @Override
    public Optional<ToDoTask> getToDoTaskById(Long id) {
        Optional<ToDoTask> toDoTaskById = repository.findById(id)
                .flatMap(toDoTask -> Optional.ofNullable(toDoTaskMapper
                        .taskDaoToTaskModel(toDoTask)));
        log.info("ToDo Task with id {} is {}", id, toDoTaskById);
        return toDoTaskById;
    }

    /**
     * Method getToDoTasksByStatus that retrieves all tasks from the database by specified status.
     * @return
     */
    @Override
    public List<ToDoTask> getToDoTasksByStatus(Status status) {
        List <ToDoTask> listWithTasksByStatus = getAllToDoTasks()
                .stream()
                .filter(t -> t.getStatus().equals(status))
                .collect(Collectors.toList());
        log.info("ToDo Tasks with status {} : {}. List size: {}.",
                status, listWithTasksByStatus, listWithTasksByStatus.size());
        return listWithTasksByStatus;
    }
    /**
     * Method getToDoTasksByStatus that retrieves all tasks from the database by specified status.
     * @return
     */
    @Override
    public List<ToDoTask> getToDoTasksByPriority(TaskPriority taskPriority) {
        List<ToDoTask> listWithTasksByPriority = getAllToDoTasks()
                .stream()
                .filter(t -> t.getTaskPriority().equals(taskPriority))
                .collect(Collectors.toList());
        log.info("ToDo Tasks with priority level {} : {}. List size: {}.",
                taskPriority, listWithTasksByPriority, listWithTasksByPriority.size());
        return listWithTasksByPriority;
    }

    /**
     * Method getAllToDoTasks that retrieves all tasks from the database.
     * @return
     */
    @Cacheable(value = "toDoTaskList")
    @Scheduled(fixedDelay = 300000)
    @Override
    public List<ToDoTask> getAllToDoTasks() {
        List<ToDoTaskDAO> toDoTaskDAOList = repository.findAll();
        log.info("Get list of ToDo Tasks. List size is: {}", toDoTaskDAOList::size);
        return toDoTaskDAOList.stream().map(toDoTaskMapper::taskDaoToTaskModel).collect(Collectors.toList());
    }

    /**
     * Method saveToDoTask saves new created Task in the database if required data is provided.
     * Task description should not match to already existing.
     * @param newToDoTask
     * @return
     */
    @CacheEvict(cacheNames = "toDoTaskList", allEntries = true)
    @Override
    public ToDoTask saveToDoTask(ToDoTask newToDoTask) {
        if (repository.findAll().stream()
                .anyMatch(p -> p.getTaskDescription().equals(newToDoTask.getTaskDescription()))) {
            log.info("ToDo Task conflict exception is thrown: {}", HttpStatus.CONFLICT);
            throw new HttpClientErrorException(HttpStatus.CONFLICT);
        }
        ToDoTaskDAO newToDoTaskDAO = repository.save(toDoTaskMapper.taskModelTOTaskDAO(newToDoTask));
        log.info("New ToDoTask is saved: {}", newToDoTask);
        return toDoTaskMapper.taskDaoToTaskModel(newToDoTaskDAO);
    }

    /**
     * Method updateToDoTask that save updated Task in the database if required data is provided.
     * @param updatedToDoTask
     * @return
     */
    @CacheEvict(cacheNames = "toDoTaskList", allEntries = true)
    @Override
    public ToDoTask updateToDoTask(ToDoTask updatedToDoTask) {
        ToDoTaskDAO updatedToDoTaskDAO = repository.save(toDoTaskMapper.taskModelTOTaskDAO(updatedToDoTask));
        log.info("New ToDoTask is saved: {}", updatedToDoTask);
        return toDoTaskMapper.taskDaoToTaskModel(updatedToDoTaskDAO);
    }

    /**
     * Method deleteToDoTask  that delete Task from the database if valid id is provided.
     * @param id
     */
    @CacheEvict(cacheNames = "toDoTaskList", allEntries = true)
    @Override
    public void deleteToDoTask(Long id) {
        repository.deleteById(id);
        log.info("ToDo Task with id {} is deleted", id);
    }
}
