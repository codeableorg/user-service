package com.ecommerce.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejo global de errores para la User API.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResponseStatusException.class)
  public Mono<ResponseEntity<Map<String, Object>>> handleResponseStatusException(
      ResponseStatusException ex, ServerHttpRequest request) {
    Map<String, Object> error = new HashMap<>();
    error.put("timestamp", Instant.now());
    error.put("status", ex.getStatusCode().value());
    error.put("error", ex.getStatusCode().toString());
    error.put("message", ex.getReason());
    error.put("path", request.getPath().value());

    return Mono.just(ResponseEntity
        .status(ex.getStatusCode())
        .body(error));
  }

  @ExceptionHandler(Exception.class)
  public Mono<ResponseEntity<Map<String, Object>>> handleException(
      Exception ex, ServerHttpRequest request) {
    Map<String, Object> error = new HashMap<>();
    error.put("timestamp", Instant.now());
    error.put("status", 500);
    error.put("error", "Internal Server Error");
    error.put("message", ex.getMessage());
    error.put("path", request.getPath().value());

    return Mono.just(ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(error));
  }
}
