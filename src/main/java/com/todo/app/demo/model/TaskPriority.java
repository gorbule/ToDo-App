package com.todo.app.demo.model;

import io.swagger.annotations.ApiModel;
import lombok.Getter;

/**
 * Enum class for TaskPriority field.
 */
@ApiModel(description = "Enum class for TaskPriority field in Model class - ToDoTask.java")
@Getter
public enum TaskPriority {
    URGENT,
    HIGH,
    MEDIUM,
    LOW
}


