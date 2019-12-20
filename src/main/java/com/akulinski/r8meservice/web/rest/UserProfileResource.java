package com.akulinski.r8meservice.web.rest;

import com.akulinski.r8meservice.domain.UserProfile;
import com.akulinski.r8meservice.repository.UserProfileRepository;
import com.akulinski.r8meservice.repository.search.UserProfileSearchRepository;
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
 * REST controller for managing {@link com.akulinski.r8meservice.domain.UserProfile}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class UserProfileResource {

    private final Logger log = LoggerFactory.getLogger(UserProfileResource.class);

    private static final String ENTITY_NAME = "userProfile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserProfileRepository userProfileRepository;

    private final UserProfileSearchRepository userProfileSearchRepository;

    public UserProfileResource(UserProfileRepository userProfileRepository, UserProfileSearchRepository userProfileSearchRepository) {
        this.userProfileRepository = userProfileRepository;
        this.userProfileSearchRepository = userProfileSearchRepository;
    }

    /**
     * {@code POST  /user-profiles} : Create a new userProfile.
     *
     * @param userProfile the userProfile to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userProfile, or with status {@code 400 (Bad Request)} if the userProfile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-profiles")
    public ResponseEntity<UserProfile> createUserProfile(@RequestBody UserProfile userProfile) throws URISyntaxException {
        log.debug("REST request to save UserProfile : {}", userProfile);
        if (userProfile.getId() != null) {
            throw new BadRequestAlertException("A new userProfile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserProfile result = userProfileRepository.save(userProfile);
        userProfileSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/user-profiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-profiles} : Updates an existing userProfile.
     *
     * @param userProfile the userProfile to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userProfile,
     * or with status {@code 400 (Bad Request)} if the userProfile is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userProfile couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-profiles")
    public ResponseEntity<UserProfile> updateUserProfile(@RequestBody UserProfile userProfile) throws URISyntaxException {
        log.debug("REST request to update UserProfile : {}", userProfile);
        if (userProfile.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        UserProfile result = userProfileRepository.save(userProfile);
        userProfileSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userProfile.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /user-profiles} : get all the userProfiles.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userProfiles in body.
     */
    @GetMapping("/user-profiles")
    public List<UserProfile> getAllUserProfiles() {
        log.debug("REST request to get all UserProfiles");
        return userProfileRepository.findAll();
    }

    /**
     * {@code GET  /user-profiles/:id} : get the "id" userProfile.
     *
     * @param id the id of the userProfile to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userProfile, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-profiles/{id}")
    public ResponseEntity<UserProfile> getUserProfile(@PathVariable Long id) {
        log.debug("REST request to get UserProfile : {}", id);
        Optional<UserProfile> userProfile = userProfileRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(userProfile);
    }

    /**
     * {@code DELETE  /user-profiles/:id} : delete the "id" userProfile.
     *
     * @param id the id of the userProfile to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-profiles/{id}")
    public ResponseEntity<Void> deleteUserProfile(@PathVariable Long id) {
        log.debug("REST request to delete UserProfile : {}", id);
        userProfileRepository.deleteById(id);
        userProfileSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/user-profiles?query=:query} : search for the userProfile corresponding
     * to the query.
     *
     * @param query the query of the userProfile search.
     * @return the result of the search.
     */
    @GetMapping("/_search/user-profiles")
    public List<UserProfile> searchUserProfiles(@RequestParam String query) {
        log.debug("REST request to search UserProfiles for query {}", query);
        return StreamSupport
            .stream(userProfileSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
