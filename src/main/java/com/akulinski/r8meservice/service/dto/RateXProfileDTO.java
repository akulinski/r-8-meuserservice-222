package com.akulinski.r8meservice.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.akulinski.r8meservice.domain.RateXProfile} entity.
 */
public class RateXProfileDTO implements Serializable {

    private Long id;


    private Long ratedId;

    private Long raterId;

    private Long rateId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRatedId() {
        return ratedId;
    }

    public void setRatedId(Long userProfileId) {
        this.ratedId = userProfileId;
    }

    public Long getRaterId() {
        return raterId;
    }

    public void setRaterId(Long userProfileId) {
        this.raterId = userProfileId;
    }

    public Long getRateId() {
        return rateId;
    }

    public void setRateId(Long rateId) {
        this.rateId = rateId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RateXProfileDTO rateXProfileDTO = (RateXProfileDTO) o;
        if (rateXProfileDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rateXProfileDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RateXProfileDTO{" +
            "id=" + getId() +
            ", rated=" + getRatedId() +
            ", rater=" + getRaterId() +
            ", rate=" + getRateId() +
            "}";
    }
}
