package com.akulinski.r8meservice.service.dto;
import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.akulinski.r8meservice.domain.Rate} entity.
 */
public class RateDTO implements Serializable {

    private Long id;

    private Double value;

    private String question;

    private Instant timeStamp;

    private String rated;

    private String rating;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Instant getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Instant timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getRated() {
        return rated;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RateDTO rateDTO = (RateDTO) o;
        if (rateDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rateDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RateDTO{" +
            "id=" + getId() +
            ", value=" + getValue() +
            ", question='" + getQuestion() + "'" +
            ", timeStamp='" + getTimeStamp() + "'" +
            "}";
    }
}
