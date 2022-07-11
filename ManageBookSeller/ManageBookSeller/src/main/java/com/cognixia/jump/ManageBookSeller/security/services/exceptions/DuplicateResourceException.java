package com.cognixia.jump.ManageBookSeller.security.services.exceptions;

public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}