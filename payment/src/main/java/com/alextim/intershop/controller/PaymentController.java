package com.alextim.intershop.controller;

import com.alextim.intershop.dto.PaymentRequest;
import com.alextim.intershop.dto.PaymentResponse;
import com.alextim.intershop.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

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
}
