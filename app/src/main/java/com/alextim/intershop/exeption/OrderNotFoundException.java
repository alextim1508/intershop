package com.alextim.intershop.exeption;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(long userId, long orderId) {
        super(String.format("Order with %d ID is not exist or not available for user with %d ID ",
                orderId, userId));
    }

    public OrderNotFoundException(long orderId) {
        super(String.format("Order with %d ID is not exist", orderId));
    }
}
