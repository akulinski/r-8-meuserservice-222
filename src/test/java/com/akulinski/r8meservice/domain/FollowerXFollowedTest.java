package com.akulinski.r8meservice.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.akulinski.r8meservice.web.rest.TestUtil;

public class FollowerXFollowedTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FollowerXFollowed.class);
        FollowerXFollowed followerXFollowed1 = new FollowerXFollowed();
        followerXFollowed1.setId(1L);
        FollowerXFollowed followerXFollowed2 = new FollowerXFollowed();
        followerXFollowed2.setId(followerXFollowed1.getId());
        assertThat(followerXFollowed1).isEqualTo(followerXFollowed2);
        followerXFollowed2.setId(2L);
        assertThat(followerXFollowed1).isNotEqualTo(followerXFollowed2);
        followerXFollowed1.setId(null);
        assertThat(followerXFollowed1).isNotEqualTo(followerXFollowed2);
    }
}
