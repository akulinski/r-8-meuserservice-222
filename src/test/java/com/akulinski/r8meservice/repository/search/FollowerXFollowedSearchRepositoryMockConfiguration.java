package com.akulinski.r8meservice.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link FollowerXFollowedSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class FollowerXFollowedSearchRepositoryMockConfiguration {

    @MockBean
    private FollowerXFollowedSearchRepository mockFollowerXFollowedSearchRepository;

}
