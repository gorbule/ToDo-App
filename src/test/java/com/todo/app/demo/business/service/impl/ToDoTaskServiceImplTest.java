package com.todo.app.demo.business.service.impl;

import com.todo.app.demo.business.mapper.ToDoTaskMapper;
import com.todo.app.demo.business.repository.ToDoRepository;
import com.todo.app.demo.business.repository.model.ToDoTaskDAO;
import com.todo.app.demo.model.Status;
import com.todo.app.demo.model.TaskPriority;
import com.todo.app.demo.model.ToDoTask;
import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@DisplayName("Test Case for ToDoTaskServiceImpl class methods")
class ToDoTaskServiceImplTest {

    @InjectMocks
    private ToDoTaskServiceImpl service;

    @Spy
    private ToDoRepository repository;

    @Spy
    private ToDoTaskMapper mapper;

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    private ToDoTask toDoTask;
    private ToDoTask toDoTask_2;
    private ToDoTaskDAO toDoTaskDAO;
    private ToDoTaskDAO toDoTaskDAO_2;
    private List<ToDoTask> toDoTaskList;
    private List<ToDoTaskDAO> toDoTaskDAOList;

    @BeforeEach
    public void init() {
        toDoTask = createToDoTask(1L, "ToDo Task Test", Status.TO_DO, TaskPriority.URGENT, "Harry up! It is URGENT!");
        toDoTask_2 = createToDoTask(1L, "ToDo Task Test", Status.TO_DO, TaskPriority.URGENT, "Harry up! It is URGENT!");
        toDoTaskDAO = createToDoTaskDAO(1L, "ToDo Task Test", Status.TO_DO, TaskPriority.URGENT, "Harry up! It is URGENT!");
        toDoTaskDAO_2 = createToDoTaskDAO(2L, "ToDo Task Test", Status.TO_DO, TaskPriority.URGENT, "Harry up! It is URGENT!");
        toDoTaskList = createToDoTaskList(toDoTask);
        toDoTaskDAOList = createToDoTaskDAOList(toDoTaskDAO);
    }

    @Test
    void getAllToDoTasks_Success() {
        when(repository.findAll()).thenReturn(toDoTaskDAOList);
        when(mapper.taskDaoToTaskModel(toDoTaskDAO)).thenReturn(toDoTask);
        List<ToDoTask> toDoTaskList = service.getAllToDoTasks();
        assertEquals(2, toDoTaskList.size());
        assertEquals(toDoTaskList.get(0).getId(), 1L);
        assertEquals(toDoTaskList.get(0).getTaskDescription(), "ToDo Task Test");
    }

    @Test
    void getAllToDoTasks_EmptyList() {
        when(repository.findAll()).thenReturn(new ArrayList<>());
        List<ToDoTask> toDoTaskList = service.getAllToDoTasks();
        assertEquals(0, toDoTaskList.size());
    }

