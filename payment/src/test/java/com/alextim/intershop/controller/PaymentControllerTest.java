package com.alextim.intershop.controller;

import com.alextim.intershop.AbstractControllerTestContainer;
import com.alextim.intershop.dto.PaymentRequest;
import com.alextim.intershop.dto.PaymentResponse;
import com.alextim.intershop.entity.Account;
import com.alextim.intershop.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.http.HttpStatus.*;

public class PaymentControllerTest extends AbstractControllerTestContainer {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private AccountService accountService;


    @Test
    public void pay_shouldPayTest() {
        double balance = 1000.0;

        Account account = accountService.save(new Account(1L, balance)).block();

        double amount = 200.0;

        PaymentRequest request = new PaymentRequest()
                .userId(account.getUserId())
                .amount(amount);

        webTestClient.mutateWith(getMockJwt())
                .post()
                .uri("/pay")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(OK)
                .expectBody(PaymentResponse.class)
                .value(response -> {
                    assert response.getUserId().equals(account.getUserId());
                    assert response.getNewBalance().equals(account.getBalance() - amount);
                    assert response.getSuccess();
                    assert "Payment successful".equals(response.getMessage());
                });
    }

    @Test
    public void pay_shouldReturnUnauthorizedDTest() {
        PaymentRequest request = new PaymentRequest()
                .userId(1L)
                .amount(200.0);

        webTestClient
                .post()
                .uri("/pay")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(UNAUTHORIZED);
    }

    @Test
    public void pay_shouldReturnInsufficientFundsTest() {
        double balance = 1000.0;

        Account account = new Account(1L, balance);

        Account savedAccount = accountService.save(account).block();

        double amount = balance + 200.0;

        PaymentRequest request = new PaymentRequest()
                .userId(account.getUserId())
                .amount(amount);

        webTestClient
                .mutateWith(getMockJwt()).post()
                .uri("/pay")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(BAD_REQUEST)
                .expectBody(PaymentResponse.class)
                .value(response -> {
                    assert response.getUserId().equals(savedAccount.getUserId());
                    assert response.getNewBalance().equals(savedAccount.getBalance());
                    assert !response.getSuccess();
                    assert "Insufficient funds".equals(response.getMessage());
                });
    }
}
