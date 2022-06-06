package com.todo.app.demo.business.repository.model;

import com.todo.app.demo.model.Status;
import com.todo.app.demo.model.TaskPriority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ToDoTaskDAO entity class. Represents ToDoTaskDAO object in data base.
 * Table name in data base "todotask_table".
 * Consist of id (column "id"), taskDescription (column "task_description"),
 * status (column "status"), taskPriority (column "task_priority").
 */
@Entity
@Table(name = "todotask_table")
@Data
@NoArgsConstructor
public class ToDoTaskDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "task_description")
    private String taskDescription;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "task_priority")
    @Enumerated(EnumType.STRING)
    private TaskPriority taskPriority;
}
