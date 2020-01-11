package com.akulinski.r8meservice.repository.search;

import com.akulinski.r8meservice.domain.OwnerCheck;
import com.akulinski.r8meservice.domain.Question;
import com.akulinski.r8meservice.domain.Rate;
import lombok.NonNull;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionSearchRepository extends ElasticsearchCrudRepository<Question, String> {

    @Query(
        "{" +
            "        \"match_all\": {" +
            "        }" +
            "}"
    )
    List<Question> findAllQuestions();

    List<Question> findAllByPoster(Long poster);

    @OwnerCheck
    void delete(@NonNull Question question);

    List<Question> findByRatesContains(@NonNull Rate rate);
}
