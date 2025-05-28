package com.alextim.intershop.exeption;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({OrderNotFoundException.class, ItemNotFoundException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleNotFoundException(Exception e) {
        log.error("GlobalExceptionHandler", e);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleException(Exception e) {
        log.error("GlobalExceptionHandler", e);
    }
}
