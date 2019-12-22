package com.akulinski.r8meservice.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class CommentXProfileMapperTest {

    private CommentXProfileMapper commentXProfileMapper;

    @BeforeEach
    public void setUp() {
        commentXProfileMapper = new CommentXProfileMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(commentXProfileMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(commentXProfileMapper.fromId(null)).isNull();
    }
}
