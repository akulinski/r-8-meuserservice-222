package com.akulinski.r8meservice.web.rest;

import com.akulinski.r8meservice.domain.CommentXProfile;
import com.akulinski.r8meservice.repository.CommentXProfileRepository;
import com.akulinski.r8meservice.repository.search.CommentXProfileSearchRepository;
import com.akulinski.r8meservice.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional; 
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.akulinski.r8meservice.domain.CommentXProfile}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CommentXProfileResource {

    private final Logger log = LoggerFactory.getLogger(CommentXProfileResource.class);

    private static final String ENTITY_NAME = "commentXProfile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommentXProfileRepository commentXProfileRepository;

    private final CommentXProfileSearchRepository commentXProfileSearchRepository;

    public CommentXProfileResource(CommentXProfileRepository commentXProfileRepository, CommentXProfileSearchRepository commentXProfileSearchRepository) {
        this.commentXProfileRepository = commentXProfileRepository;
        this.commentXProfileSearchRepository = commentXProfileSearchRepository;
    }

    /**
     * {@code POST  /comment-x-profiles} : Create a new commentXProfile.
     *
     * @param commentXProfile the commentXProfile to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new commentXProfile, or with status {@code 400 (Bad Request)} if the commentXProfile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/comment-x-profiles")
    public ResponseEntity<CommentXProfile> createCommentXProfile(@RequestBody CommentXProfile commentXProfile) throws URISyntaxException {
        log.debug("REST request to save CommentXProfile : {}", commentXProfile);
        if (commentXProfile.getId() != null) {
            throw new BadRequestAlertException("A new commentXProfile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CommentXProfile result = commentXProfileRepository.save(commentXProfile);
        commentXProfileSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/comment-x-profiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /comment-x-profiles} : Updates an existing commentXProfile.
     *
     * @param commentXProfile the commentXProfile to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commentXProfile,
     * or with status {@code 400 (Bad Request)} if the commentXProfile is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commentXProfile couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/comment-x-profiles")
    public ResponseEntity<CommentXProfile> updateCommentXProfile(@RequestBody CommentXProfile commentXProfile) throws URISyntaxException {
        log.debug("REST request to update CommentXProfile : {}", commentXProfile);
        if (commentXProfile.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CommentXProfile result = commentXProfileRepository.save(commentXProfile);
        commentXProfileSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, commentXProfile.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /comment-x-profiles} : get all the commentXProfiles.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of commentXProfiles in body.
     */
    @GetMapping("/comment-x-profiles")
    public List<CommentXProfile> getAllCommentXProfiles() {
        log.debug("REST request to get all CommentXProfiles");
        return commentXProfileRepository.findAll();
    }

    /**
     * {@code GET  /comment-x-profiles/:id} : get the "id" commentXProfile.
     *
     * @param id the id of the commentXProfile to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the commentXProfile, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/comment-x-profiles/{id}")
    public ResponseEntity<CommentXProfile> getCommentXProfile(@PathVariable Long id) {
        log.debug("REST request to get CommentXProfile : {}", id);
        Optional<CommentXProfile> commentXProfile = commentXProfileRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(commentXProfile);
    }

    /**
     * {@code DELETE  /comment-x-profiles/:id} : delete the "id" commentXProfile.
     *
     * @param id the id of the commentXProfile to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/comment-x-profiles/{id}")
    public ResponseEntity<Void> deleteCommentXProfile(@PathVariable Long id) {
        log.debug("REST request to delete CommentXProfile : {}", id);
        commentXProfileRepository.deleteById(id);
        commentXProfileSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/comment-x-profiles?query=:query} : search for the commentXProfile corresponding
     * to the query.
     *
     * @param query the query of the commentXProfile search.
     * @return the result of the search.
     */
    @GetMapping("/_search/comment-x-profiles")
    public List<CommentXProfile> searchCommentXProfiles(@RequestParam String query) {
        log.debug("REST request to search CommentXProfiles for query {}", query);
        return StreamSupport
            .stream(commentXProfileSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
