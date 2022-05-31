package com.todo.app.demo.model;

import com.todo.app.demo.swagger.DescriptionVariables;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel(description = "Model of ToDo Task")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToDoTask {

    @ApiModelProperty(
            notes = "ToDo Task id. Number to identify object inside of collection. Range [1, MAX_LONG]",
            required = true, example = "1")
    @Min(value = 1, message = DescriptionVariables.MODEL_ID_MIN)
    @Max(value = Long.MAX_VALUE, message = DescriptionVariables.MODEL_ID_MAX)
    private Long id;

    @ApiModelProperty(
            notes = "ToDo Task description",
            required = true)
    @NotBlank
    @NotNull(message = "Task description field cannot be null")
    private String taskDescription;

    @ApiModelProperty(
            notes = "ToDo Task Status.",
            dataType = "String",
            allowableValues = "TO_DO, IN_PROGRESS, DONE",
            required = true)
    @NotNull(message = "Task status field cannot be null")
    private Status status;

    @ApiModelProperty(
            notes = "ToDo Task Priority.",
            dataType = "String",
            allowableValues = "URGENT, HIGH, MEDIUM, LOW",
            required = true)
    @NotNull(message = "Task priority field cannot be null")
    private TaskPriority taskPriority;
}
