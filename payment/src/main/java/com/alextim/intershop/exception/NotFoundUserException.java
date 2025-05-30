package com.alextim.intershop.exception;

import lombok.Getter;

@Getter
public class NotFoundUserException extends RuntimeException{

    private final long userId;

    public NotFoundUserException(long userId) {
        super(String.format("User with ID %d is not found", userId));
        this.userId = userId;
    }
}
