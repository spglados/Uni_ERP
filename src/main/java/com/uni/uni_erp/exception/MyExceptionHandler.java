package com.uni.uni_erp.exception;

import com.uni.uni_erp.exception.errors.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// RuntimeException이 발생하면 해당 파일로 오류가 모인다
@RestControllerAdvice // C.A --> 뷰 에러 페이지, R.C.A 데이터 반환 (에러)
public class MyExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(MyExceptionHandler.class);

    @ExceptionHandler(Exception400.class)
    public ResponseEntity<ApiUtil<String>> handleException400(Exception400 e) {
        logger.error("Exception400 발생: {}", e.getMessage(), e);
        ApiUtil<String> apiUtil = new ApiUtil<>(400, e.getMessage());
        return new ResponseEntity<>(apiUtil, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception401.class)
    public ResponseEntity<ApiUtil<String>> handleException401(Exception401 e) {
        logger.error("Exception401 발생: {}", e.getMessage(), e);
        ApiUtil<String> apiUtil = new ApiUtil<>(401, e.getMessage());
        return new ResponseEntity<>(apiUtil, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception403.class)
    public ResponseEntity<ApiUtil<String>> handleException403(Exception403 e) {
        logger.error("Exception403 발생: {}", e.getMessage(), e);
        ApiUtil<String> apiUtil = new ApiUtil<>(403, e.getMessage());
        return new ResponseEntity<>(apiUtil, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception404.class)
    public ResponseEntity<ApiUtil<String>> handleException404(Exception404 e) {
        logger.error("Exception404 발생: {}", e.getMessage(), e);
        ApiUtil<String> apiUtil = new ApiUtil<>(404, e.getMessage());
        return new ResponseEntity<>(apiUtil, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception500.class)
    public ResponseEntity<ApiUtil<String>> handleException500(Exception500 e) {
        logger.error("Exception500 발생: {}", e.getMessage(), e);
        ApiUtil<String> apiUtil = new ApiUtil<>(500, e.getMessage());
        return new ResponseEntity<>(apiUtil, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 추가: HttpMediaTypeNotAcceptableException 핸들러
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<ApiUtil<String>> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException e) {
        logger.warn("HttpMediaTypeNotAcceptableException 발생: {}", e.getMessage(), e);
        ApiUtil<String> apiUtil = new ApiUtil<>(406, "Media type not acceptable: " + e.getMessage());
        return new ResponseEntity<>(apiUtil, HttpStatus.NOT_ACCEPTABLE);
    }

    // 추가: 일반적인 예외 핸들러
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiUtil<String>> handleGeneralException(Exception e) {
        logger.error("예상치 못한 예외 발생: {}", e.getMessage(), e);
        ApiUtil<String> apiUtil = new ApiUtil<>(500, "Internal server error: " + e.getMessage());
        return new ResponseEntity<>(apiUtil, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
