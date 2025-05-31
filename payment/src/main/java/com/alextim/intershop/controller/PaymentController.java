package com.alextim.intershop.controller;

import com.alextim.intershop.dto.PaymentRequest;
import com.alextim.intershop.dto.PaymentResponse;
import com.alextim.intershop.exception.InsufficientFundsException;
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

@RestController("/pay")
@RequiredArgsConstructor
@Slf4j
public class PaymentController implements PaymentsApi {

    private final AccountService accountService;

    @Override
    public Mono<ResponseEntity<PaymentResponse>> payPost(Mono<PaymentRequest> paymentRequest, ServerWebExchange exchange) {
        return paymentRequest.flatMap(request -> {
            log.info("incoming request for payment user with ID {} of amount {}",
                    request.getUserId(), request.getAmount());

            return accountService.payment(request.getUserId(), request.getAmount())
                    .map(account -> ResponseEntity.ok(
                            new PaymentResponse()
                                    .userId(request.getUserId())
                                    .newBalance(account.getBalance())
                                    .success(true)
                                    .message("Payment successful"))
                    );
        });
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<PaymentResponse> handleInsufficientFundsException(InsufficientFundsException e) {
        log.error("GlobalExceptionHandler", e);
        return ResponseEntity.badRequest().body(
                new PaymentResponse()
                        .userId(e.getUserId())
                        .newBalance(e.getCurBalance())
                        .success(false)
                        .message("Insufficient funds")
        );
    }

    @ExceptionHandler(NotFoundUserException.class)
    public ResponseEntity<PaymentResponse> handleNotFoundException(NotFoundUserException e) {
        log.error("GlobalExceptionHandler", e);
        return ResponseEntity.status(NOT_FOUND).body(
                new PaymentResponse()
                        .userId(e.getUserId())
                        .newBalance(null)
                        .success(false)
                        .message("Not found")
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<PaymentResponse> handleException(Exception e) {
        log.error("GlobalExceptionHandler", e);
        return ResponseEntity.internalServerError().body(
                new PaymentResponse()
                        .userId(null)
                        .newBalance(null)
                        .success(false)
                        .message("Internal server error")
        );
    }
}
