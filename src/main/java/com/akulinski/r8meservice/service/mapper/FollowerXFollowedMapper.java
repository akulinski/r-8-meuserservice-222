package com.akulinski.r8meservice.service.mapper;

import com.akulinski.r8meservice.domain.*;
import com.akulinski.r8meservice.service.dto.FollowerXFollowedDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link FollowerXFollowed} and its DTO {@link FollowerXFollowedDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserProfileMapper.class})
public interface FollowerXFollowedMapper extends EntityMapper<FollowerXFollowedDTO, FollowerXFollowed> {

    default FollowerXFollowedDTO toDto(FollowerXFollowed followerXFollowed){
        return new FollowerXFollowedDTO();

    }

   default FollowerXFollowed toEntity(FollowerXFollowedDTO followerXFollowedDTO){
        return new FollowerXFollowed();
   }

    default FollowerXFollowed fromId(Long id) {
        if (id == null) {
            return null;
        }
        FollowerXFollowed followerXFollowed = new FollowerXFollowed();
        followerXFollowed.setId(id);
        return followerXFollowed;
    }
}
