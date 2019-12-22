package com.akulinski.r8meservice.service.dto;
import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.akulinski.r8meservice.domain.Comment} entity.
 */
public class CommentDTO implements Serializable {

    private Long id;

    private String comment;

    private Instant timeStamp;

    private String receiver;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Instant getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Instant timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CommentDTO commentDTO = (CommentDTO) o;
        if (commentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), commentDTO.getId());
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CommentDTO{" +
            "id=" + getId() +
            ", comment='" + getComment() + "'" +
            ", timeStamp='" + getTimeStamp() + "'" +
            "}";
    }
}
