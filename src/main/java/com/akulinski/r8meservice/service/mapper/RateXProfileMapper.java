package com.akulinski.r8meservice.service.mapper;

import com.akulinski.r8meservice.domain.*;
import com.akulinski.r8meservice.service.dto.RateXProfileDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link RateXProfile} and its DTO {@link RateXProfileDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserProfileMapper.class, RateMapper.class})
public interface RateXProfileMapper extends EntityMapper<RateXProfileDTO, RateXProfile> {

    @Mapping(source = "rated.id", target = "ratedId")
    @Mapping(source = "rater.id", target = "raterId")
    @Mapping(source = "rate.id", target = "rateId")
    RateXProfileDTO toDto(RateXProfile rateXProfile);

    @Mapping(source = "ratedId", target = "rated")
    @Mapping(source = "raterId", target = "rater")
    @Mapping(source = "rateId", target = "rate")
    RateXProfile toEntity(RateXProfileDTO rateXProfileDTO);

    default RateXProfile fromId(Long id) {
        if (id == null) {
            return null;
        }
        RateXProfile rateXProfile = new RateXProfile();
        rateXProfile.setId(id);
        return rateXProfile;
    }
}
