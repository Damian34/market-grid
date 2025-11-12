package com.damian.marketgrid.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdviser {

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<Map<String, String>> handleGlobalException(GlobalException ex) {
        log.error("GlobalException occurred: {}", ex.getMessage(), ex);
        Map<String, String> error = Map.of("error", ex.getMessage());
        return ResponseEntity.status(ex.getCode()).body(error);
    }
}
