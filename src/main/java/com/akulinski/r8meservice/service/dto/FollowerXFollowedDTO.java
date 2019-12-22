package com.akulinski.r8meservice.service.dto;
import com.akulinski.r8meservice.security.SecurityUtils;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.akulinski.r8meservice.domain.FollowerXFollowed} entity.
 */
public class FollowerXFollowedDTO implements Serializable {

    private Long id;

    private Long followerId;

    private Long followedId;

    private String followerUsername;

    private String followedUsername;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFollowerId() {
        return followerId;
    }

    public void setFollowerId(Long userProfileId) {
        this.followerId = userProfileId;
    }

    public Long getFollowedId() {
        return followedId;
    }

    public void setFollowedId(Long userProfileId) {
        this.followedId = userProfileId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FollowerXFollowedDTO followerXFollowedDTO = (FollowerXFollowedDTO) o;
        if (followerXFollowedDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), followerXFollowedDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FollowerXFollowedDTO{" +
            "id=" + getId() +
            ", follower=" + getFollowerId() +
            ", followed=" + getFollowedId() +
            "}";
    }

    public String getFollowerUsername() {
        return followerUsername;
    }

    public void setFollowerUsername(String followerUsername) {
        this.followerUsername = followerUsername;
    }

    public String getFollowedUsername() {
        return followedUsername;
    }

    public void setFollowedUsername(String followedUsername) {
        this.followedUsername = followedUsername;
    }
}
