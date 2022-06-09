package com.todo.app.demo.web.controller;

import com.todo.app.demo.business.service.impl.ToDoTaskServiceImpl;
import com.todo.app.demo.model.Status;
import com.todo.app.demo.model.TaskPriority;
import com.todo.app.demo.model.ToDoTask;
import com.todo.app.demo.swagger.DescriptionVariables;
import com.todo.app.demo.swagger.HTMLResponseMessages;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.kie.api.runtime.KieSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

import static com.todo.app.demo.swagger.DescriptionVariables.MAX_LONG_RANGE;
import static com.todo.app.demo.swagger.DescriptionVariables.TODO_APP_CONTROLLER;

@Api(tags = {TODO_APP_CONTROLLER})
@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/todoapp")
public class ToDoController {

    private final ToDoTaskServiceImpl service;

    private final KieSession kieSession;

    @ApiOperation(
            value = "Finds all ToDo Tasks",
            notes = "Returns the entire list of ToDo Tasks",
            response = ToDoTask.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/")
    public ResponseEntity<List<ToDoTask>> getAllToDoTasks() {
        List<ToDoTask> toDoTaskList = service.getAllToDoTasks();
        if (toDoTaskList.isEmpty()) {
            log.info("ToDo Tasks list is empty");
            return ResponseEntity.notFound().build();
        }
        log.info("ToDo Tasks list is found. Size = {}", toDoTaskList::size);
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
            @ApiParam(value = "ToDo Task id", defaultValue = "1",
                    allowableValues = MAX_LONG_RANGE, required = true) @NotNull @PathVariable("id") Long id) {
        Optional<ToDoTask> toDoTaskById = service.getToDoTaskById(id);
        if (toDoTaskById.isPresent()) {
            log.info("ToDo Task with id {} is found: {}", id, toDoTaskById);
            return ResponseEntity.ok(toDoTaskById.get());
        }
        log.warn("ToDo Task with id {} is not found.", id);
        return ResponseEntity.notFound().build();
    }

    @ApiOperation(
            value = "Finds the ToDo Task by specified status",
            notes = "Provide a task status to search specific ToDo Task in database",
            response = ToDoTask.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/filteredList/v1/{status}")
    public ResponseEntity<List<ToDoTask>> getToDoTasksByStatus(
            @ApiParam(value = "ToDo Task status", required = true)
            @PathVariable("status") Status status) {
        List<ToDoTask> toDoTasksListByStatus = service.getToDoTasksByStatus(status);
        if (toDoTasksListByStatus.isEmpty()) {
            log.info("ToDo Tasks with status {} not found. List is empty", status);
            return ResponseEntity.notFound().build();
        }
        log.info("ToDo Tasks with status {} is found. Size = {}", status, toDoTasksListByStatus.size());
        return ResponseEntity.ok().body(toDoTasksListByStatus);
    }

    @ApiOperation(
            value = "Finds the ToDo Task by specified status",
            notes = "Provide a task status to search specific ToDo Task in database",
            response = ToDoTask.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/filteredList/v2/{taskPriority}")
    public ResponseEntity<List<ToDoTask>> getToDoTasksByPriority(
            @ApiParam(value = "ToDo Task status", required = true)
            @PathVariable("taskPriority") TaskPriority taskPriority) {
        List<ToDoTask> toDoTasksListByPriority = service.getToDoTasksByPriority(taskPriority);
        if (toDoTasksListByPriority.isEmpty()) {
            log.info("ToDo Tasks with priority level {} not found. List is empty", taskPriority);
            return ResponseEntity.notFound().build();
        }
        log.info("ToDo Tasks with status {} is found. Size = {}", taskPriority, toDoTasksListByPriority.size());
        return ResponseEntity.ok().body(toDoTasksListByPriority);
    }

    @PostMapping()
    @ApiOperation(value = "Saves ToDo Task in DB",
            notes = "If provided valid ToDO Task description, saves it",
            response = ToDoTask.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ToDoTask> saveToDoTask(
            @Valid @RequestBody ToDoTask toDoTask, BindingResult bindingResult) {
        log.info("Save new Project Hiring Status with id: {}", toDoTask.getId());
        if (bindingResult.hasErrors() || toDoTask.getTaskDescription().isEmpty()) {
            log.error("The ToDo Task has error or required data is missing: {}", bindingResult);
            return ResponseEntity.badRequest().build();
        }
        kieSession.insert(toDoTask);
        kieSession.fireAllRules();
        ToDoTask toDoTaskSaved = service.saveToDoTask(toDoTask);
        log.info("New ToDo Task is created: {}", toDoTaskSaved);
        return new ResponseEntity<>(toDoTaskSaved, HttpStatus.CREATED);
    }

    @ApiOperation(
            value = "Deletes the project role with its employee list",
            response = ToDoTask.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = HTMLResponseMessages.HTTP_204_WITH_DATA),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public ResponseEntity<ToDoTask> deleteToDoTask(
            @ApiParam(value = DescriptionVariables.NON_NULL_MAX_LONG, defaultValue = "1",
                    allowableValues = DescriptionVariables.MAX_LONG_RANGE, required = true)
            @NonNull @PathVariable Long id) {
        log.info("Delete ToDo Task by passing id, where id is:{}", id);

        Optional<ToDoTask> toDoTask = service.getToDoTaskById(id);
        if (!(toDoTask.isPresent())) {
            log.warn("ToDo Task for delete with id {} is not found.", id);
            return ResponseEntity.notFound().build();
        }
        service.deleteToDoTask(id);
        log.debug("ToDo Task with id {} is deleted: {}", id, toDoTask);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Updates the ToDo Task by id",
            notes = "Updates the ToDo Task if id exists",
            response = ToDoTask.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = HTMLResponseMessages.HTTP_201),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ToDoTask> updateToDoTask(@ApiParam(value = "ToDo Task id", example = "1", required = true)
                                                   @PathVariable @NonNull Long id,
                                                   @Valid @RequestBody ToDoTask toDoTask,
                                                   BindingResult bindingResult) {
        log.info("Update existing ToDo Task with id: {} and new body: {}", id, toDoTask);
        if (bindingResult.hasErrors() || !id.equals(toDoTask.getId())) {
            log.warn("ToDo Task for update with id {} not found. " +
                    "Check Input. Maybe missed required parameters or parameters are not valid.", id);
            return ResponseEntity.notFound().build();
        }
        service.updateToDoTask(toDoTask);
        kieSession.insert(toDoTask);
        kieSession.fireAllRules();
        log.debug("ToDo Task with id {} is updated: {}", id, toDoTask);
        return new ResponseEntity<>(toDoTask, HttpStatus.CREATED);
    }
}

