package com.akulinski.r8meservice.repository.search;
import com.akulinski.r8meservice.domain.Rate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Rate} entity.
 */
public interface RateSearchRepository extends ElasticsearchRepository<Rate, Long> {
}
