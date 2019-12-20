package com.akulinski.r8meservice.web.rest;

import com.akulinski.r8meservice.RedisTestContainerExtension;
import com.akulinski.r8meservice.R8Meuserservice2App;
import com.akulinski.r8meservice.domain.RateXProfile;
import com.akulinski.r8meservice.repository.RateXProfileRepository;
import com.akulinski.r8meservice.repository.search.RateXProfileSearchRepository;
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
 * Integration tests for the {@link RateXProfileResource} REST controller.
 */
@SpringBootTest(classes = R8Meuserservice2App.class)
@ExtendWith(RedisTestContainerExtension.class)
public class RateXProfileResourceIT {

    @Autowired
    private RateXProfileRepository rateXProfileRepository;

    /**
     * This repository is mocked in the com.akulinski.r8meservice.repository.search test package.
     *
     * @see com.akulinski.r8meservice.repository.search.RateXProfileSearchRepositoryMockConfiguration
     */
    @Autowired
    private RateXProfileSearchRepository mockRateXProfileSearchRepository;

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

    private MockMvc restRateXProfileMockMvc;

    private RateXProfile rateXProfile;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RateXProfileResource rateXProfileResource = new RateXProfileResource(rateXProfileRepository, mockRateXProfileSearchRepository);
        this.restRateXProfileMockMvc = MockMvcBuilders.standaloneSetup(rateXProfileResource)
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
    public static RateXProfile createEntity(EntityManager em) {
        RateXProfile rateXProfile = new RateXProfile();
        return rateXProfile;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RateXProfile createUpdatedEntity(EntityManager em) {
        RateXProfile rateXProfile = new RateXProfile();
        return rateXProfile;
    }

    @BeforeEach
    public void initTest() {
        rateXProfile = createEntity(em);
    }

    @Test
    @Transactional
    public void createRateXProfile() throws Exception {
        int databaseSizeBeforeCreate = rateXProfileRepository.findAll().size();

        // Create the RateXProfile
        restRateXProfileMockMvc.perform(post("/api/rate-x-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rateXProfile)))
            .andExpect(status().isCreated());

        // Validate the RateXProfile in the database
        List<RateXProfile> rateXProfileList = rateXProfileRepository.findAll();
        assertThat(rateXProfileList).hasSize(databaseSizeBeforeCreate + 1);
        RateXProfile testRateXProfile = rateXProfileList.get(rateXProfileList.size() - 1);

        // Validate the RateXProfile in Elasticsearch
        verify(mockRateXProfileSearchRepository, times(1)).save(testRateXProfile);
    }

    @Test
    @Transactional
    public void createRateXProfileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rateXProfileRepository.findAll().size();

        // Create the RateXProfile with an existing ID
        rateXProfile.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRateXProfileMockMvc.perform(post("/api/rate-x-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rateXProfile)))
            .andExpect(status().isBadRequest());

        // Validate the RateXProfile in the database
        List<RateXProfile> rateXProfileList = rateXProfileRepository.findAll();
        assertThat(rateXProfileList).hasSize(databaseSizeBeforeCreate);

        // Validate the RateXProfile in Elasticsearch
        verify(mockRateXProfileSearchRepository, times(0)).save(rateXProfile);
    }


    @Test
    @Transactional
    public void getAllRateXProfiles() throws Exception {
        // Initialize the database
        rateXProfileRepository.saveAndFlush(rateXProfile);

        // Get all the rateXProfileList
        restRateXProfileMockMvc.perform(get("/api/rate-x-profiles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rateXProfile.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getRateXProfile() throws Exception {
        // Initialize the database
        rateXProfileRepository.saveAndFlush(rateXProfile);

        // Get the rateXProfile
        restRateXProfileMockMvc.perform(get("/api/rate-x-profiles/{id}", rateXProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rateXProfile.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingRateXProfile() throws Exception {
        // Get the rateXProfile
        restRateXProfileMockMvc.perform(get("/api/rate-x-profiles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRateXProfile() throws Exception {
        // Initialize the database
        rateXProfileRepository.saveAndFlush(rateXProfile);

        int databaseSizeBeforeUpdate = rateXProfileRepository.findAll().size();

        // Update the rateXProfile
        RateXProfile updatedRateXProfile = rateXProfileRepository.findById(rateXProfile.getId()).get();
        // Disconnect from session so that the updates on updatedRateXProfile are not directly saved in db
        em.detach(updatedRateXProfile);

        restRateXProfileMockMvc.perform(put("/api/rate-x-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRateXProfile)))
            .andExpect(status().isOk());

        // Validate the RateXProfile in the database
        List<RateXProfile> rateXProfileList = rateXProfileRepository.findAll();
        assertThat(rateXProfileList).hasSize(databaseSizeBeforeUpdate);
        RateXProfile testRateXProfile = rateXProfileList.get(rateXProfileList.size() - 1);

        // Validate the RateXProfile in Elasticsearch
        verify(mockRateXProfileSearchRepository, times(1)).save(testRateXProfile);
    }

    @Test
    @Transactional
    public void updateNonExistingRateXProfile() throws Exception {
        int databaseSizeBeforeUpdate = rateXProfileRepository.findAll().size();

        // Create the RateXProfile

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRateXProfileMockMvc.perform(put("/api/rate-x-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rateXProfile)))
            .andExpect(status().isBadRequest());

        // Validate the RateXProfile in the database
        List<RateXProfile> rateXProfileList = rateXProfileRepository.findAll();
        assertThat(rateXProfileList).hasSize(databaseSizeBeforeUpdate);

        // Validate the RateXProfile in Elasticsearch
        verify(mockRateXProfileSearchRepository, times(0)).save(rateXProfile);
    }

    @Test
    @Transactional
    public void deleteRateXProfile() throws Exception {
        // Initialize the database
        rateXProfileRepository.saveAndFlush(rateXProfile);

        int databaseSizeBeforeDelete = rateXProfileRepository.findAll().size();

        // Delete the rateXProfile
        restRateXProfileMockMvc.perform(delete("/api/rate-x-profiles/{id}", rateXProfile.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RateXProfile> rateXProfileList = rateXProfileRepository.findAll();
        assertThat(rateXProfileList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the RateXProfile in Elasticsearch
        verify(mockRateXProfileSearchRepository, times(1)).deleteById(rateXProfile.getId());
    }

    @Test
    @Transactional
    public void searchRateXProfile() throws Exception {
        // Initialize the database
        rateXProfileRepository.saveAndFlush(rateXProfile);
        when(mockRateXProfileSearchRepository.search(queryStringQuery("id:" + rateXProfile.getId())))
            .thenReturn(Collections.singletonList(rateXProfile));
        // Search the rateXProfile
        restRateXProfileMockMvc.perform(get("/api/_search/rate-x-profiles?query=id:" + rateXProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rateXProfile.getId().intValue())));
    }
}
