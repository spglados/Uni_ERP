package com.uni.uni_erp.exception;

import com.uni.uni_erp.exception.errorsRest.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class UniRestExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(UniRestExceptionHandler.class);

    @ExceptionHandler(RestException400.class)
    public ResponseEntity<?> handleException400(RestException400 ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());
        logger.error("RestException400 발생: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    @ExceptionHandler(RestException401.class)
    public ResponseEntity<?> handleException401(RestException401 ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());
        logger.error("RestException401 발생: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
    @ExceptionHandler(RestException403.class)
    public ResponseEntity<?> handleException403(RestException403 ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());
        logger.error("RestException403 발생: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(RestException404.class)
    public ResponseEntity<?> handleException404(RestException404 ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());
        logger.error("RestException404 발생: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    @ExceptionHandler(RestException500.class)
    public ResponseEntity<?> handleException500(RestException500 ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());
        logger.error("RestException500 발생: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}
