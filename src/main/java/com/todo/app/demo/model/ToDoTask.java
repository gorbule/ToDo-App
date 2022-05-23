package com.todo.app.demo.model;

import com.todo.app.demo.swagger.DescriptionVariables;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

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
    @NonNull
    private String taskDescription;

    @ApiModelProperty(
            notes = "ToDo Task Status. Status can be: " +
                    "1 (done) or 0 (not completed)",
            required = true)
    @NonNull
    @NotEmpty(message = "Description could not be empty. Fill in some information.")
    private Long status;

    @ApiModelProperty(
            notes = "ToDo Task Priority: " +
            " 1 - Important Not Urgent" +
            " 2 - Not Important Not Urgent" +
            " 3 - Important and Urgent" +
            " 4 - Not Important Urgent",
            required = true)
    @NonNull
    private Long taskPriority;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(Long taskPriority) {
        this.taskPriority = taskPriority;
    }
}
