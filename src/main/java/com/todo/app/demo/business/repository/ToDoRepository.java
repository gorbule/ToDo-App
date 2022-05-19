package com.todo.app.demo.business.repository;

import com.todo.app.demo.business.repository.model.ToDoTaskDAO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToDoRepository extends JpaRepository<ToDoTaskDAO, Long> {
}
