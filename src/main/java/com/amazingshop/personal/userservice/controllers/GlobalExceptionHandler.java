package com.amazingshop.personal.userservice.controllers;

import com.amazingshop.personal.userservice.util.exceptions.ErrorResponse;
import com.amazingshop.personal.userservice.util.exceptions.PersonNotFoundException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handlerExceptions(MethodArgumentNotValidException e){
        Map<String, String> errors = e.getBindingResult().getFieldErrors().stream().
                collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> Optional.ofNullable(fieldError.getDefaultMessage())
                                .orElse("Validation error occurred!")
                ));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handlerValidationException(BadCredentialsException e){
        return new ResponseEntity<>(ErrorResponse.makeErrorResponse(e.getMessage()),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(PersonNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerPersonNotFoundException(PersonNotFoundException e){
        return new ResponseEntity<>(ErrorResponse.makeErrorResponse("Person with this name wasn't found!"),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<ErrorResponse> handlerJWTVerificationException(JWTVerificationException e) {
        return new ResponseEntity<>(ErrorResponse.makeErrorResponse("Invalid JWT token!"),
        HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerUsernameNotFoundException(UsernameNotFoundException e) {
        return new ResponseEntity<>(ErrorResponse.makeErrorResponse("User not found!"),
        HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handlerNullPointerException(NullPointerException e) {
        return new ResponseEntity<>(ErrorResponse.makeErrorResponse("Internal server error: Null pointer exception!"),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handlerException(Exception e) {
        return new ResponseEntity<>(ErrorResponse.makeErrorResponse("An unexpected error occurred!"),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
