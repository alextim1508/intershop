package com.alextim.intershop.exeption;

public class ItemNotFoundException extends RuntimeException {

    public ItemNotFoundException(long id) {
        super(String.format("Item with %d ID is not exist", id));
    }
}
