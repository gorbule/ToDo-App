package com.todo.app.demo.business.repository.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "todotask_table")
@Data
public class ToDoTaskDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "taskDescription")
    private String taskDescription;

    @Column(name = "status")
    private Long status;

    @Column(name = "taskPriority")
    private Long taskPriority;

    public ToDoTaskDAO() {
    }

    public ToDoTaskDAO(Long id, String taskDescription, Long status, Long taskPriority) {
        this.id = id;
        this.taskDescription = taskDescription;
        this.status = status;
        this.taskPriority = taskPriority;
    }

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
