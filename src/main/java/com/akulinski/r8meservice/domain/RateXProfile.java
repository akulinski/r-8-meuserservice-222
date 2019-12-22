package com.akulinski.r8meservice.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A RateXProfile.
 */
@Entity
@Table(name = "rate_x_profile")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "ratexprofile")
public class RateXProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @OneToOne
    @JoinColumn
    private UserProfile rated;

    @OneToOne
    @JoinColumn
    private UserProfile rater;

    @OneToOne
    @JoinColumn
    private Rate rate;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserProfile getRated() {
        return rated;
    }

    public RateXProfile rated(UserProfile userProfile) {
        this.rated = userProfile;
        return this;
    }

    public void setRated(UserProfile userProfile) {
        this.rated = userProfile;
    }

    public UserProfile getRater() {
        return rater;
    }

    public RateXProfile rater(UserProfile userProfile) {
        this.rater = userProfile;
        return this;
    }

    public void setRater(UserProfile userProfile) {
        this.rater = userProfile;
    }

    public Rate getRate() {
        return rate;
    }

    public RateXProfile rate(Rate rate) {
        this.rate = rate;
        return this;
    }

    public void setRate(Rate rate) {
        this.rate = rate;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RateXProfile)) {
            return false;
        }
        return id != null && id.equals(((RateXProfile) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "RateXProfile{" +
            "id=" + getId() +
            "}";
    }
}
