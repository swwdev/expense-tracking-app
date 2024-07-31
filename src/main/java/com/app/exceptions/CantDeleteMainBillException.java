package com.app.exceptions;

public class CantDeleteMainBillException extends RuntimeException {
    public CantDeleteMainBillException(String string) {
        super(string);
    }
}
