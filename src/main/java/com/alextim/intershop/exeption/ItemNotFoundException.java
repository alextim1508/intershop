package com.alextim.intershop.exeption;

public class ItemNotFoundException extends RuntimeException {

    public ItemNotFoundException(long id) {
        super("Item with " + id + " is not exist");
    }
}
