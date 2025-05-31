package com.alextim.intershop.controller;

import com.alextim.intershop.dto.BalanceResponse;
import com.alextim.intershop.exception.NotFoundUserException;
import com.alextim.intershop.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController("/balance")
@RequiredArgsConstructor
@Slf4j
public class BalanceController implements BalanceApi {

    private final AccountService accountService;

    @Override
    public Mono<ResponseEntity<BalanceResponse>> balanceGet(Long userId, ServerWebExchange exchange) {
        log.info("incoming request for getting user with ID {} balance", userId);
        return accountService.getBalanceByUserId(userId)
                .map(balance -> ResponseEntity.ok(
                        new BalanceResponse()
                                .userId(userId)
                                .balance(balance))
                );
    }

    @ExceptionHandler(NotFoundUserException.class)
    public ResponseEntity<BalanceResponse> handleNotFoundException(NotFoundUserException e) {
        log.error("GlobalExceptionHandler", e);
        return ResponseEntity.status(NOT_FOUND).body(
                new BalanceResponse()
                        .userId(e.getUserId())
                        .balance(null)
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BalanceResponse> handleException(Exception e) {
        log.error("GlobalExceptionHandler", e);
        return ResponseEntity.internalServerError().body(
                new BalanceResponse()
                        .userId(null)
                        .balance(null)
        );
    }
}
