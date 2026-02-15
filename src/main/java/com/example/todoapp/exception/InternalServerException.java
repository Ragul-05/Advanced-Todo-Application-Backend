package com.example.todoapp.exception;

/**
 * Thrown when an unexpected server-side error occurs (maps to HTTP 500).
 */
public class InternalServerException extends RuntimeException {
    public InternalServerException() { super(); }
    public InternalServerException(String message) { super(message); }
    public InternalServerException(String message, Throwable cause) { super(message, cause); }
}
