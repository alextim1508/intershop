package com.alextim.intershop;

import com.alextim.intershop.client.pay.dto.BalanceResponse;
import com.alextim.intershop.client.pay.dto.PaymentResponse;
import com.alextim.intershop.service.PaymentService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

@TestConfiguration
public class PaymentServiceConfiguration {

    @Primary
    @Bean
    public PaymentService paymentService() {
        return new PaymentService() {
            @Override
            public Mono<ResponseEntity<BalanceResponse>> getBalance(long userId) {
                return Mono.just(ResponseEntity.ok(
                        new BalanceResponse()
                                .userId(userId)
                                .balance(10000.0))
                );
            }

            @Override
            public Mono<ResponseEntity<PaymentResponse>> payment(long userId, double amount) {
                return Mono.just(ResponseEntity.ok(
                        new PaymentResponse()
                                .userId(userId)
                                .newBalance(10000.0)
                                .message("success")
                                .success(true))
                );
            }
        };
    }

}
