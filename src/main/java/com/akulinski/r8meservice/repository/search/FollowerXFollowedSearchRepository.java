package com.akulinski.r8meservice.repository.search;
import com.akulinski.r8meservice.domain.FollowerXFollowed;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link FollowerXFollowed} entity.
 */
public interface FollowerXFollowedSearchRepository extends ElasticsearchRepository<FollowerXFollowed, Long> {
}
