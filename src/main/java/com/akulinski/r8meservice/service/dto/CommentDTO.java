package com.akulinski.r8meservice.service.dto;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.akulinski.r8meservice.domain.Comment} entity.
 */
public class CommentDTO implements Serializable, Comparable {

    private Long id;

    private String comment;

    private Instant timeStamp;

    private String receiver;

    private String poster;

    private String imageUrl;

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

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommentDTO)) return false;
        CommentDTO that = (CommentDTO) o;
        return Objects.equals(getId(), that.getId()) &&
            Objects.equals(getComment(), that.getComment()) &&
            Objects.equals(getTimeStamp(), that.getTimeStamp()) &&
            Objects.equals(getReceiver(), that.getReceiver()) &&
            Objects.equals(getPoster(), that.getPoster()) &&
            Objects.equals(getImageUrl(), that.getImageUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getComment(), getTimeStamp(), getReceiver(), getPoster(), getImageUrl());
    }

    @Override
    public int compareTo(@NotNull Object o) {
        return this.getTimeStamp().compareTo(((CommentDTO) o).getTimeStamp());
    }
}
