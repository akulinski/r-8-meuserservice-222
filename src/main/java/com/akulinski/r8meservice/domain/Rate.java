package com.akulinski.r8meservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Rate.
 */
@Data
@NoArgsConstructor
public class Rate implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long poster;

    private Long receiver;

    private Double value;

    public Rate(Long posterId, Long receiverId, Double value) {
        this.poster = posterId;
        this.receiver = receiverId;
        this.value = value;
    }

    private Instant timeStamp = Instant.now();
}
