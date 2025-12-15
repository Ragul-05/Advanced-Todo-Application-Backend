package com.example.todoapp.controller;

import com.example.todoapp.dto.*;
import com.example.todoapp.model.User;   // ✅ CORRECT USER IMPORT
import com.example.todoapp.service.AuthService;
import com.example.todoapp.util.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<User>> register(
            @RequestBody RegisterRequest request) {

        User savedUser = authService.register(request); // ✅ NO CAST

        return ResponseEntity.ok(
                ApiResponse.success("User registered successfully", savedUser)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @RequestBody LoginRequest request) {

        AuthResponse authResponse = authService.login(request);

        return ResponseEntity.ok(
                ApiResponse.success("Login successful", authResponse)
        );
    }
}
