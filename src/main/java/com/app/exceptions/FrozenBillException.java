package com.app.exceptions;

public class FrozenBillException extends RuntimeException {
    public FrozenBillException(String message) {
        super(message);
    }
}
