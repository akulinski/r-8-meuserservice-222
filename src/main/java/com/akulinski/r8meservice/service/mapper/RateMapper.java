package com.akulinski.r8meservice.service.mapper;

import com.akulinski.r8meservice.domain.*;
import com.akulinski.r8meservice.service.dto.RateDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Rate} and its DTO {@link RateDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RateMapper extends EntityMapper<RateDTO, Rate> {

    default Rate fromId(Long id) {
        if (id == null) {
            return null;
        }
        Rate rate = new Rate();
        rate.setId(id);
        return rate;
    }
}
