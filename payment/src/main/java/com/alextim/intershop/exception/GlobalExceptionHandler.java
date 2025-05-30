package com.alextim.intershop.exception;

import com.alextim.intershop.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({NotFoundUserException.class})
    public ResponseEntity<ErrorResponse> handleNotFoundException(Exception e) {
        log.error("GlobalExceptionHandler", e);
        return ResponseEntity.status(NOT_FOUND).body(new ErrorResponse().error(e.getMessage()));
    }

    @ExceptionHandler({InsufficientFundsException.class})
    public ResponseEntity<ErrorResponse> handleInsufficientFundsException(Exception e) {
        log.error("GlobalExceptionHandler", e);
        return ResponseEntity.badRequest().body(new ErrorResponse().error(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("GlobalExceptionHandler", e);
        return ResponseEntity.internalServerError().body(new ErrorResponse().error(e.getMessage()));
    }
}