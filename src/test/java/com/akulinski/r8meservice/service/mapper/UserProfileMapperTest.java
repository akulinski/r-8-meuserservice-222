package com.akulinski.r8meservice.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class UserProfileMapperTest {

    private UserProfileMapper userProfileMapper;

    @BeforeEach
    public void setUp() {
        userProfileMapper = new UserProfileMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(userProfileMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(userProfileMapper.fromId(null)).isNull();
    }
}
