package com.akulinski.r8meservice.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

/**
 * A FollowerXFollowed.
 */
@Entity
@Table(name = "follower_x_followed", uniqueConstraints = {
    @UniqueConstraint(
        columnNames = {"follower_id", "followed_id"}
    )
})
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "followerxfollowed")
public class FollowerXFollowed implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @OneToOne
    @JoinColumn
    private UserProfile follower;

    @OneToOne
    @JoinColumn
    private UserProfile followed;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserProfile getFollower() {
        return follower;
    }

    public FollowerXFollowed follower(UserProfile userProfile) {
        this.follower = userProfile;
        return this;
    }

    public void setFollower(UserProfile userProfile) {
        this.follower = userProfile;
    }

    public UserProfile getFollowed() {
        return followed;
    }

    public FollowerXFollowed followed(UserProfile userProfile) {
        this.followed = userProfile;
        return this;
    }

    public void setFollowed(UserProfile userProfile) {
        this.followed = userProfile;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FollowerXFollowed)) {
            return false;
        }
        return id != null && id.equals(((FollowerXFollowed) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "FollowerXFollowed{" +
            "id=" + getId() +
            "}";
    }
}
