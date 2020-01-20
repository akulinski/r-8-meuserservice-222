package com.akulinski.r8meservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A UserProfile.
 */
@Entity
@Table(name = "user_profile")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "userprofile")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "current_rating")
    private Double currentRating = -1D;

    @Column(name = "rates_count")
    private Long ratesCount = 0L;

    @Column(name = "last_calc_run")
    private Instant lastCalcRun = Instant.parse("1970-01-01T00:00:00.00Z");

    @OneToOne
    @JoinColumn(unique = true)
    private User user;
}
