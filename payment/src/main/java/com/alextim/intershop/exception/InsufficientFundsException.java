package com.alextim.intershop.exception;

import lombok.Getter;

@Getter
public class InsufficientFundsException extends RuntimeException{

    private final long userId;
    private final double curBalance;
    private final double amount;

    public InsufficientFundsException(long userId, double curBalance, double amount) {
        super(String.format(
                "Insufficient funds on the user's balance with ID %d (current balance: %f) to payment %f",
                userId, curBalance, amount));
        this.userId = userId;
        this.curBalance = curBalance;
        this.amount = amount;
    }
}
