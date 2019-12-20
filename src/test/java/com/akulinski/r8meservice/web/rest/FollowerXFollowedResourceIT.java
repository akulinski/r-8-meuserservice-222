package com.akulinski.r8meservice.web.rest;

import com.akulinski.r8meservice.RedisTestContainerExtension;
import com.akulinski.r8meservice.R8Meuserservice2App;
import com.akulinski.r8meservice.domain.FollowerXFollowed;
import com.akulinski.r8meservice.repository.FollowerXFollowedRepository;
import com.akulinski.r8meservice.repository.search.FollowerXFollowedSearchRepository;
import com.akulinski.r8meservice.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static com.akulinski.r8meservice.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link FollowerXFollowedResource} REST controller.
 */
@SpringBootTest(classes = R8Meuserservice2App.class)
@ExtendWith(RedisTestContainerExtension.class)
public class FollowerXFollowedResourceIT {

    @Autowired
    private FollowerXFollowedRepository followerXFollowedRepository;

    /**
     * This repository is mocked in the com.akulinski.r8meservice.repository.search test package.
     *
     * @see com.akulinski.r8meservice.repository.search.FollowerXFollowedSearchRepositoryMockConfiguration
     */
    @Autowired
    private FollowerXFollowedSearchRepository mockFollowerXFollowedSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restFollowerXFollowedMockMvc;

