package com.exercise.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<?> taskNotFoundException(final TaskNotFoundException ex, final WebRequest req) {
        return new ResponseEntity<>("Task not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> illegalArgumentException(final IllegalArgumentException ex, final WebRequest req) {
        return new ResponseEntity<>("Illegal Argument", HttpStatus.BAD_REQUEST);
    };

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> ioException(final Exception ex, final WebRequest req) {
        return new ResponseEntity<>("File not found for given task.", HttpStatus.INTERNAL_SERVER_ERROR);
    }



}
