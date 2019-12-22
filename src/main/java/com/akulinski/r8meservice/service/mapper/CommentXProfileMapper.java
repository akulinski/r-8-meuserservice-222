package com.akulinski.r8meservice.service.mapper;

import com.akulinski.r8meservice.domain.*;
import com.akulinski.r8meservice.service.dto.CommentXProfileDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link CommentXProfile} and its DTO {@link CommentXProfileDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserProfileMapper.class, CommentMapper.class})
public interface CommentXProfileMapper extends EntityMapper<CommentXProfileDTO, CommentXProfile> {

    @Mapping(source = "receiver.id", target = "receiverId")
    @Mapping(source = "poster.id", target = "posterId")
    @Mapping(source = "comment.id", target = "commentId")
    CommentXProfileDTO toDto(CommentXProfile commentXProfile);

    @Mapping(source = "receiverId", target = "receiver")
    @Mapping(source = "posterId", target = "poster")
    @Mapping(source = "commentId", target = "comment")
    CommentXProfile toEntity(CommentXProfileDTO commentXProfileDTO);

    default CommentXProfile fromId(Long id) {
        if (id == null) {
            return null;
        }
        CommentXProfile commentXProfile = new CommentXProfile();
        commentXProfile.setId(id);
        return commentXProfile;
    }
}
