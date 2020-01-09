/*
package com.akulinski.r8meservice.web.rest;

import com.akulinski.r8meservice.RedisTestContainerExtension;
import com.akulinski.r8meservice.R8Meuserservice2App;
import com.akulinski.r8meservice.domain.CommentXProfile;
import com.akulinski.r8meservice.repository.CommentXProfileRepository;
import com.akulinski.r8meservice.repository.search.CommentXProfileSearchRepository;
import com.akulinski.r8meservice.service.CommentXProfileService;
import com.akulinski.r8meservice.service.dto.CommentXProfileDTO;
import com.akulinski.r8meservice.service.mapper.CommentXProfileMapper;
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

*/
/**
 * Integration tests for the {@link CommentXProfileResource} REST controller.
 *//*

@SpringBootTest(classes = R8Meuserservice2App.class)
@ExtendWith(RedisTestContainerExtension.class)
public class CommentXProfileResourceIT {

    @Autowired
    private CommentXProfileRepository commentXProfileRepository;

    @Autowired
    private CommentXProfileMapper commentXProfileMapper;

    @Autowired
    private CommentXProfileService commentXProfileService;

    */
/**
     * This repository is mocked in the com.akulinski.r8meservice.repository.search test package.
     *
     * @see com.akulinski.r8meservice.repository.search.CommentXProfileSearchRepositoryMockConfiguration
     *//*

    @Autowired
    private CommentXProfileSearchRepository mockCommentXProfileSearchRepository;

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

    private MockMvc restCommentXProfileMockMvc;

    private CommentXProfile commentXProfile;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CommentXProfileResource commentXProfileResource = new CommentXProfileResource(commentXProfileService);
        this.restCommentXProfileMockMvc = MockMvcBuilders.standaloneSetup(commentXProfileResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    */
/**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     *//*

    public static CommentXProfile createEntity(EntityManager em) {
        CommentXProfile commentXProfile = new CommentXProfile();
        return commentXProfile;
    }
    */
/**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     *//*

    public static CommentXProfile createUpdatedEntity(EntityManager em) {
        CommentXProfile commentXProfile = new CommentXProfile();
        return commentXProfile;
    }

    @BeforeEach
    public void initTest() {
        commentXProfile = createEntity(em);
    }

    @Test
    @Transactional
    public void createCommentXProfile() throws Exception {
        int databaseSizeBeforeCreate = commentXProfileRepository.findAll().size();

        // Create the CommentXProfile
        CommentXProfileDTO commentXProfileDTO = commentXProfileMapper.toDto(commentXProfile);
        restCommentXProfileMockMvc.perform(post("/api/comment-x-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commentXProfileDTO)))
            .andExpect(status().isCreated());

        // Validate the CommentXProfile in the database
        List<CommentXProfile> commentXProfileList = commentXProfileRepository.findAll();
        assertThat(commentXProfileList).hasSize(databaseSizeBeforeCreate + 1);
        CommentXProfile testCommentXProfile = commentXProfileList.get(commentXProfileList.size() - 1);

        // Validate the CommentXProfile in Elasticsearch
        verify(mockCommentXProfileSearchRepository, times(1)).save(testCommentXProfile);
    }

    @Test
    @Transactional
    public void createCommentXProfileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = commentXProfileRepository.findAll().size();

        // Create the CommentXProfile with an existing ID
        CommentXProfileDTO commentXProfileDTO = commentXProfileMapper.toDto(commentXProfile);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommentXProfileMockMvc.perform(post("/api/comment-x-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commentXProfileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CommentXProfile in the database
        List<CommentXProfile> commentXProfileList = commentXProfileRepository.findAll();
        assertThat(commentXProfileList).hasSize(databaseSizeBeforeCreate);

        // Validate the CommentXProfile in Elasticsearch
        verify(mockCommentXProfileSearchRepository, times(0)).save(commentXProfile);
    }


    @Test
    @Transactional
    public void getAllCommentXProfiles() throws Exception {
        // Initialize the database
        commentXProfileRepository.saveAndFlush(commentXProfile);

        // Get all the commentXProfileList
        restCommentXProfileMockMvc.perform(get("/api/comment-x-profiles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)) ;
    }

    @Test
    @Transactional
    public void getCommentXProfile() throws Exception {
        // Initialize the database
        commentXProfileRepository.saveAndFlush(commentXProfile);
*/
/*
        // Get the commentXProfile
        restCommentXProfileMockMvc.perform(get("/api/comment-x-profiles/{id}", commentXProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(commentXProfile.getId().intValue()));*//*

    }

    @Test
    @Transactional
    public void getNonExistingCommentXProfile() throws Exception {
        // Get the commentXProfile
        restCommentXProfileMockMvc.perform(get("/api/comment-x-profiles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

  */
