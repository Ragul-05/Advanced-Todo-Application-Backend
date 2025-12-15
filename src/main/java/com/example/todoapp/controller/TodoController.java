package com.example.todoapp.controller;

import com.example.todoapp.dto.TodoRequest;
import com.example.todoapp.dto.TodoResponse;
import com.example.todoapp.model.User;
import com.example.todoapp.service.TodoService;
import com.example.todoapp.util.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    /* =========================
       CREATE TODO
       POST /todos
       ========================= */
    @PostMapping
    public ResponseEntity<ApiResponse<TodoResponse>> createTodo(
            @RequestBody TodoRequest request,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        TodoResponse response = todoService.createTodo(request, user);

        return ResponseEntity.ok(
                ApiResponse.success("Todo created successfully", response)
        );
    }

    /* =========================
       GET ALL TODOS
       GET /todos
       ========================= */
    @GetMapping
    public ResponseEntity<ApiResponse<List<TodoResponse>>> getAllTodos(
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        List<TodoResponse> todos = todoService.getTodos(user);

        return ResponseEntity.ok(
                ApiResponse.success("Todos fetched successfully", todos)
        );
    }

    /* =========================
       GET TODO BY ID
       GET /todos/{id}
       ========================= */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TodoResponse>> getTodoById(
            @PathVariable Long id,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        TodoResponse todo = todoService.getTodoById(id, user);

        return ResponseEntity.ok(
                ApiResponse.success("Todo fetched successfully", todo)
        );
    }

    /* =========================
       UPDATE TODO
       PUT /todos/{id}
       ========================= */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TodoResponse>> updateTodo(
            @PathVariable Long id,
            @RequestBody TodoRequest request,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        TodoResponse updated = todoService.updateTodo(id, request, user);

        return ResponseEntity.ok(
                ApiResponse.success("Todo updated successfully", updated)
        );
    }

    /* =========================
       DELETE TODO
       DELETE /todos/{id}
       ========================= */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTodo(
            @PathVariable Long id,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        todoService.deleteTodo(id, user);

        return ResponseEntity.ok(
                ApiResponse.success("Todo deleted successfully", null)
        );
    }

    /* =========================
       MARK AS COMPLETED
       PATCH /todos/{id}/complete
       ========================= */
    @PatchMapping("/{id}/complete")
    public ResponseEntity<ApiResponse<TodoResponse>> completeTodo(
            @PathVariable Long id,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        TodoResponse completed = todoService.markAsCompleted(id, user);

        return ResponseEntity.ok(
                ApiResponse.success("Todo marked as completed", completed)
        );
    }

    /* =========================
       GET COMPLETED TODOS
       GET /todos/completed
       ========================= */
    @GetMapping("/completed")
    public ResponseEntity<ApiResponse<List<TodoResponse>>> getCompletedTodos(
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        List<TodoResponse> todos = todoService.getCompletedTodos(user);

        return ResponseEntity.ok(
                ApiResponse.success("Completed todos fetched", todos)
        );
    }

    /* =========================
       GET TODOS BY CATEGORY
       GET /todos/category/{category}
       ========================= */
    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<TodoResponse>>> getByCategory(
            @PathVariable String category,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        List<TodoResponse> todos = todoService.getTodosByCategory(category, user);

        return ResponseEntity.ok(
                ApiResponse.success("Todos fetched by category", todos)
        );
    }

    /* =========================
       GET TODOS BY PRIORITY
       GET /todos/priority/{priority}
       ========================= */
    @GetMapping("/priority/{priority}")
    public ResponseEntity<ApiResponse<List<TodoResponse>>> getByPriority(
            @PathVariable String priority,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        List<TodoResponse> todos = todoService.getTodosByPriority(priority, user);

        return ResponseEntity.ok(
                ApiResponse.success("Todos fetched by priority", todos)
        );
    }
}
