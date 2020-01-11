package com.akulinski.r8meservice.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Rate.
 */
@Data
@NoArgsConstructor
public class Rate implements Serializable, ProtectedResource {

    private static final long serialVersionUID = 1L;

    private Long poster;

    private Long receiver;

    private Double value;

    private String question;

    public Rate(Long posterId, Long receiverId, Double value) {
        this.poster = posterId;
        this.receiver = receiverId;
        this.value = value;
    }

    private Instant timeStamp = Instant.now();

    @Override
    public long getOwner() {
        return this.poster;
    }
}
