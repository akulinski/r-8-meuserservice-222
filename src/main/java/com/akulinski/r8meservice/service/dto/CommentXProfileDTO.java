package com.akulinski.r8meservice.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.akulinski.r8meservice.domain.CommentXProfile} entity.
 */
public class CommentXProfileDTO implements Serializable {

    private Long id;


    private Long receiverId;

    private Long posterId;

    private Long commentId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long userProfileId) {
        this.receiverId = userProfileId;
    }

    public Long getPosterId() {
        return posterId;
    }

    public void setPosterId(Long userProfileId) {
        this.posterId = userProfileId;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CommentXProfileDTO commentXProfileDTO = (CommentXProfileDTO) o;
        if (commentXProfileDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), commentXProfileDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CommentXProfileDTO{" +
            "id=" + getId() +
            ", receiver=" + getReceiverId() +
            ", poster=" + getPosterId() +
            ", comment=" + getCommentId() +
            "}";
    }
}
