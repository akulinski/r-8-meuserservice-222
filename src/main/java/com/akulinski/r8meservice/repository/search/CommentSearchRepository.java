package com.akulinski.r8meservice.repository.search;
import com.akulinski.r8meservice.domain.Comment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Comment} entity.
 */
public interface CommentSearchRepository extends ElasticsearchRepository<Comment, Long> {
}
