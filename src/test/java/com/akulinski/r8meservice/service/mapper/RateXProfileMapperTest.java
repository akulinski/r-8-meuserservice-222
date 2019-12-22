package com.akulinski.r8meservice.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class RateXProfileMapperTest {

    private RateXProfileMapper rateXProfileMapper;

    @BeforeEach
    public void setUp() {
        rateXProfileMapper = new RateXProfileMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(rateXProfileMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(rateXProfileMapper.fromId(null)).isNull();
    }
}
