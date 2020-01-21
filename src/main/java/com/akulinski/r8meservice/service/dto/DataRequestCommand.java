package com.akulinski.r8meservice.service.dto;


import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@AllArgsConstructor
public final class DataRequestCommand implements Serializable {
    public final String questionId;

    public final Instant timestamp;
}


