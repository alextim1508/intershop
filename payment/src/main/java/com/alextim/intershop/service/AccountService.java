package com.alextim.intershop.service;

import com.alextim.intershop.entity.Account;
import reactor.core.publisher.Mono;

public interface AccountService {
    Mono<Account> save(Account account);

    Mono<Double> getBalanceByUserId(long userId);

    Mono<Account> payment(long userId, double amount);
}
