package com.alextim.intershop;

import com.alextim.intershop.client.pay.dto.BalanceResponse;
import com.alextim.intershop.client.pay.dto.PaymentResponse;
import com.alextim.intershop.service.PaymentService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

@TestConfiguration
public class PaymentServiceConfiguration {

    @Bean
    public PaymentService paymentService() {
        return new PaymentService() {
            @Override
            public Mono<ResponseEntity<BalanceResponse>> getBalance() {
                return Mono.just(ResponseEntity.ok(
                        new BalanceResponse()
                                .userId(1L)
                                .balance(10000.0))
                );
            }

            @Override
            public Mono<ResponseEntity<PaymentResponse>> payment(double amount) {
                return Mono.just(ResponseEntity.ok(
                        new PaymentResponse()
                                .userId(1L)
                                .newBalance(10000.0)
                                .message("success")
                                .success(true))
                );
            }
        };
    }

}
