package com.todo.app.demo.model;

import io.swagger.annotations.ApiModel;
import lombok.Getter;

/**
 * Enum class for Status field.
 */
@ApiModel(description = "Enum class for Status field in Model class - ToDoTask.java")
@Getter
public enum Status {
    TO_DO,
    IN_PROGRESS,
    DONE
}
