package com.alextim.intershop.service;

import com.alextim.intershop.entity.Account;
import com.alextim.intershop.exception.InsufficientFundsException;
import com.alextim.intershop.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Value("${app.start-balance}")
    private Double startBalance;

    @Override
    public Mono<Account> save(Account account) {
        return accountRepository.save(account)
                .doOnNext(savedAccount -> log.info("Account {} is saved", savedAccount));
    }

    @Override
    public Mono<Double> getBalanceByUserId(long userId) {
        return accountRepository.findByUserId(userId)
                .switchIfEmpty(Mono.defer(() -> accountRepository.save(new Account(userId, startBalance))))
                .doOnSuccess(account -> {
                    if (account == null) {
                        log.error("User with ID {} is not found", userId);
                    } else {
                        log.info("User with ID {} is found", userId);
                    }
                })
                .map(Account::getBalance);
    }

    @Override
    public Mono<Account> payment(long userId, double amount) {
        return accountRepository.findByUserId(userId)
                .switchIfEmpty(Mono.defer(() -> accountRepository.save(new Account(userId, startBalance))))
                .doOnSuccess(account -> {
                    if (account == null) {
                        log.error("User with ID {} is not found", userId);
                    } else {
                        log.info("User with ID {} is found", userId);
                    }
                })
                .flatMap(account -> {
                    if (account.getBalance() < amount) {
                        return Mono.error(new InsufficientFundsException(userId, account.getBalance(), amount));
                    }
                    account.setBalance(account.getBalance() - amount);
                    return accountRepository.save(account);
                });
    }
}
