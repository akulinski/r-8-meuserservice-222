package com.akulinski.r8meservice.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.akulinski.r8meservice.web.rest.TestUtil;

public class CommentXProfileDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommentXProfileDTO.class);
        CommentXProfileDTO commentXProfileDTO1 = new CommentXProfileDTO();
        commentXProfileDTO1.setId(1L);
        CommentXProfileDTO commentXProfileDTO2 = new CommentXProfileDTO();
        assertThat(commentXProfileDTO1).isNotEqualTo(commentXProfileDTO2);
        commentXProfileDTO2.setId(commentXProfileDTO1.getId());
        assertThat(commentXProfileDTO1).isEqualTo(commentXProfileDTO2);
        commentXProfileDTO2.setId(2L);
        assertThat(commentXProfileDTO1).isNotEqualTo(commentXProfileDTO2);
        commentXProfileDTO1.setId(null);
        assertThat(commentXProfileDTO1).isNotEqualTo(commentXProfileDTO2);
    }
}
