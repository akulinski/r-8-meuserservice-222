package com.akulinski.r8meservice.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class FollowerXFollowedMapperTest {

    private FollowerXFollowedMapper followerXFollowedMapper;

    @BeforeEach
    public void setUp() {
        followerXFollowedMapper = new FollowerXFollowedMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(followerXFollowedMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(followerXFollowedMapper.fromId(null)).isNull();
    }
}
