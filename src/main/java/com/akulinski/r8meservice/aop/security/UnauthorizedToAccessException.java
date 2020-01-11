package com.akulinski.r8meservice.aop.security;

public class UnauthorizedToAccessException extends RuntimeException {
    public UnauthorizedToAccessException() {
    }

    public UnauthorizedToAccessException(String message) {
        super(message);
    }
}
