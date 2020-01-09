package com.akulinski.r8meservice.service.mapper;

import com.akulinski.r8meservice.domain.*;
import com.akulinski.r8meservice.service.dto.UserProfileDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserProfile} and its DTO {@link UserProfileDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface UserProfileMapper extends EntityMapper<UserProfileDTO, UserProfile> {

    UserProfileDTO toDto(UserProfile userProfile);

    UserProfile toEntity(UserProfileDTO userProfileDTO);

    default UserProfile fromId(Long id) {
        if (id == null) {
            return null;
        }
        UserProfile userProfile = new UserProfile();
        userProfile.setId(id);
        return userProfile;
    }
}
