package com.squelette.squelette_backend.middleware;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@AllArgsConstructor
@RestControllerAdvice
public class ApiExceptionHandler {

    private static final String SWAGGER = "swagger";
    private static final String API_DOCS_PATTERN = "/v3/api-docs";

    private final GlobalExceptionHandler globalExceptionHandler;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exceptionHandler(HttpServletRequest request, Exception exception) throws Exception {
        String path = request.getRequestURI();
        if (path.contains(SWAGGER) || path.startsWith(API_DOCS_PATTERN))
            throw exception;

        return globalExceptionHandler.handle(exception);
    }
}