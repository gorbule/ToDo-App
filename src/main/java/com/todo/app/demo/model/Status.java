package com.todo.app.demo.model;

import io.swagger.annotations.ApiModel;

/**
 * Enum class for Status field.
 */
@ApiModel(description = "Enum class for Status field in Model class - ToDoTask.java")
public enum Status {
    TO_DO,
    IN_PROGRESS,
    DONE
}
