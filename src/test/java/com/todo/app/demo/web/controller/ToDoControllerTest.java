package com.todo.app.demo.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.app.demo.business.service.impl.ToDoTaskServiceImpl;
import com.todo.app.demo.model.Status;
import com.todo.app.demo.model.TaskPriority;
import com.todo.app.demo.model.ToDoTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ToDoController.class)
@DisplayName("Test Case for ToDoController class methods")
class ToDoControllerTest {

    public static String baseUrl = "/todoapp/";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ToDoController controller;

    @MockBean
    private ToDoTaskServiceImpl service;

    @MockBean
    KieSession kieSession;

    private ToDoTask toDoTask;
    private List<ToDoTask> toDoTaskList;


    @BeforeEach
    public void beforeEach(){
        toDoTask= createToDoTask(1L,"ToDo Task Test", Status.TO_DO,TaskPriority.URGENT,"Harry up! It is URGENT!");
        toDoTaskList = createToDoTaskList();
    }


    @Test
    void getAllToDoTasks_Success() throws Exception {
        when(service.getAllToDoTasks()).thenReturn(toDoTaskList);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(baseUrl)
                        .content(asJsonString(toDoTaskList)))
                        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                        .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                        .andExpect(MockMvcResultMatchers.jsonPath("$[0].taskDescription").value("ToDo Task Test 1"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$[1].taskDescription").value("ToDo Task Test 2"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$[1].status").value("IN_PROGRESS"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$[0].taskPriority").value("HIGH"))
                        .andExpect(status().isOk());

        verify(service, times(1)).getAllToDoTasks();
    }

    @Test
    void getAllToDoTasks_Invalid_ListIsEmpty() throws Exception {
        toDoTaskList.clear();
        when(service.getAllToDoTasks()).thenReturn(toDoTaskList);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(baseUrl)
                        .content(asJsonString(toDoTaskList))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());

        verify(service, times(1)).getAllToDoTasks();
    }

