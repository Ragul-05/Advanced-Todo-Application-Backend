package com.example.todoapp.service;

import com.example.todoapp.dto.TodoRequest;
import com.example.todoapp.dto.TodoResponse;
import com.example.todoapp.model.Todo;
import com.example.todoapp.model.User;
import com.example.todoapp.repository.TodoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    /* =========================
       CREATE TODO
       ========================= */
    public TodoResponse createTodo(TodoRequest request, User user) {

        long userTodoCount = todoRepository.countByUser(user);

        Todo todo = new Todo();
        todo.setDisplayId((int) userTodoCount + 1); // ⭐ DISPLAY ID LOGIC
        todo.setTitle(request.getTitle());
        todo.setDescription(request.getDescription());
        todo.setDueDate(request.getDueDate());
        todo.setDueTime(request.getDueTime());
        todo.setCategory(request.getCategory());
        todo.setPriority(request.getPriority());
        todo.setCompleted(false);
        todo.setUser(user);

        return mapToResponse(todoRepository.save(todo));
    }

    /* =========================
       GET ALL TODOS
       ========================= */
    public List<TodoResponse> getTodos(User user) {
        return todoRepository.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /* =========================
       GET TODO BY ID
       ========================= */
    public TodoResponse getTodoById(Long id, User user) {
        return mapToResponse(findUserTodo(id, user));
    }

    /* =========================
       UPDATE TODO
       ========================= */
    public TodoResponse updateTodo(Long id, TodoRequest request, User user) {

        Todo todo = findUserTodo(id, user);

        todo.setTitle(request.getTitle());
        todo.setDescription(request.getDescription());
        todo.setDueDate(request.getDueDate());
        todo.setDueTime(request.getDueTime());
        todo.setCategory(request.getCategory());
        todo.setPriority(request.getPriority());

        return mapToResponse(todoRepository.save(todo));
    }

    /* =========================
       DELETE TODO
       ========================= */
    public void deleteTodo(Long id, User user) {
        todoRepository.delete(findUserTodo(id, user));
    }

    /* =========================
       MARK AS COMPLETED
       ========================= */
    public TodoResponse markAsCompleted(Long id, User user) {

        Todo todo = findUserTodo(id, user);
        todo.setCompleted(true);

        return mapToResponse(todoRepository.save(todo));
    }

    /* =========================
   MARK AS INCOMPLETED
   ========================= */
    public TodoResponse markAsIncomplete(Long id, User user) {

        Todo todo = findUserTodo(id, user);
        todo.setCompleted(false);

        return mapToResponse(todoRepository.save(todo));
    }

    /* =========================
       GET COMPLETED TODOS
       ========================= */
    public List<TodoResponse> getCompletedTodos(User user) {
        return todoRepository.findByUserAndCompletedTrue(user)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /* =========================
      GET INCOMPLETED TODOS
      ========================= */
    public List<TodoResponse> getIncompleteTodos(User user) {

        return todoRepository.findByUserAndCompletedFalse(user)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    /* =========================
       GET TODOS BY CATEGORY
       ========================= */
    public List<TodoResponse> getTodosByCategory(String category, User user) {
        return todoRepository.findByUserAndCategory(user, category)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /* =========================
       GET TODOS BY PRIORITY
       ========================= */
    public List<TodoResponse> getTodosByPriority(String priority, User user) {
        return todoRepository.findByUserAndPriority(user, priority)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /* =========================
       PRIVATE METHODS
       ========================= */

    private Todo findUserTodo(Long id, User user) {
        return todoRepository.findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new RuntimeException("Todo not found or access denied"));
    }

    private TodoResponse mapToResponse(Todo todo) {

        TodoResponse res = new TodoResponse();
        res.setId(todo.getId());               // DB ID
        res.setDisplayId(todo.getDisplayId()); // UI ID
        res.setTitle(todo.getTitle());
        res.setDescription(todo.getDescription());
        res.setDueDate(todo.getDueDate());
        res.setDueTime(todo.getDueTime());
        res.setCategory(todo.getCategory());
        res.setPriority(todo.getPriority());
        res.setCompleted(todo.isCompleted());

        return res;
    }
}
