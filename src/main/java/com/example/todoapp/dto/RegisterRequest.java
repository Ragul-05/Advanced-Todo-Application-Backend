package com.example.todoapp.dto;

public class RegisterRequest {
    private String fullName;
    private String email;
    private String password;
    private String confirmPassword;

    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getConfirmPassword() { return confirmPassword; }
}