    private FollowerXFollowed followerXFollowed;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FollowerXFollowedResource followerXFollowedResource = new FollowerXFollowedResource(followerXFollowedRepository, mockFollowerXFollowedSearchRepository);
        this.restFollowerXFollowedMockMvc = MockMvcBuilders.standaloneSetup(followerXFollowedResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FollowerXFollowed createEntity(EntityManager em) {
        FollowerXFollowed followerXFollowed = new FollowerXFollowed();
        return followerXFollowed;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FollowerXFollowed createUpdatedEntity(EntityManager em) {
        FollowerXFollowed followerXFollowed = new FollowerXFollowed();
        return followerXFollowed;
    }

    @BeforeEach
    public void initTest() {
        followerXFollowed = createEntity(em);
    }

    @Test
    @Transactional
    public void createFollowerXFollowed() throws Exception {
        int databaseSizeBeforeCreate = followerXFollowedRepository.findAll().size();

        // Create the FollowerXFollowed
        restFollowerXFollowedMockMvc.perform(post("/api/follower-x-followeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(followerXFollowed)))
            .andExpect(status().isCreated());

        // Validate the FollowerXFollowed in the database
        List<FollowerXFollowed> followerXFollowedList = followerXFollowedRepository.findAll();
        assertThat(followerXFollowedList).hasSize(databaseSizeBeforeCreate + 1);
        FollowerXFollowed testFollowerXFollowed = followerXFollowedList.get(followerXFollowedList.size() - 1);

        // Validate the FollowerXFollowed in Elasticsearch
        verify(mockFollowerXFollowedSearchRepository, times(1)).save(testFollowerXFollowed);
    }

    @Test
    @Transactional
    public void createFollowerXFollowedWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = followerXFollowedRepository.findAll().size();

        // Create the FollowerXFollowed with an existing ID
        followerXFollowed.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFollowerXFollowedMockMvc.perform(post("/api/follower-x-followeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(followerXFollowed)))
            .andExpect(status().isBadRequest());

        // Validate the FollowerXFollowed in the database
        List<FollowerXFollowed> followerXFollowedList = followerXFollowedRepository.findAll();
        assertThat(followerXFollowedList).hasSize(databaseSizeBeforeCreate);

        // Validate the FollowerXFollowed in Elasticsearch
        verify(mockFollowerXFollowedSearchRepository, times(0)).save(followerXFollowed);
    }


    @Test
    @Transactional
    public void getAllFollowerXFolloweds() throws Exception {
        // Initialize the database
        followerXFollowedRepository.saveAndFlush(followerXFollowed);

        // Get all the followerXFollowedList
        restFollowerXFollowedMockMvc.perform(get("/api/follower-x-followeds?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(followerXFollowed.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getFollowerXFollowed() throws Exception {
        // Initialize the database
        followerXFollowedRepository.saveAndFlush(followerXFollowed);

        // Get the followerXFollowed
        restFollowerXFollowedMockMvc.perform(get("/api/follower-x-followeds/{id}", followerXFollowed.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(followerXFollowed.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingFollowerXFollowed() throws Exception {
        // Get the followerXFollowed
        restFollowerXFollowedMockMvc.perform(get("/api/follower-x-followeds/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFollowerXFollowed() throws Exception {
        // Initialize the database
        followerXFollowedRepository.saveAndFlush(followerXFollowed);

        int databaseSizeBeforeUpdate = followerXFollowedRepository.findAll().size();

        // Update the followerXFollowed
        FollowerXFollowed updatedFollowerXFollowed = followerXFollowedRepository.findById(followerXFollowed.getId()).get();
        // Disconnect from session so that the updates on updatedFollowerXFollowed are not directly saved in db
        em.detach(updatedFollowerXFollowed);

        restFollowerXFollowedMockMvc.perform(put("/api/follower-x-followeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFollowerXFollowed)))
            .andExpect(status().isOk());

        // Validate the FollowerXFollowed in the database
        List<FollowerXFollowed> followerXFollowedList = followerXFollowedRepository.findAll();
        assertThat(followerXFollowedList).hasSize(databaseSizeBeforeUpdate);
        FollowerXFollowed testFollowerXFollowed = followerXFollowedList.get(followerXFollowedList.size() - 1);

        // Validate the FollowerXFollowed in Elasticsearch
        verify(mockFollowerXFollowedSearchRepository, times(1)).save(testFollowerXFollowed);
    }

    @Test
    @Transactional
    public void updateNonExistingFollowerXFollowed() throws Exception {
        int databaseSizeBeforeUpdate = followerXFollowedRepository.findAll().size();

        // Create the FollowerXFollowed

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFollowerXFollowedMockMvc.perform(put("/api/follower-x-followeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(followerXFollowed)))
            .andExpect(status().isBadRequest());

        // Validate the FollowerXFollowed in the database
        List<FollowerXFollowed> followerXFollowedList = followerXFollowedRepository.findAll();
        assertThat(followerXFollowedList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FollowerXFollowed in Elasticsearch
        verify(mockFollowerXFollowedSearchRepository, times(0)).save(followerXFollowed);
    }

    @Test
    @Transactional
    public void deleteFollowerXFollowed() throws Exception {
        // Initialize the database
        followerXFollowedRepository.saveAndFlush(followerXFollowed);

        int databaseSizeBeforeDelete = followerXFollowedRepository.findAll().size();

        // Delete the followerXFollowed
        restFollowerXFollowedMockMvc.perform(delete("/api/follower-x-followeds/{id}", followerXFollowed.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FollowerXFollowed> followerXFollowedList = followerXFollowedRepository.findAll();
        assertThat(followerXFollowedList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the FollowerXFollowed in Elasticsearch
        verify(mockFollowerXFollowedSearchRepository, times(1)).deleteById(followerXFollowed.getId());
    }

    @Test
    @Transactional
    public void searchFollowerXFollowed() throws Exception {
        // Initialize the database
        followerXFollowedRepository.saveAndFlush(followerXFollowed);
        when(mockFollowerXFollowedSearchRepository.search(queryStringQuery("id:" + followerXFollowed.getId())))
            .thenReturn(Collections.singletonList(followerXFollowed));
        // Search the followerXFollowed
        restFollowerXFollowedMockMvc.perform(get("/api/_search/follower-x-followeds?query=id:" + followerXFollowed.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(followerXFollowed.getId().intValue())));
    }
}
