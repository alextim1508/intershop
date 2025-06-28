package com.alextim.intershop.controller;

import com.alextim.intershop.AbstractControllerTestContainer;
import com.alextim.intershop.dto.BalanceResponse;
import com.alextim.intershop.entity.Account;
import com.alextim.intershop.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.http.HttpStatus.*;

public class BalanceControllerTest extends AbstractControllerTestContainer {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private AccountService accountService;

    @Test
    public void getBalance_shouldGetBalanceTest() {
        double balance = 100.0;

        Account account = new Account(1L, balance);

        Account savedAccount = accountService.save(account).block();

        webTestClient.mutateWith(getMockJwt())
                .get()
                .uri("/balance?userId={userId}", savedAccount.getId())
                .exchange()
                .expectStatus().isEqualTo(OK)
                .expectBody(BalanceResponse.class)
                .value(response -> {
                    assert response.getUserId().equals(savedAccount.getId());
                    assert response.getBalance().equals(balance);
                });
    }

    @Test
    public void getBalance_shouldReturnUnauthorizedDTest() {
        Long userId = 1L;

        webTestClient
                .get()
                .uri("/balance?userId={userId}", userId)
                .exchange()
                .expectStatus().isEqualTo(UNAUTHORIZED);
    }
}
