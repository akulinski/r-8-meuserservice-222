package com.akulinski.r8meservice.web.rest;

import com.akulinski.r8meservice.security.AuthoritiesConstants;
import com.akulinski.r8meservice.service.FollowerXFollowedService;
import com.akulinski.r8meservice.web.rest.errors.BadRequestAlertException;
import com.akulinski.r8meservice.service.dto.FollowerXFollowedDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.akulinski.r8meservice.domain.FollowerXFollowed}.
 */
@RestController
@RequestMapping("/api")
@PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
public class FollowerXFollowedResource {

    private final Logger log = LoggerFactory.getLogger(FollowerXFollowedResource.class);

    private static final String ENTITY_NAME = "followerXFollowed";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FollowerXFollowedService followerXFollowedService;

    public FollowerXFollowedResource(FollowerXFollowedService followerXFollowedService) {
        this.followerXFollowedService = followerXFollowedService;
    }

    /**
     * {@code POST  /follower-x-followeds} : Create a new followerXFollowed.
     *
     * @param followerXFollowedDTO the followerXFollowedDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new followerXFollowedDTO, or with status {@code 400 (Bad Request)} if the followerXFollowed has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/follower-x-followeds")
    public ResponseEntity<FollowerXFollowedDTO> createFollowerXFollowed(@RequestBody FollowerXFollowedDTO followerXFollowedDTO) throws URISyntaxException {
        log.debug("REST request to save FollowerXFollowed : {}", followerXFollowedDTO);
        if (followerXFollowedDTO.getId() != null) {
            throw new BadRequestAlertException("A new followerXFollowed cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FollowerXFollowedDTO result = followerXFollowedService.save(followerXFollowedDTO);
        return ResponseEntity.created(new URI("/api/follower-x-followeds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /follower-x-followeds} : Updates an existing followerXFollowed.
     *
     * @param followerXFollowedDTO the followerXFollowedDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated followerXFollowedDTO,
     * or with status {@code 400 (Bad Request)} if the followerXFollowedDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the followerXFollowedDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/follower-x-followeds")
    public ResponseEntity<FollowerXFollowedDTO> updateFollowerXFollowed(@RequestBody FollowerXFollowedDTO followerXFollowedDTO) throws URISyntaxException {
        log.debug("REST request to update FollowerXFollowed : {}", followerXFollowedDTO);
        if (followerXFollowedDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FollowerXFollowedDTO result = followerXFollowedService.save(followerXFollowedDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, followerXFollowedDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /follower-x-followeds} : get all the followerXFolloweds.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of followerXFolloweds in body.
     */
    @GetMapping("/follower-x-followeds")
    public List<FollowerXFollowedDTO> getAllFollowerXFolloweds() {
        log.debug("REST request to get all FollowerXFolloweds");
        return followerXFollowedService.findAll();
    }

    /**
     * {@code GET  /follower-x-followeds/:id} : get the "id" followerXFollowed.
     *
     * @param id the id of the followerXFollowedDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the followerXFollowedDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/follower-x-followeds/{id}")
    public ResponseEntity<FollowerXFollowedDTO> getFollowerXFollowed(@PathVariable Long id) {
        log.debug("REST request to get FollowerXFollowed : {}", id);
        Optional<FollowerXFollowedDTO> followerXFollowedDTO = followerXFollowedService.findOne(id);
        return ResponseUtil.wrapOrNotFound(followerXFollowedDTO);
    }

    /**
     * {@code DELETE  /follower-x-followeds/:id} : delete the "id" followerXFollowed.
     *
     * @param id the id of the followerXFollowedDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/follower-x-followeds/{id}")
    public ResponseEntity<Void> deleteFollowerXFollowed(@PathVariable Long id) {
        log.debug("REST request to delete FollowerXFollowed : {}", id);
        followerXFollowedService.delete(id);
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
    public List<FollowerXFollowedDTO> searchFollowerXFolloweds(@RequestParam String query) {
        log.debug("REST request to search FollowerXFolloweds for query {}", query);
        return followerXFollowedService.search(query);
    }
}
