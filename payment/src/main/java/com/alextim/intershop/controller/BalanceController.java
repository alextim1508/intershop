package com.alextim.intershop.controller;

import com.alextim.intershop.dto.BalanceRequest;
import com.alextim.intershop.dto.BalanceResponse;
import com.alextim.intershop.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController("/balance")
@RequiredArgsConstructor
@Slf4j
public class BalanceController implements BalanceApi {

    private final AccountService accountService;

    @Override
    public Mono<ResponseEntity<BalanceResponse>> balanceGet(Mono<BalanceRequest> balanceRequest, ServerWebExchange exchange) {
        return balanceRequest.flatMap(request -> {
            log.info("incoming request for getting user with ID {} balance", request.getUserId());
            return accountService.getBalanceByUserId(request.getUserId())
                    .map(balance ->
                            ResponseEntity.ok(new BalanceResponse().userId(request.getUserId()).balance(balance)));
        });
    }
}
