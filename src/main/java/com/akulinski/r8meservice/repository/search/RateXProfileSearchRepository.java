package com.akulinski.r8meservice.repository.search;
import com.akulinski.r8meservice.domain.Rate;
import com.akulinski.r8meservice.domain.RateXProfile;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link RateXProfile} entity.
 */
public interface RateXProfileSearchRepository extends ElasticsearchRepository<RateXProfile, Long> {
    void deleteAllByRate(Rate rate);

}
