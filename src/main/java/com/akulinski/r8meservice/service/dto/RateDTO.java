package com.akulinski.r8meservice.service.dto;
import lombok.Data;

import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.akulinski.r8meservice.domain.Rate} entity.
 */
@Data
public class RateDTO implements Serializable {

    private Long id;

    private Double value;

    private Instant timeStamp;

    private String rated;

    private String rating;

    private String questionId;
}
