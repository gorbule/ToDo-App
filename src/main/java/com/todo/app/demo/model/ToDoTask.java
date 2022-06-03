package com.todo.app.demo.model;

import com.todo.app.demo.swagger.DescriptionVariables;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;

@ApiModel(description = "Model of ToDo Task")
@Data
@NoArgsConstructor
@Component
public class ToDoTask implements Serializable {

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
                    " 0 - TO_DO" +
                    " 1 - IN_PROGRESS" +
                    " 2 - DONE",
            required = true)
    @NonNull
    private Status status;

    @ApiModelProperty(
            notes = "ToDo Task Priority: " +
            " 0 - URGENT" +
            " 1 - HIGH" +
            " 2 - MEDIUM" +
            " 3 - LOW",
            required = true)
    @NonNull
    private TaskPriority taskPriority;
}
