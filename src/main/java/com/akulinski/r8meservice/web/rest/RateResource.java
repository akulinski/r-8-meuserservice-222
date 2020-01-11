package com.akulinski.r8meservice.web.rest;

import com.akulinski.r8meservice.domain.Rate;
import com.akulinski.r8meservice.service.RateService;
import com.akulinski.r8meservice.service.dto.RateDTO;
import com.akulinski.r8meservice.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * REST controller for managing {@link com.akulinski.r8meservice.domain.Rate}.
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class RateResource {

    private static final String ENTITY_NAME = "rate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RateService rateService;

    public RateResource(RateService rateService) {
        this.rateService = rateService;
    }

    /**
     * {@code POST  /rates} : Create a new rate.
     *
     * @param rateDTO the rateDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rateDTO, or with status {@code 400 (Bad Request)} if the rate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rates")
    public ResponseEntity<RateDTO> createRate(@RequestBody RateDTO rateDTO) throws URISyntaxException {
        log.debug("REST request to save Rate : {}", rateDTO);
        if (rateDTO.getId() != null) {
            throw new BadRequestAlertException("A new rate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RateDTO result = rateService.save(rateDTO);
        return ResponseEntity.created(new URI("/api/rates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rates} : Updates an existing rate.
     *
     * @param rateDTO the rateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rateDTO,
     * or with status {@code 400 (Bad Request)} if the rateDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rates")
    public ResponseEntity<RateDTO> updateRate(@RequestBody RateDTO rateDTO) throws URISyntaxException {
        log.debug("REST request to update Rate : {}", rateDTO);
        if (rateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RateDTO result = rateService.save(rateDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, rateDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /rates} : get all the rates.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rates in body.
     */
    @GetMapping("/rates")
    public List<RateDTO> getAllRates() {
        log.debug("REST request to get all Rates");
        return rateService.findAll();
    }

    @DeleteMapping("/rates")
    public ResponseEntity deleteRate(@RequestBody Rate rate) {
        rateService.deleteRate(rate);
        return ResponseEntity.accepted().build();
    }

    /**
     * Return rates for user
     *
     * @param username of user that rates are requested for
     * @return List of rates
     */
    @GetMapping("/rates/{username}")
    @ResponseStatus(HttpStatus.OK)
    public List<RateDTO> getAllRatesForUser(@PathVariable("username") String username) {
        return rateService.findAll(username);
    }
}
