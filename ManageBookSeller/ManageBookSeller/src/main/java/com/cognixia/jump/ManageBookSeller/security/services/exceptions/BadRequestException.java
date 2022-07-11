package com.cognixia.jump.ManageBookSeller.security.services.exceptions;
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}