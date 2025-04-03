package com.alextim.intershop.exeption;

public class CurrentOrderAbsentException extends RuntimeException {

    public CurrentOrderAbsentException() {
        super("There is not current order");
    }
}
