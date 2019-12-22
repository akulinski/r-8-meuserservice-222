package com.akulinski.r8meservice.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.akulinski.r8meservice.web.rest.TestUtil;

public class RateXProfileDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RateXProfileDTO.class);
        RateXProfileDTO rateXProfileDTO1 = new RateXProfileDTO();
        rateXProfileDTO1.setId(1L);
        RateXProfileDTO rateXProfileDTO2 = new RateXProfileDTO();
        assertThat(rateXProfileDTO1).isNotEqualTo(rateXProfileDTO2);
        rateXProfileDTO2.setId(rateXProfileDTO1.getId());
        assertThat(rateXProfileDTO1).isEqualTo(rateXProfileDTO2);
        rateXProfileDTO2.setId(2L);
        assertThat(rateXProfileDTO1).isNotEqualTo(rateXProfileDTO2);
        rateXProfileDTO1.setId(null);
        assertThat(rateXProfileDTO1).isNotEqualTo(rateXProfileDTO2);
    }
}
