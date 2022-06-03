package com.todo.app.demo.business.mapper;

import com.todo.app.demo.business.repository.model.ToDoTaskDAO;
import com.todo.app.demo.model.Status;
import com.todo.app.demo.model.TaskPriority;
import com.todo.app.demo.model.ToDoTask;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ToDoTaskMapperImpl.class)
class ToDoTaskMapperImplTest {

    @Autowired
    private ToDoTaskMapper mapper;

    @Test
    void taskModelTOTaskDAO_Invalid() {
//        when(mapper.taskModelTOTaskDAO(null)).thenReturn(null);
//        assertEquals(mapper.taskModelTOTaskDAO(null), null);
//        verify(mapper, times(0)).taskModelTOTaskDAO(null);
    }
    @Test
    void taskModelTOTaskDAO_Success() {
        ToDoTask toDoTask = new ToDoTask();
        toDoTask.setId(1L);
        toDoTask.setTaskDescription("ToDo Task Test");
        toDoTask.setStatus(Status.TO_DO);
        toDoTask.setTaskPriority(TaskPriority.LOW);

        ToDoTaskDAO toDoTaskDAO = mapper.taskModelTOTaskDAO(toDoTask);

        assertThat(toDoTaskDAO.getId()).isEqualTo(1L);
        assertThat(toDoTaskDAO.getTaskDescription()).isEqualTo("ToDo Task Test");
        assertThat(toDoTaskDAO.getStatus()).isEqualTo(Status.TO_DO);
        assertThat(toDoTaskDAO.getTaskPriority()).isEqualTo(TaskPriority.LOW);
    }

    @Test
    void taskDaoToTaskModel_Success() {
        ToDoTaskDAO toDoTaskDAO = new ToDoTaskDAO();
        toDoTaskDAO.setId(1L);
        toDoTaskDAO.setTaskDescription("ToDo Task Test");
        toDoTaskDAO.setStatus(Status.TO_DO);
        toDoTaskDAO.setTaskPriority(TaskPriority.LOW);

        ToDoTask toDoTask = mapper.taskDaoToTaskModel(toDoTaskDAO);

        assertThat(toDoTask.getId()).isEqualTo(1L);
        assertThat(toDoTask.getTaskDescription()).isEqualTo("ToDo Task Test");
        assertThat(toDoTask.getStatus()).isEqualTo(Status.TO_DO);
        assertThat(toDoTask.getTaskPriority()).isEqualTo(TaskPriority.LOW);
    }
}