package com.akulinski.r8meservice.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.akulinski.r8meservice.web.rest.TestUtil;

public class FollowerXFollowedDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FollowerXFollowedDTO.class);
        FollowerXFollowedDTO followerXFollowedDTO1 = new FollowerXFollowedDTO();
        followerXFollowedDTO1.setId(1L);
        FollowerXFollowedDTO followerXFollowedDTO2 = new FollowerXFollowedDTO();
        assertThat(followerXFollowedDTO1).isNotEqualTo(followerXFollowedDTO2);
        followerXFollowedDTO2.setId(followerXFollowedDTO1.getId());
        assertThat(followerXFollowedDTO1).isEqualTo(followerXFollowedDTO2);
        followerXFollowedDTO2.setId(2L);
        assertThat(followerXFollowedDTO1).isNotEqualTo(followerXFollowedDTO2);
        followerXFollowedDTO1.setId(null);
        assertThat(followerXFollowedDTO1).isNotEqualTo(followerXFollowedDTO2);
    }
}
