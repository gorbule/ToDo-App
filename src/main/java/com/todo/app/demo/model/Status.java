package com.todo.app.demo.model;

import io.swagger.annotations.ApiModel;
import lombok.Getter;

@ApiModel(description = "Enum class for Status field in Model class - ToDoTask.java")
@Getter
public enum Status {
    TO_DO,
    IN_PROGRESS,
    DONE

}
