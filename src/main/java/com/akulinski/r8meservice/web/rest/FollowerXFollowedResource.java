package com.akulinski.r8meservice.web.rest;

import com.akulinski.r8meservice.domain.FollowerXFollowed;
import com.akulinski.r8meservice.repository.FollowerXFollowedRepository;
import com.akulinski.r8meservice.repository.search.FollowerXFollowedSearchRepository;
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
 * REST controller for managing {@link com.akulinski.r8meservice.domain.FollowerXFollowed}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FollowerXFollowedResource {

    private final Logger log = LoggerFactory.getLogger(FollowerXFollowedResource.class);

    private static final String ENTITY_NAME = "followerXFollowed";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FollowerXFollowedRepository followerXFollowedRepository;

    private final FollowerXFollowedSearchRepository followerXFollowedSearchRepository;

    public FollowerXFollowedResource(FollowerXFollowedRepository followerXFollowedRepository, FollowerXFollowedSearchRepository followerXFollowedSearchRepository) {
        this.followerXFollowedRepository = followerXFollowedRepository;
        this.followerXFollowedSearchRepository = followerXFollowedSearchRepository;
    }

    /**
     * {@code POST  /follower-x-followeds} : Create a new followerXFollowed.
     *
     * @param followerXFollowed the followerXFollowed to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new followerXFollowed, or with status {@code 400 (Bad Request)} if the followerXFollowed has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/follower-x-followeds")
    public ResponseEntity<FollowerXFollowed> createFollowerXFollowed(@RequestBody FollowerXFollowed followerXFollowed) throws URISyntaxException {
        log.debug("REST request to save FollowerXFollowed : {}", followerXFollowed);
        if (followerXFollowed.getId() != null) {
            throw new BadRequestAlertException("A new followerXFollowed cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FollowerXFollowed result = followerXFollowedRepository.save(followerXFollowed);
        followerXFollowedSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/follower-x-followeds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /follower-x-followeds} : Updates an existing followerXFollowed.
     *
     * @param followerXFollowed the followerXFollowed to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated followerXFollowed,
     * or with status {@code 400 (Bad Request)} if the followerXFollowed is not valid,
     * or with status {@code 500 (Internal Server Error)} if the followerXFollowed couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/follower-x-followeds")
    public ResponseEntity<FollowerXFollowed> updateFollowerXFollowed(@RequestBody FollowerXFollowed followerXFollowed) throws URISyntaxException {
        log.debug("REST request to update FollowerXFollowed : {}", followerXFollowed);
        if (followerXFollowed.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FollowerXFollowed result = followerXFollowedRepository.save(followerXFollowed);
        followerXFollowedSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, followerXFollowed.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /follower-x-followeds} : get all the followerXFolloweds.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of followerXFolloweds in body.
     */
    @GetMapping("/follower-x-followeds")
    public List<FollowerXFollowed> getAllFollowerXFolloweds() {
        log.debug("REST request to get all FollowerXFolloweds");
        return followerXFollowedRepository.findAll();
    }

    /**
     * {@code GET  /follower-x-followeds/:id} : get the "id" followerXFollowed.
     *
     * @param id the id of the followerXFollowed to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the followerXFollowed, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/follower-x-followeds/{id}")
    public ResponseEntity<FollowerXFollowed> getFollowerXFollowed(@PathVariable Long id) {
        log.debug("REST request to get FollowerXFollowed : {}", id);
        Optional<FollowerXFollowed> followerXFollowed = followerXFollowedRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(followerXFollowed);
    }

    /**
     * {@code DELETE  /follower-x-followeds/:id} : delete the "id" followerXFollowed.
     *
     * @param id the id of the followerXFollowed to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/follower-x-followeds/{id}")
    public ResponseEntity<Void> deleteFollowerXFollowed(@PathVariable Long id) {
        log.debug("REST request to delete FollowerXFollowed : {}", id);
        followerXFollowedRepository.deleteById(id);
        followerXFollowedSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/follower-x-followeds?query=:query} : search for the followerXFollowed corresponding
     * to the query.
     *
     * @param query the query of the followerXFollowed search.
     * @return the result of the search.
     */
    @GetMapping("/_search/follower-x-followeds")
    public List<FollowerXFollowed> searchFollowerXFolloweds(@RequestParam String query) {
        log.debug("REST request to search FollowerXFolloweds for query {}", query);
        return StreamSupport
            .stream(followerXFollowedSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
