package com.alextim.intershop.service;

import com.alextim.intershop.client.pay.api.BalanceApi;
import com.alextim.intershop.client.pay.api.PaymentsApi;
import com.alextim.intershop.client.pay.dto.BalanceResponse;
import com.alextim.intershop.client.pay.dto.PaymentRequest;
import com.alextim.intershop.client.pay.dto.PaymentResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private static final Long USER_ID_STUB = 1L;

    private BalanceApi balanceApi;
    private PaymentsApi paymentsApi;

    @PostConstruct
    public void init() {
        balanceApi = new BalanceApi();
        paymentsApi = new PaymentsApi();
    }

    @Override
    public Mono<ResponseEntity<BalanceResponse>> getBalance() {
        log.info("Get balance for user with ID {}", USER_ID_STUB);

        return balanceApi.balanceGetWithHttpInfo(USER_ID_STUB)
                .doOnSuccess(response ->
                        log.info("Get user with {} balance response {} (code: {})",
                                USER_ID_STUB, response.getBody(), response.getStatusCode()));
    }

    @Override
    public Mono<ResponseEntity<PaymentResponse>> payment(double amount) {
        log.info("Payment for user with ID {} of amount {}", USER_ID_STUB, amount);

        return paymentsApi.payPostWithHttpInfo(new PaymentRequest().userId(USER_ID_STUB).amount(amount))
                .doOnNext(response ->
                        log.info("Get user with {} payment response {} (code: {})",
                            USER_ID_STUB,  response.getBody(), response.getStatusCode()));
    }
}
