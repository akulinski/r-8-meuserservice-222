package com.akulinski.r8meservice.service;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class NoBoardForProfileProfileException extends RuntimeException {
    private final long profileId;

    public NoBoardForProfileProfileException(String message, long profileId) {
        super(message);
        this.profileId = profileId;
    }
}
