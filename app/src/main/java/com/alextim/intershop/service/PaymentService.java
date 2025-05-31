package com.alextim.intershop.service;

import com.alextim.intershop.client.pay.dto.BalanceResponse;
import com.alextim.intershop.client.pay.dto.PaymentResponse;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface PaymentService {
    Mono<ResponseEntity<BalanceResponse>> getBalance();

    Mono<ResponseEntity<PaymentResponse>> payment(double amount);
}
