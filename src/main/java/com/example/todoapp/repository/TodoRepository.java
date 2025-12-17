package com.example.todoapp.repository;

import com.example.todoapp.model.Todo;
import com.example.todoapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    List<Todo> findByUser(User user);

    Optional<Todo> findByIdAndUser(Long id, User user);

    List<Todo> findByUserAndCompletedTrue(User user);

    List<Todo> findByUserAndCompletedFalse(User user);

    List<Todo> findByUserAndCategory(User user, String category);

    List<Todo> findByUserAndPriority(User user, String priority);

    long countByUser(User user); // 🔑 FOR displayId LOGIC
}
