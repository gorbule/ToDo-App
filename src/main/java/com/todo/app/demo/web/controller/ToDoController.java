package com.todo.app.demo.web.controller;

import com.todo.app.demo.business.service.ToDoTaskService;
import com.todo.app.demo.model.ToDoTask;
import com.todo.app.demo.swagger.HTMLResponseMessages;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.todo.app.demo.swagger.DescriptionVariables.TODO_APP_MAIN;

@Api(tags = {TODO_APP_MAIN})
@RestController
@RequestMapping("/todoapp")
public class ToDoController {

    @Autowired
    ToDoTaskService service;


    @ApiOperation(
            value = "Finds all ToDo Tasks",
            notes = "Returns the entire list of ToDo Tasks",
            response = ToDoTask.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/")
    public ResponseEntity<List<ToDoTask>> getAllToDoTasks() {
        List<ToDoTask> toDoTaskList = service.getAllToDoTasks();
        if (toDoTaskList.isEmpty()) {
//            log.info("ToDo Tasks list is empty");
            System.out.println("ToDo Tasks list is empty");
            return ResponseEntity.notFound().build();
        }
//        log.info("ToDo Tasks list is found. Size = {}", toDoTaskList::size);
        System.out.println("ToDo Tasks list is found. Size = " + toDoTaskList.size());
        return ResponseEntity.ok().body(toDoTaskList);
    }
}