/*  @Test
    @Transactional
    public void updateCommentXProfile() throws Exception {
        // Initialize the database
        commentXProfileRepository.saveAndFlush(commentXProfile);

        int databaseSizeBeforeUpdate = commentXProfileRepository.findAll().size();

        // Update the commentXProfile
        CommentXProfile updatedCommentXProfile = commentXProfileRepository.findById(commentXProfile.getId()).get();
        // Disconnect from session so that the updates on updatedCommentXProfile are not directly saved in db
        em.detach(updatedCommentXProfile);
        CommentXProfileDTO commentXProfileDTO = commentXProfileMapper.toDto(updatedCommentXProfile);

        restCommentXProfileMockMvc.perform(put("/api/comment-x-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commentXProfileDTO)))
            .andExpect(status().isOk());

        // Validate the CommentXProfile in the database
        List<CommentXProfile> commentXProfileList = commentXProfileRepository.findAll();
        assertThat(commentXProfileList).hasSize(databaseSizeBeforeUpdate);
        CommentXProfile testCommentXProfile = commentXProfileList.get(commentXProfileList.size() - 1);

        // Validate the CommentXProfile in Elasticsearch
        verify(mockCommentXProfileSearchRepository, times(1)).save(testCommentXProfile);
    }*//*


    @Test
    @Transactional
    public void updateNonExistingCommentXProfile() throws Exception {
        int databaseSizeBeforeUpdate = commentXProfileRepository.findAll().size();

        // Create the CommentXProfile
        CommentXProfileDTO commentXProfileDTO = commentXProfileMapper.toDto(commentXProfile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommentXProfileMockMvc.perform(put("/api/comment-x-profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(commentXProfileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CommentXProfile in the database
        List<CommentXProfile> commentXProfileList = commentXProfileRepository.findAll();
        assertThat(commentXProfileList).hasSize(databaseSizeBeforeUpdate);

        // Validate the CommentXProfile in Elasticsearch
        verify(mockCommentXProfileSearchRepository, times(0)).save(commentXProfile);
    }

    @Test
    @Transactional
    public void deleteCommentXProfile() throws Exception {
        // Initialize the database
        commentXProfileRepository.saveAndFlush(commentXProfile);

        int databaseSizeBeforeDelete = commentXProfileRepository.findAll().size();

        // Delete the commentXProfile
        restCommentXProfileMockMvc.perform(delete("/api/comment-x-profiles/{id}", commentXProfile.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CommentXProfile> commentXProfileList = commentXProfileRepository.findAll();
        assertThat(commentXProfileList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the CommentXProfile in Elasticsearch
        verify(mockCommentXProfileSearchRepository, times(1)).deleteById(commentXProfile.getId());
    }

    @Test
    @Transactional
    public void searchCommentXProfile() throws Exception {
        // Initialize the database
        commentXProfileRepository.saveAndFlush(commentXProfile);
        when(mockCommentXProfileSearchRepository.search(queryStringQuery("id:" + commentXProfile.getId())))
            .thenReturn(Collections.singletonList(commentXProfile));
        // Search the commentXProfile
        restCommentXProfileMockMvc.perform(get("/api/_search/comment-x-profiles?query=id:" + commentXProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commentXProfile.getId().intValue())));
    }
}
*/