    @Test
    void getToDoTaskById() throws Exception {
        when(service.getToDoTaskById(1L)).thenReturn(Optional.of(toDoTask));

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(baseUrl + 1L))
                        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.taskDescription").value("ToDo Task Test"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("TO_DO"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.taskPriority").value("URGENT"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.specialMessage").value("Harry up! It is URGENT!"))
                        .andExpect(status().isOk());

        verify(service, times(1)).getToDoTaskById(1L);
    }

    @Test
    void getToDoTaskById_Invalid_NotFoundId() throws Exception {
        when(service.getToDoTaskById(3L)).thenReturn(Optional.empty());

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(baseUrl + 3L)
                        .content(asJsonString(Optional.empty()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());

        verify(service, times(1)).getToDoTaskById(3L);
    }

    @Test
    void getToDoTasksByStatus_Success() throws Exception {
        when(service.getToDoTasksByStatus(Status.IN_PROGRESS)).thenReturn(toDoTaskList);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(baseUrl + "filteredList/v1/" + Status.valueOf("IN_PROGRESS")))
                        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2L))
                        .andExpect(MockMvcResultMatchers.jsonPath("$[1].taskDescription").value("ToDo Task Test 2"))
                        .andExpect(status().isOk());

        verify(service, times(1)).getToDoTasksByStatus(Status.IN_PROGRESS);
    }

    @Test
    void getToDoTasksByStatus_Invalid_EmptyList() throws Exception {
        when(service.getToDoTasksByStatus(Status.DONE)).thenReturn(Collections.emptyList());

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(baseUrl + "filteredList/v1/" + Status.valueOf("DONE"))
                        .content(asJsonString(Collections.emptyList()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());

        verify(service, times(1)).getToDoTasksByStatus(Status.DONE);
    }

    @Test
    void getToDoTasksByPriority_Success() throws Exception {
        when(service.getToDoTasksByPriority(TaskPriority.LOW)).thenReturn(toDoTaskList);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(baseUrl + "filteredList/v2/" + TaskPriority.valueOf("LOW")))
                        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2L))
                        .andExpect(MockMvcResultMatchers.jsonPath("$[1].taskDescription").value("ToDo Task Test 2"))
                        .andExpect(status().isOk());

        verify(service, times(1)).getToDoTasksByPriority(TaskPriority.LOW);
    }

    @Test
    void getToDoTasksByPriority_Invalid_EmptyList() throws Exception {
        when(service.getToDoTasksByPriority(TaskPriority.URGENT)).thenReturn(Collections.emptyList());

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(baseUrl + "filteredList/v2/" + TaskPriority.valueOf("URGENT"))
                        .content(asJsonString(Collections.emptyList()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());

        verify(service, times(1)).getToDoTasksByPriority(TaskPriority.URGENT);
    }

    @Test
    void saveToDoTask_Success() throws Exception {
        ResultActions mvcResult = mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(toDoTask))
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated());

        verify(service, times(1)).saveToDoTask(toDoTask);
    }

    @Test
    void saveToDoTask_Invalid_IncorrectId() throws Exception{
        toDoTask.setId(-1L);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(toDoTask))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(service, times(0)).saveToDoTask(toDoTask);
    }

    @Test
    void saveToDoTask_Invalid_EmptyTaskDescriptionField() throws Exception {
        toDoTask.setTaskDescription("");
        ResultActions mvcResult = mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(toDoTask))
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());

        verify(service, times(0)).saveToDoTask(toDoTask);
    }

    @Test
    void saveToDoTask_Invalid_StatusIsNull() throws Exception {
        assertThrows(NullPointerException.class,
                ()->{
                    toDoTask.setStatus(null);
                });
        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());

        verify(service, times(0)).saveToDoTask(toDoTask);
    }

    @Test
    void saveToDoTask_Invalid_TaskPriorityIsNull() throws Exception {
        assertThrows(NullPointerException.class,
                ()->{
                    toDoTask.setTaskPriority(null);
                });
        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());

        verify(service, times(0)).saveToDoTask(toDoTask);
    }


    @Test
    void deleteToDoTaskByPassingInId_Success() throws Exception {
        when(service.getToDoTaskById(anyLong())).thenReturn(Optional.of(toDoTask));
        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .delete(baseUrl + "/" + anyLong())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNoContent());

        verify(service, times(1)).deleteToDoTask(anyLong());
    }

    @Test
    void deleteToDoTaskByPassingInId_Invalid_IdNotFound() throws Exception {
        when(service.getToDoTaskById(anyLong())).thenReturn(Optional.empty());
        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .delete(baseUrl + "/" + anyLong())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());

        verify(service, times(0)).deleteToDoTask(anyLong());
    }

    @Test
    void deleteToDoTaskByPassingInId_Invalid_IdIsNull() throws Exception {
        toDoTask.setId(0L);
        when(service.getToDoTaskById(0L)).thenReturn(Optional.empty());

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .delete(baseUrl + 0L)
                        .content(asJsonString(toDoTask))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().is4xxClientError());
        verify(service, times(0)).deleteToDoTask(0L);
    }

    @Test
    void updateToDoTask_Success() throws Exception {
        when(service.getToDoTaskById(toDoTask.getId())).thenReturn(Optional.of(toDoTask));

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(baseUrl + "/1")
                        .content(asJsonString(toDoTask))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.taskDescription").value("ToDo Task Test"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("TO_DO"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.taskPriority").value("URGENT"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.specialMessage").value("Harry up! It is URGENT!"))
                        .andExpect(status().isCreated());

        verify(service, times(1)).updateToDoTask(toDoTask);
    }

    @Test
    void updateToDoTask_Invalid_IdNotFound() throws Exception {
        Long notFoundId = 3L;

        when(service.getToDoTaskById(3L)).thenReturn(Optional.empty());
        assertThat(String.valueOf(!notFoundId.equals(toDoTask.getId())), true);

        ResultActions mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(baseUrl + notFoundId)
                        .content(asJsonString(Optional.empty()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());

        verify(service, times(0)).updateToDoTask(toDoTask);
    }

    @Test
    void updateToDoTask_Invalid_EmptyTaskDescriptionField() throws Exception {
        toDoTask.setTaskDescription("");
        ResultActions mvcResult = mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(toDoTask))
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest());

        verify(service, times(0)).updateToDoTask(toDoTask);
    }

    public ToDoTask createToDoTask(Long id, String taskDescription, Status status, TaskPriority taskPriority,
                                   String specialMessage) {
        ToDoTask toDoTask = new ToDoTask();
        toDoTask.setId(id);
        toDoTask.setTaskDescription(taskDescription);
        toDoTask.setStatus(status);
        toDoTask.setTaskPriority(taskPriority);
        toDoTask.setSpecialMessage(specialMessage);
        return toDoTask;
    }

    public List<ToDoTask> createToDoTaskList() {
        List<ToDoTask> toDoTaskList = new ArrayList<>();
        toDoTaskList.add(createToDoTask(1L, "ToDo Task Test 1", Status.TO_DO,
                TaskPriority.HIGH, "Harry up! HIGH priority task!"));
        toDoTaskList.add(createToDoTask(2L, "ToDo Task Test 2", Status.IN_PROGRESS,
                TaskPriority.LOW, "You already started this task! Don't forget to complete!"));
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