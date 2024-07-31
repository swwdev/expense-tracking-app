package com.app.exceptions;

public class NotEnoughBalance extends RuntimeException {
    public NotEnoughBalance(String string) {
        super(string);
    }
}
