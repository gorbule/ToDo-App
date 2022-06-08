package com.todo.app.demo.business.mapper;

import com.todo.app.demo.business.repository.model.ToDoTaskDAO;
import com.todo.app.demo.model.Status;
import com.todo.app.demo.model.TaskPriority;
import com.todo.app.demo.model.ToDoTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test Case for ToDoTaskMapperImpl class methods")
class ToDoTaskMapperImplTest {

    private ToDoTaskMapperImpl mapper;
    private ToDoTask toDoTask;
    private ToDoTaskDAO toDoTaskDAO;

    @BeforeEach
    public void setUp(){
        mapper = new ToDoTaskMapperImpl();
        toDoTask = createToDoTask(1L, "ToDo Task Test", Status.TO_DO, TaskPriority.URGENT, "Harry up! It is URGENT!");
        toDoTaskDAO = createToDoTaskDAO(1L, "ToDo Task Test", Status.TO_DO, TaskPriority.URGENT, "Harry up! It is URGENT!");
    }

    @Test
    void taskModelTOTaskDAO_Success() {
        ToDoTaskDAO toDoTaskDAO = mapper.taskModelTOTaskDAO(toDoTask);

        assertThat(toDoTaskDAO.getId()).isEqualTo(1L);
        assertThat(toDoTaskDAO.getTaskDescription()).isEqualTo("ToDo Task Test");
        assertThat(toDoTaskDAO.getStatus()).isEqualTo(Status.TO_DO);
        assertThat(toDoTaskDAO.getTaskPriority()).isEqualTo(TaskPriority.URGENT);
        assertThat(toDoTaskDAO.getSpecialMessage()).isEqualTo("Harry up! It is URGENT!");
    }

    @Test
    void taskModelTOTaskDAO_Invalid() {
        ToDoTask toDoTask = null;
        ToDoTaskDAO toDoTaskDAO = mapper.taskModelTOTaskDAO(null);
        assertNull(null);
    }

    @Test
    void taskDaoToTaskModel_Success() {
        ToDoTask toDoTask = mapper.taskDaoToTaskModel(toDoTaskDAO);

        assertThat(toDoTask.getId()).isEqualTo(1L);
        assertThat(toDoTask.getTaskDescription()).isEqualTo("ToDo Task Test");
        assertThat(toDoTask.getStatus()).isEqualTo(Status.TO_DO);
        assertThat(toDoTask.getTaskPriority()).isEqualTo(TaskPriority.URGENT);
        assertThat(toDoTask.getSpecialMessage()).isEqualTo("Harry up! It is URGENT!");
    }

    @Test
    void taskDaoToTaskModel_Invalid() {
        ToDoTaskDAO toDoTaskDAO = null;
        ToDoTask toDoTask = mapper.taskDaoToTaskModel(null);
        assertNull(null);
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
}