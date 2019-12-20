package com.akulinski.r8meservice.repository.search;
import com.akulinski.r8meservice.domain.User;
import com.akulinski.r8meservice.domain.UserProfile;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

/**
 * Spring Data Elasticsearch repository for the {@link UserProfile} entity.
 */
public interface UserProfileSearchRepository extends ElasticsearchRepository<UserProfile, Long> {
    Optional<UserProfile> findByUser(User user);
    void deleteAllByUser(User user);
}
