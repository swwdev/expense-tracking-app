package com.app.exceptions;

public class CantObtainFingerPrintException extends RuntimeException {
    public CantObtainFingerPrintException(String invalidFingerPrint) {
        super(invalidFingerPrint);
    }
}
