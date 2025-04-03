package com.alextim.intershop.exeption;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(long id) {
        super("Order with " + id + " is not exist");
    }
}
