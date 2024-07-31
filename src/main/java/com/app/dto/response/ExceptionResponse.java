package com.app.dto.response;

import lombok.Data;

@Data
public class ExceptionResponse {
    private final Class<? extends Exception> exceptionClass;
    private final String message;
}
