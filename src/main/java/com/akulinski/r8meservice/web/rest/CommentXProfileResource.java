package com.akulinski.r8meservice.web.rest;

import com.akulinski.r8meservice.service.CommentXProfileService;
import com.akulinski.r8meservice.web.rest.errors.BadRequestAlertException;
import com.akulinski.r8meservice.service.dto.CommentXProfileDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.akulinski.r8meservice.domain.CommentXProfile}.
 */
@RestController
@RequestMapping("/api")
public class CommentXProfileResource {

    private final Logger log = LoggerFactory.getLogger(CommentXProfileResource.class);

    private static final String ENTITY_NAME = "commentXProfile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommentXProfileService commentXProfileService;

    public CommentXProfileResource(CommentXProfileService commentXProfileService) {
        this.commentXProfileService = commentXProfileService;
    }

    /**
     * {@code POST  /comment-x-profiles} : Create a new commentXProfile.
     *
     * @param commentXProfileDTO the commentXProfileDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new commentXProfileDTO, or with status {@code 400 (Bad Request)} if the commentXProfile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/comment-x-profiles")
    public ResponseEntity<CommentXProfileDTO> createCommentXProfile(@RequestBody CommentXProfileDTO commentXProfileDTO) throws URISyntaxException {
        log.debug("REST request to save CommentXProfile : {}", commentXProfileDTO);
        if (commentXProfileDTO.getId() != null) {
            throw new BadRequestAlertException("A new commentXProfile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CommentXProfileDTO result = commentXProfileService.save(commentXProfileDTO);
        return ResponseEntity.created(new URI("/api/comment-x-profiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /comment-x-profiles} : Updates an existing commentXProfile.
     *
     * @param commentXProfileDTO the commentXProfileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commentXProfileDTO,
     * or with status {@code 400 (Bad Request)} if the commentXProfileDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commentXProfileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/comment-x-profiles")
    public ResponseEntity<CommentXProfileDTO> updateCommentXProfile(@RequestBody CommentXProfileDTO commentXProfileDTO) throws URISyntaxException {
        log.debug("REST request to update CommentXProfile : {}", commentXProfileDTO);
        if (commentXProfileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CommentXProfileDTO result = commentXProfileService.save(commentXProfileDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, commentXProfileDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /comment-x-profiles} : get all the commentXProfiles.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of commentXProfiles in body.
     */
    @GetMapping("/comment-x-profiles")
    public List<CommentXProfileDTO> getAllCommentXProfiles() {
        log.debug("REST request to get all CommentXProfiles");
        return commentXProfileService.findAll();
    }

    /**
     * {@code GET  /comment-x-profiles/:id} : get the "id" commentXProfile.
     *
     * @param id the id of the commentXProfileDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the commentXProfileDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/comment-x-profiles/{id}")
    public ResponseEntity<CommentXProfileDTO> getCommentXProfile(@PathVariable Long id) {
        log.debug("REST request to get CommentXProfile : {}", id);
        Optional<CommentXProfileDTO> commentXProfileDTO = commentXProfileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(commentXProfileDTO);
    }

    /**
     * {@code DELETE  /comment-x-profiles/:id} : delete the "id" commentXProfile.
     *
     * @param id the id of the commentXProfileDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/comment-x-profiles/{id}")
    public ResponseEntity<Void> deleteCommentXProfile(@PathVariable Long id) {
        log.debug("REST request to delete CommentXProfile : {}", id);
        commentXProfileService.delete(id);
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
    public List<CommentXProfileDTO> searchCommentXProfiles(@RequestParam String query) {
        log.debug("REST request to search CommentXProfiles for query {}", query);
        return commentXProfileService.search(query);
    }
}
