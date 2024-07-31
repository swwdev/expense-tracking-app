package com.app.handler;

import com.app.dto.response.ExceptionResponse;
import com.app.exceptions.CantDeleteMainBillException;
import com.app.exceptions.CantObtainFingerPrintException;
import com.app.exceptions.FrozenBillException;
import com.app.exceptions.InvalidDataException;
import com.app.exceptions.InvalidMainBillException;
import com.app.exceptions.NotEnoughBalance;
import com.app.exceptions.UserNotFoundException;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleException(BadCredentialsException exception) {
        ExceptionResponse response = new ExceptionResponse(
                exception.getClass(),
                exception.getMessage()
        );
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler(CantObtainFingerPrintException.class)
    public ResponseEntity<ExceptionResponse> handleException(CantObtainFingerPrintException exception) {
        ExceptionResponse response = new ExceptionResponse(
                exception.getClass(),
                exception.getMessage()
        );
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleException(MethodArgumentNotValidException exception) {
        ExceptionResponse response = new ExceptionResponse(
                exception.getClass(),
                exception.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.joining(", ", "[", "]"))
        );
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(EntityNotFoundException exception) {
        ExceptionResponse response = new ExceptionResponse(
                exception.getClass(),
                exception.getMessage()
        );
        return new ResponseEntity<>(response, NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(UserNotFoundException exception) {
        ExceptionResponse response = new ExceptionResponse(
                exception.getClass(),
                exception.getMessage()
        );
        return new ResponseEntity<>(response, NOT_FOUND);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleException(UsernameNotFoundException exception) {
        ExceptionResponse response = new ExceptionResponse(
                exception.getClass(),
                exception.getMessage()
        );
        return new ResponseEntity<>(response, NOT_FOUND);
    }


    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ExceptionResponse> handleException(JwtException exception) {
        ExceptionResponse response = new ExceptionResponse(
                exception.getClass(),
                exception.getMessage()
        );
        return new ResponseEntity<>(response, FORBIDDEN);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleException(AccessDeniedException exception) {
        ExceptionResponse response = new ExceptionResponse(
                exception.getClass(),
                exception.getMessage()
        );
        return new ResponseEntity<>(response, FORBIDDEN);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handleException(AuthenticationException exception) {
        ExceptionResponse response = new ExceptionResponse(
                exception.getClass(),
                exception.getMessage()
        );
        return new ResponseEntity<>(response, UNAUTHORIZED);
    }

    @ExceptionHandler(NotEnoughBalance.class)
    public ResponseEntity<ExceptionResponse> handleException(NotEnoughBalance exception) {
        ExceptionResponse response = new ExceptionResponse(
                exception.getClass(),
                exception.getMessage()
        );
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler(FrozenBillException.class)
    public ResponseEntity<ExceptionResponse> handleException(FrozenBillException exception) {
        ExceptionResponse response = new ExceptionResponse(
                exception.getClass(),
                exception.getMessage()
        );
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler(CantDeleteMainBillException.class)
    public ResponseEntity<ExceptionResponse> handleException(CantDeleteMainBillException exception) {
        ExceptionResponse response = new ExceptionResponse(
                exception.getClass(),
                exception.getMessage()
        );
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler(DataSourceLookupFailureException.class)
    public ResponseEntity<ExceptionResponse> handleException(DataSourceLookupFailureException exception) {
        ExceptionResponse response = new ExceptionResponse(
                exception.getClass(),
                exception.getMessage()
        );
        return new ResponseEntity<>(response, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<ExceptionResponse> handleException(InvalidDataException exception) {
        ExceptionResponse response = new ExceptionResponse(
                exception.getClass(),
                exception.getMessage()
        );
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @ExceptionHandler(InvalidMainBillException.class)
    public ResponseEntity<ExceptionResponse> handleException(InvalidMainBillException exception) {
        ExceptionResponse response = new ExceptionResponse(
                exception.getClass(),
                exception.getMessage()
        );
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

}
