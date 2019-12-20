package com.akulinski.r8meservice.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.akulinski.r8meservice.web.rest.TestUtil;

public class RateXProfileTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RateXProfile.class);
        RateXProfile rateXProfile1 = new RateXProfile();
        rateXProfile1.setId(1L);
        RateXProfile rateXProfile2 = new RateXProfile();
        rateXProfile2.setId(rateXProfile1.getId());
        assertThat(rateXProfile1).isEqualTo(rateXProfile2);
        rateXProfile2.setId(2L);
        assertThat(rateXProfile1).isNotEqualTo(rateXProfile2);
        rateXProfile1.setId(null);
        assertThat(rateXProfile1).isNotEqualTo(rateXProfile2);
    }
}
