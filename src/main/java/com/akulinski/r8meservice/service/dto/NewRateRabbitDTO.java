package com.akulinski.r8meservice.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Data
@NoArgsConstructor
public class NewRateRabbitDTO implements Serializable {
    private String questionId;
    private Long posterId;
    private Double value;
    private Instant timestamp;
}
