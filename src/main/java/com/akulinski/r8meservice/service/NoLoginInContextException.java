package com.akulinski.r8meservice.service;

import lombok.Data;

@Data
public class NoLoginInContextException extends RuntimeException{
    public NoLoginInContextException() {
        super();
    }

    public NoLoginInContextException(String message) {
        super(message);
    }
}
