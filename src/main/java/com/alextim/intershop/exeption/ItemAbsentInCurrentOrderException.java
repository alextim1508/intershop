package com.alextim.intershop.exeption;

public class ItemAbsentInCurrentOrderException extends RuntimeException {

    public ItemAbsentInCurrentOrderException(long id) {
        super("Item with " + id + "absent in current order");
    }
}
