package com.akulinski.r8meservice.repository.search;

import com.akulinski.r8meservice.domain.Question;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionSearchRepository extends ElasticsearchCrudRepository<Question, String> {

    @Override
    List<Question> findAll();

    List<Question> findAllByPoster(Long poster);
}