    @Test
    void getToDoTaskById_Success() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(toDoTaskDAO));
        when(mapper.taskDaoToTaskModel(toDoTaskDAO)).thenReturn(toDoTask);
        Optional<ToDoTask> optionalToDoTask = service.getToDoTaskById(anyLong());
        optionalToDoTask.ifPresent(toDoTask -> assertEquals("ToDo Task Test", toDoTask.getTaskDescription()));
    }

    @Test
    void getToDoTaskById_Invalid() {
        when(repository.findById(any())).thenReturn(Optional.empty());
        Optional<ToDoTask> toDoTask = service.getToDoTaskById(any());
        Assertions.assertFalse(toDoTask.isPresent());
    }

    @Test
    void getToDoTasksByStatus_Success() {
        when(repository.findAll()).thenReturn(toDoTaskDAOList);
        when(mapper.taskDaoToTaskModel(toDoTaskDAO)).thenReturn(toDoTask);
        List<ToDoTask> toDoTaskListByStatus = service.getToDoTasksByStatus(Status.TO_DO);
        assertEquals(2, toDoTaskListByStatus.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void getToDoTasksByStatus_Invalid_EmptyList() {
        when(repository.findAll()).thenReturn(Collections.emptyList());
        assertTrue(service.getToDoTasksByStatus(Status.IN_PROGRESS).isEmpty());
        verify(repository, times(1)).findAll();
    }

    @Test
    void getToDoTasksByPriority_Success() {
        when(repository.findAll()).thenReturn(toDoTaskDAOList);
        when(mapper.taskDaoToTaskModel(toDoTaskDAO)).thenReturn(toDoTask);
        List<ToDoTask> toDoTaskListByPriority = service.getToDoTasksByPriority(TaskPriority.URGENT);
        assertEquals(2, toDoTaskListByPriority.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void getToDoTasksByPriority_Invalid_EmptyList() {
        when(repository.findAll()).thenReturn(Collections.emptyList());
        assertTrue(service.getToDoTasksByPriority(TaskPriority.LOW).isEmpty());
        verify(repository, times(1)).findAll();
    }

    @Test
    void saveToDoTask_Success() {
        when(repository.save(toDoTaskDAO)).thenReturn(toDoTaskDAO);
        when(mapper.taskDaoToTaskModel(toDoTaskDAO)).thenReturn(toDoTask);
        when(mapper.taskModelTOTaskDAO(toDoTask)).thenReturn(toDoTaskDAO);
        ToDoTask toDoTaskSaved = service.saveToDoTask(toDoTask);
        assertEquals("ToDo Task Test", toDoTaskSaved.getTaskDescription());

        verify(repository, times(1)).save(toDoTaskDAO);
    }

    @Test
    void saveToDoTask_Invalid_DuplicateTaskDescription() {
        when(repository.findAll()).thenReturn(toDoTaskDAOList);
        assertThrows(HttpClientErrorException.class, () -> service.saveToDoTask(toDoTask_2));

        verify(repository, times(0)).save(toDoTaskDAO);
    }

    @Test
    void updateToDoTask_Success() {
        when(repository.save(toDoTaskDAO)).thenReturn(toDoTaskDAO);
        when(mapper.taskDaoToTaskModel(toDoTaskDAO)).thenReturn(toDoTask);
        when(mapper.taskModelTOTaskDAO(toDoTask)).thenReturn(toDoTaskDAO);
        ToDoTask toDoTaskUpdated = service.updateToDoTask(toDoTask);
        assertEquals("ToDo Task Test", toDoTaskUpdated.getTaskDescription());

        verify(repository, times(1)).save(toDoTaskDAO);
    }

    @Test
    void updateToDoTask_Invalid() {
        when(repository.save(toDoTaskDAO)).thenThrow(new IllegalArgumentException());
        when(mapper.taskModelTOTaskDAO(toDoTask)).thenReturn(toDoTaskDAO);
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.updateToDoTask(toDoTask));
        verify(repository, times(1)).save(toDoTaskDAO);
    }


    @Test
    void deleteToDoTask_Success() {
        service.deleteToDoTask(anyLong());
        verify(repository, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteToDoTask_Invalid_IdIsNull() {
        service.deleteToDoTask(null);
        expectedException.expect(IllegalArgumentException.class);
    }

    @Test
    void deleteToDoTaskByPassingInId_Invalid_NumberFormatException() throws Exception {
        assertThrows(NumberFormatException.class,
                ()->{
                    when(service.getToDoTaskById(Long.valueOf("xyz"))).thenReturn(Optional.empty());
                });
    }

    @Test
    void deleteToDoTaskByPassingInId_Invalid_NumberFormatException2() throws Exception {
        assertThrows(NumberFormatException.class,
                ()->{
                    when(service.getToDoTaskById(Long.valueOf("!*@"))).thenReturn(Optional.empty());
                });
    }

    @Test
    void deleteToDoTaskByPassingInId_Invalid_NumberFormatException3() throws Exception {
        assertThrows(NumberFormatException.class,
                ()->{
                    when(service.getToDoTaskById(Long.valueOf(""))).thenReturn(Optional.empty());
                });
    }


    public ToDoTask createToDoTask(Long id, String taskDescription, Status status, TaskPriority taskPriority, String specialMessage) {
        ToDoTask toDoTask = new ToDoTask();
        toDoTask.setId(id);
        toDoTask.setTaskDescription(taskDescription);
        toDoTask.setStatus(status);
        toDoTask.setTaskPriority(taskPriority);
        toDoTask.setSpecialMessage(specialMessage);
        return toDoTask;
    }

    public ToDoTaskDAO createToDoTaskDAO(Long id, String taskDescription, Status status, TaskPriority taskPriority, String specialMessage) {
        ToDoTaskDAO toDoTaskDAO = new ToDoTaskDAO();
        toDoTaskDAO.setId(id);
        toDoTaskDAO.setTaskDescription(taskDescription);
        toDoTaskDAO.setStatus(status);
        toDoTaskDAO.setTaskPriority(taskPriority);
        toDoTaskDAO.setSpecialMessage(specialMessage);
        return toDoTaskDAO;
    }

    public List<ToDoTask> createToDoTaskList (ToDoTask toDoTask) {
        List<ToDoTask> toDoTaskList = new ArrayList<>();
        toDoTaskList.add(toDoTask);
        toDoTaskList.add(toDoTask);
        return toDoTaskList;
    }

    public List<ToDoTaskDAO> createToDoTaskDAOList (ToDoTaskDAO toDoTaskDAO) {
        List<ToDoTaskDAO> toDoTaskDAOList = new ArrayList<>();
        toDoTaskDAOList.add(toDoTaskDAO);
        toDoTaskDAOList.add(toDoTaskDAO);
        return toDoTaskDAOList;
    }
}