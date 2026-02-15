package com.example.todoapp.exception;

/**
 * Thrown when the client sends a bad request (HTTP 400).
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException() { super(); }
    public BadRequestException(String message) { super(message); }
    public BadRequestException(String message, Throwable cause) { super(message, cause); }
}
