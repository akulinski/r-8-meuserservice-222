package com.akulinski.r8meservice.service;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class NoProfileConnectedException extends RuntimeException {
    private long id;

    private String username;

    public NoProfileConnectedException(long id) {
        super(String.format("User %s has no profile", id));
        this.id = id;
    }

    public NoProfileConnectedException(String username) {
        super(String.format("User %s has no profile", username));
        this.username = username;
    }
}
