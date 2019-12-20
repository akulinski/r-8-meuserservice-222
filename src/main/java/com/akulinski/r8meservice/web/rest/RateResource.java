package com.akulinski.r8meservice.web.rest;

import com.akulinski.r8meservice.domain.Rate;
import com.akulinski.r8meservice.repository.RateRepository;
import com.akulinski.r8meservice.repository.search.RateSearchRepository;
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
 * REST controller for managing {@link com.akulinski.r8meservice.domain.Rate}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RateResource {

    private final Logger log = LoggerFactory.getLogger(RateResource.class);

    private static final String ENTITY_NAME = "rate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RateRepository rateRepository;

    private final RateSearchRepository rateSearchRepository;

    public RateResource(RateRepository rateRepository, RateSearchRepository rateSearchRepository) {
        this.rateRepository = rateRepository;
        this.rateSearchRepository = rateSearchRepository;
    }

    /**
     * {@code POST  /rates} : Create a new rate.
     *
     * @param rate the rate to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rate, or with status {@code 400 (Bad Request)} if the rate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rates")
    public ResponseEntity<Rate> createRate(@RequestBody Rate rate) throws URISyntaxException {
        log.debug("REST request to save Rate : {}", rate);
        if (rate.getId() != null) {
            throw new BadRequestAlertException("A new rate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Rate result = rateRepository.save(rate);
        rateSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/rates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rates} : Updates an existing rate.
     *
     * @param rate the rate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rate,
     * or with status {@code 400 (Bad Request)} if the rate is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rates")
    public ResponseEntity<Rate> updateRate(@RequestBody Rate rate) throws URISyntaxException {
        log.debug("REST request to update Rate : {}", rate);
        if (rate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Rate result = rateRepository.save(rate);
        rateSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, rate.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /rates} : get all the rates.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rates in body.
     */
    @GetMapping("/rates")
    public List<Rate> getAllRates() {
        log.debug("REST request to get all Rates");
        return rateRepository.findAll();
    }

    /**
     * {@code GET  /rates/:id} : get the "id" rate.
     *
     * @param id the id of the rate to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rate, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rates/{id}")
    public ResponseEntity<Rate> getRate(@PathVariable Long id) {
        log.debug("REST request to get Rate : {}", id);
        Optional<Rate> rate = rateRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(rate);
    }

    /**
     * {@code DELETE  /rates/:id} : delete the "id" rate.
     *
     * @param id the id of the rate to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rates/{id}")
    public ResponseEntity<Void> deleteRate(@PathVariable Long id) {
        log.debug("REST request to delete Rate : {}", id);
        rateRepository.deleteById(id);
        rateSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/rates?query=:query} : search for the rate corresponding
     * to the query.
     *
     * @param query the query of the rate search.
     * @return the result of the search.
     */
    @GetMapping("/_search/rates")
    public List<Rate> searchRates(@RequestParam String query) {
        log.debug("REST request to search Rates for query {}", query);
        return StreamSupport
            .stream(rateSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
