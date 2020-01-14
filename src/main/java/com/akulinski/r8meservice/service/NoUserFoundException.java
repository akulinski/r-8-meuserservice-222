package com.akulinski.r8meservice.service;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NoUserFoundException extends RuntimeException {
    private String login;

    public NoUserFoundException(String message, String login) {
        super(message);
        this.login = login;
    }
}
