package com.weyland.synthetichumancorestarter.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public final class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(final MethodArgumentNotValidException e) {
        final Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", "Validation failed");
        final List<String> errors = e.getBindingResult().getFieldErrors().stream().map(ex -> String.format("%s: %s", ex.getField(), ex.getDefaultMessage())).toList();
        body.put("details", errors);

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(final ConstraintViolationException e) {
        final Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", "Constraint violation");
        final List<String> errors = e.getConstraintViolations().stream().map(ex -> String.format("%s: %s", ex.getPropertyPath(), ex.getMessage())).toList();
        body.put("details", errors);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(CommandQueueFullException.class)
    public ResponseEntity<Map<String, Object>> handleCommandQueue(final CommandQueueFullException e) {
        final Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", "Command Queue Exception");
        body.put("message", e.getMessage());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(UnknownCommandPriorityException.class)
    public ResponseEntity<Map<String, Object>> handleCommandPriority(final UnknownCommandPriorityException e) {
        final Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", "Command Priority Exception");
        body.put("message", e.getMessage());
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleOtherRuntimeExceptions(final RuntimeException e) {
        final Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", "Internal error");
        body.put("message", e.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
