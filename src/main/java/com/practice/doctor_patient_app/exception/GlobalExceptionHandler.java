package com.practice.doctor_patient_app.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    record ApiError(Instant timestamp, int status, String error, String message, List<String> details, String path) {}

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        List<String> details = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage()).toList();
        ApiError err = new ApiError(Instant.now(), HttpStatus.BAD_REQUEST.value(), "Validation Error",
                "Invalid request", details, req.getRequestURI());
        return ResponseEntity.badRequest().body(err);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleBadRequest(IllegalArgumentException ex, HttpServletRequest req) {
        ApiError err = new ApiError(Instant.now(), HttpStatus.BAD_REQUEST.value(), "Bad Request",
                ex.getMessage(), List.of(), req.getRequestURI());
        return ResponseEntity.badRequest().body(err);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleForbidden(AccessDeniedException ex, HttpServletRequest req) {
        ApiError err = new ApiError(Instant.now(), HttpStatus.FORBIDDEN.value(), "Forbidden",
                ex.getMessage(), List.of(), req.getRequestURI());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(err);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAll(Exception ex, HttpServletRequest req) {
        ApiError err = new ApiError(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "Server Error",
                ex.getMessage(), List.of(), req.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
    }
}
