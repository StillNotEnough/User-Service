package com.amazingshop.personal.userservice.util.exceptions;

public class PersonNotFoundException extends RuntimeException {
    public PersonNotFoundException(String message) {
        super(message);
    }
}
