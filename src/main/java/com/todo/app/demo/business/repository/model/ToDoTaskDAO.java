package com.todo.app.demo.business.repository.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "todotask_table")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToDoTaskDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "task_description")
    private String taskDescription;

    @Column(name = "status")
    private Long status;

    @Column(name = "task_priority")
    private Long taskPriority;

    public ToDoTaskDAO(Long id) {
        this.id = id;
    }
}
