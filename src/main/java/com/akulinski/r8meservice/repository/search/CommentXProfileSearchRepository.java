package com.akulinski.r8meservice.repository.search;
import com.akulinski.r8meservice.domain.CommentXProfile;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link CommentXProfile} entity.
 */
public interface CommentXProfileSearchRepository extends ElasticsearchRepository<CommentXProfile, Long> {
}
