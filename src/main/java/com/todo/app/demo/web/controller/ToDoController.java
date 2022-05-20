package com.todo.app.demo.web.controller;

import com.todo.app.demo.business.service.impl.ToDoTaskServiceImpl;
import com.todo.app.demo.model.ToDoTask;
import com.todo.app.demo.swagger.HTMLResponseMessages;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static com.todo.app.demo.swagger.DescriptionVariables.MAX_LONG_RANGE;
import static com.todo.app.demo.swagger.DescriptionVariables.TODO_APP_MAIN;

@Api(tags = {TODO_APP_MAIN})
@RestController
@RequestMapping("/todoapp")
public class ToDoController {

    @Autowired
    ToDoTaskServiceImpl service;

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

    @ApiOperation(
            value = "Finds the ToDo Task by the id",
            notes = "Provide an id to search specific ToDo Task in database",
            response = ToDoTask.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/{id}")
    public ResponseEntity<ToDoTask> getToDoTaskById(
            @ApiParam(value = "Id of the ToDoTask", defaultValue = "1",
                    allowableValues = MAX_LONG_RANGE, required = true) @PathVariable("id") Long id) {
        Optional<ToDoTask> toDoTaskById = service.getToDoTaskById(id);
        if (!toDoTaskById.isPresent()) {
//            log.warn("ToDo Task with id {} is not found.", id);
            System.out.println("ToDo Task with id " + id + " is not found.");
        } else {
            System.out.println("ToDo Task with id " + id + " is found: " + toDoTaskById);
//            log.info("ToDo Task with id {} is found: {}", id, toDoTaskById);
        }
//        log.info("ToDo Task with id {} is found: {}", id, toDoTaskById);
        return toDoTaskById.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

}

