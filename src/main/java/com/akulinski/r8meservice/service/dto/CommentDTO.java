package com.akulinski.r8meservice.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.akulinski.r8meservice.domain.Comment} entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO implements Serializable, Comparable {

    private String id;

    private String comment;

    private Instant timeStamp;

    private String receiver;

    private String poster;

    private String imageUrl;

    @Override
    public int compareTo(@NotNull Object o) {
        return this.getTimeStamp().compareTo(((CommentDTO) o).getTimeStamp());
    }
}
