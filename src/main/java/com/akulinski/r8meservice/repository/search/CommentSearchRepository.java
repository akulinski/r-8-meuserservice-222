package com.akulinski.r8meservice.repository.search;

import com.akulinski.r8meservice.domain.Comment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * Spring Data Elasticsearch repository for the {@link Comment} entity.
 */
public interface CommentSearchRepository extends ElasticsearchRepository<Comment, String> {

    @Override
    List<Comment> findAll();

    List<Comment> findAllByReceiver(long receiver);
}
