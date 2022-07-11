package com.cognixia.jump.ManageBookSeller.security.services.exceptions;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String message) {
        super(message);
    }
}