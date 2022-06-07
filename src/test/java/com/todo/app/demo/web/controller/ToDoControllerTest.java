package com.todo.app.demo.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.app.demo.business.service.impl.ToDoTaskServiceImpl;
import com.todo.app.demo.model.Status;
import com.todo.app.demo.model.TaskPriority;
import com.todo.app.demo.model.ToDoTask;
import org.junit.jupiter.api.Test;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ToDoController.class)
class ToDoControllerTest {

    public static String baseUrl = "/todoapp";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ToDoTaskServiceImpl service;

    @MockBean
    KieSession kieSession;

    @Autowired
    private ToDoController controller;

    @Test
    void getAllToDoTasks_Success() throws Exception {
        List<ToDoTask> toDoTaskList = createToDoTaskList();
        when(service.getAllToDoTasks()).thenReturn(toDoTaskList);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(baseUrl + "/")
                        .content(asJsonString(toDoTaskList)))
                        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                        .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                        .andExpect(MockMvcResultMatchers.jsonPath("$[1].taskDescription").value("ToDo Task Test 2"))
                        .andExpect(status().isOk());

        verify(service, times(1)).getAllToDoTasks();
    }

    @Test
    void getAllToDoTasks_Invalid_ListIsEmpty() throws Exception {
        List<ToDoTask> toDoTaskList = new ArrayList<>();
        when(service.getAllToDoTasks()).thenReturn(toDoTaskList);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(baseUrl + "/")
                        .content(asJsonString(toDoTaskList))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());

        verify(service, times(1)).getAllToDoTasks();
    }

    @Test
    void getToDoTaskById() throws Exception {
        when(service.getToDoTaskById(anyLong())).thenReturn(Optional.of(toDoTask()));

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(baseUrl + "/1"))
                        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.taskDescription").value("ToDo Task Test"))
                        .andExpect(status().isOk());

        verify(service, times(1)).getToDoTaskById(anyLong());
    }

    @Test
    void getToDoTaskById_Invalid_NotFoundId() throws Exception {
        List<ToDoTask> listWithEExistingTasks = createToDoTaskList();
        when(service.getToDoTaskById(3L)).thenReturn(Optional.empty());

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(baseUrl + "/" + 3L)
                        .content(asJsonString(Optional.empty()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());

        verify(service, times(1)).getToDoTaskById(3L);
    }

    @Test
    void postToDoTask_Success() throws Exception {
        ToDoTask newToDoTask = toDoTask();
        ResultActions mvcResult = mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(newToDoTask))
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated());

        verify(service, times(1)).postToDoTask(newToDoTask);
    }

    @Test
    void postToDoTask_Invalid() throws Exception {
        ToDoTask newToDoTask = toDoTask();
        newToDoTask.setTaskDescription("");
        ResultActions mvcResult = mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(newToDoTask))
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());

        verify(service, times(0)).postToDoTask(newToDoTask);
    }

    @Test
    void deleteToDoTaskByPassingInId_Success() throws Exception {
        when(service.getToDoTaskById(anyLong())).thenReturn(Optional.of(toDoTask()));
        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .delete(baseUrl + "/" + anyLong())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNoContent());
    }

    @Test
    void deleteToDoTaskByPassingInId_Invalid_IdNotFound() throws Exception {
        when(service.getToDoTaskById(anyLong())).thenReturn(Optional.empty());
        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .delete(baseUrl + "/" + anyLong())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
    }

    @Test
    void updateToDoTask_Success() throws Exception {
        ToDoTask updatedToDoTask = toDoTask();
        when(service.getToDoTaskById(updatedToDoTask.getId())).thenReturn(Optional.of(updatedToDoTask));

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(baseUrl + "/1")
                        .content(asJsonString(updatedToDoTask))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.taskDescription").value("ToDo Task Test"))
                .andExpect(status().isCreated());

        verify(service, times(1)).updateToDoTask(updatedToDoTask);
    }

    @Test
    void updateToDoTask_Invalid_IdNotFound() throws Exception {
        ToDoTask updatedToDoTask = toDoTask();
        Long notFoundId = 2L;

        when(service.getToDoTaskById(2L)).thenReturn(Optional.empty());
        assertThat(String.valueOf(!notFoundId.equals(updatedToDoTask.getId())), true);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(baseUrl + "/" + notFoundId)
                        .content(asJsonString(Optional.empty()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());

        verify(service, times(0)).updateToDoTask(updatedToDoTask);
    }

    public ToDoTask toDoTask() {
        ToDoTask toDoTask = new ToDoTask();
        toDoTask.setId(1L);
        toDoTask.setTaskDescription("ToDo Task Test");
        toDoTask.setStatus(Status.TO_DO);
        toDoTask.setTaskPriority(TaskPriority.MEDIUM);
        return toDoTask;
    }

    public ToDoTask createToDoTask(Long id, String taskDescription, Status status, TaskPriority taskPriority) {
        ToDoTask toDoTask = new ToDoTask();
        toDoTask.setId(id);
        toDoTask.setTaskDescription(taskDescription);
        toDoTask.setStatus(status);
        toDoTask.setTaskPriority(taskPriority);
        return toDoTask;
    }

    public List<ToDoTask> createToDoTaskList() {
        List<ToDoTask> toDoTaskList = new ArrayList<>();
        toDoTaskList.add(createToDoTask(1L, "ToDo Task Test 1", Status.TO_DO, TaskPriority.HIGH));
        toDoTaskList.add(createToDoTask(2L, "ToDo Task Test 2", Status.IN_PROGRESS, TaskPriority.LOW));
        return toDoTaskList;
    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}