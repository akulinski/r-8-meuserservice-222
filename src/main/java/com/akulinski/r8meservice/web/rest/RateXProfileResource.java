package com.akulinski.r8meservice.web.rest;

import com.akulinski.r8meservice.security.AuthoritiesConstants;
import com.akulinski.r8meservice.service.RateXProfileService;
import com.akulinski.r8meservice.web.rest.errors.BadRequestAlertException;
import com.akulinski.r8meservice.service.dto.RateXProfileDTO;

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
 * REST controller for managing {@link com.akulinski.r8meservice.domain.RateXProfile}.
 */
@RestController
@RequestMapping("/api")
@PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
public class RateXProfileResource {

    private final Logger log = LoggerFactory.getLogger(RateXProfileResource.class);

    private static final String ENTITY_NAME = "rateXProfile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RateXProfileService rateXProfileService;

    public RateXProfileResource(RateXProfileService rateXProfileService) {
        this.rateXProfileService = rateXProfileService;
    }

    /**
     * {@code POST  /rate-x-profiles} : Create a new rateXProfile.
     *
     * @param rateXProfileDTO the rateXProfileDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rateXProfileDTO, or with status {@code 400 (Bad Request)} if the rateXProfile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rate-x-profiles")
    public ResponseEntity<RateXProfileDTO> createRateXProfile(@RequestBody RateXProfileDTO rateXProfileDTO) throws URISyntaxException {
        log.debug("REST request to save RateXProfile : {}", rateXProfileDTO);
        if (rateXProfileDTO.getId() != null) {
            throw new BadRequestAlertException("A new rateXProfile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RateXProfileDTO result = rateXProfileService.save(rateXProfileDTO);
        return ResponseEntity.created(new URI("/api/rate-x-profiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rate-x-profiles} : Updates an existing rateXProfile.
     *
     * @param rateXProfileDTO the rateXProfileDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rateXProfileDTO,
     * or with status {@code 400 (Bad Request)} if the rateXProfileDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rateXProfileDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rate-x-profiles")
    public ResponseEntity<RateXProfileDTO> updateRateXProfile(@RequestBody RateXProfileDTO rateXProfileDTO) throws URISyntaxException {
        log.debug("REST request to update RateXProfile : {}", rateXProfileDTO);
        if (rateXProfileDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RateXProfileDTO result = rateXProfileService.save(rateXProfileDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, rateXProfileDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /rate-x-profiles} : get all the rateXProfiles.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rateXProfiles in body.
     */
    @GetMapping("/rate-x-profiles")
    public List<RateXProfileDTO> getAllRateXProfiles() {
        log.debug("REST request to get all RateXProfiles");
        return rateXProfileService.findAll();
    }

    /**
     * {@code GET  /rate-x-profiles/:id} : get the "id" rateXProfile.
     *
     * @param id the id of the rateXProfileDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rateXProfileDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rate-x-profiles/{id}")
    public ResponseEntity<RateXProfileDTO> getRateXProfile(@PathVariable Long id) {
        log.debug("REST request to get RateXProfile : {}", id);
        Optional<RateXProfileDTO> rateXProfileDTO = rateXProfileService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rateXProfileDTO);
    }

    /**
     * {@code DELETE  /rate-x-profiles/:id} : delete the "id" rateXProfile.
     *
     * @param id the id of the rateXProfileDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rate-x-profiles/{id}")
    public ResponseEntity<Void> deleteRateXProfile(@PathVariable Long id) {
        log.debug("REST request to delete RateXProfile : {}", id);
        rateXProfileService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/rate-x-profiles?query=:query} : search for the rateXProfile corresponding
     * to the query.
     *
     * @param query the query of the rateXProfile search.
     * @return the result of the search.
     */
    @GetMapping("/_search/rate-x-profiles")
    public List<RateXProfileDTO> searchRateXProfiles(@RequestParam String query) {
        log.debug("REST request to search RateXProfiles for query {}", query);
        return rateXProfileService.search(query);
    }
}
