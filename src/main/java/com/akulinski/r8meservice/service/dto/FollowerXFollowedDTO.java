package com.akulinski.r8meservice.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * A DTO for the {@link com.akulinski.r8meservice.domain.FollowerXFollowed} entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowerXFollowedDTO implements Serializable {

    private Long id;

    private Long followerId;

    private Long followedId;

    private String followerUsername;

    private String followedUsername;
}
