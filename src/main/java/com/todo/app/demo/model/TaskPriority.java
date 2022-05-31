package com.todo.app.demo.model;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "Enum class for TaskPriority field in Model class - ToDoTask.java")
public enum TaskPriority {
    URGENT,
    HIGH,
    MEDIUM,
    LOW
}
