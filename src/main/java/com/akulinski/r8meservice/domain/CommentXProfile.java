package com.akulinski.r8meservice.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A CommentXProfile.
 */
@Entity
@Table(name = "comment_x_profile")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "commentxprofile")
public class CommentXProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @OneToOne
    @JoinColumn
    private UserProfile receiver;

    @OneToOne
    @JoinColumn
    private UserProfile poster;

    @OneToOne
    @JoinColumn
    private Comment comment;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserProfile getReceiver() {
        return receiver;
    }

    public CommentXProfile receiver(UserProfile userProfile) {
        this.receiver = userProfile;
        return this;
    }

    public void setReceiver(UserProfile userProfile) {
        this.receiver = userProfile;
    }

    public UserProfile getPoster() {
        return poster;
    }

    public CommentXProfile poster(UserProfile userProfile) {
        this.poster = userProfile;
        return this;
    }

    public void setPoster(UserProfile userProfile) {
        this.poster = userProfile;
    }

    public Comment getComment() {
        return comment;
    }

    public CommentXProfile comment(Comment comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommentXProfile)) {
            return false;
        }
        return id != null && id.equals(((CommentXProfile) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CommentXProfile{" +
            "id=" + getId() +
            "}";
    }
}
