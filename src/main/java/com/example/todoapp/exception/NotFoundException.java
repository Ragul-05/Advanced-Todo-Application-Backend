package com.example.todoapp.exception;

/**
 * Thrown when a requested entity is not found (HTTP 404).
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException() { super(); }
    public NotFoundException(String message) { super(message); }
    public NotFoundException(String message, Throwable cause) { super(message, cause); }
}
