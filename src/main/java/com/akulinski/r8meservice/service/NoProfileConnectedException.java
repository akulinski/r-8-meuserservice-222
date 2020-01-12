package com.akulinski.r8meservice.service;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class NoProfileConnectedException extends RuntimeException {
    private long id;

    public NoProfileConnectedException(long id) {
        super(String.format("User %s has no profile", id));
        this.id = id;
    }
}
