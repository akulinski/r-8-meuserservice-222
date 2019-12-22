package com.akulinski.r8meservice.service;

import com.akulinski.r8meservice.domain.RateXProfile;
import com.akulinski.r8meservice.repository.RateXProfileRepository;
import com.akulinski.r8meservice.repository.search.RateXProfileSearchRepository;
import com.akulinski.r8meservice.service.dto.RateXProfileDTO;
import com.akulinski.r8meservice.service.mapper.RateXProfileMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link RateXProfile}.
 */
@Service
@Transactional
public class RateXProfileService {

    private final Logger log = LoggerFactory.getLogger(RateXProfileService.class);

    private final RateXProfileRepository rateXProfileRepository;

    private final RateXProfileMapper rateXProfileMapper;

    private final RateXProfileSearchRepository rateXProfileSearchRepository;

    public RateXProfileService(RateXProfileRepository rateXProfileRepository, RateXProfileMapper rateXProfileMapper, RateXProfileSearchRepository rateXProfileSearchRepository) {
        this.rateXProfileRepository = rateXProfileRepository;
        this.rateXProfileMapper = rateXProfileMapper;
        this.rateXProfileSearchRepository = rateXProfileSearchRepository;
    }

    /**
     * Save a rateXProfile.
     *
     * @param rateXProfileDTO the entity to save.
     * @return the persisted entity.
     */
    public RateXProfileDTO save(RateXProfileDTO rateXProfileDTO) {
        log.debug("Request to save RateXProfile : {}", rateXProfileDTO);
        RateXProfile rateXProfile = rateXProfileMapper.toEntity(rateXProfileDTO);
        rateXProfile = rateXProfileRepository.save(rateXProfile);
        RateXProfileDTO result = rateXProfileMapper.toDto(rateXProfile);
        rateXProfileSearchRepository.save(rateXProfile);
        return result;
    }

    /**
     * Get all the rateXProfiles.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RateXProfileDTO> findAll() {
        log.debug("Request to get all RateXProfiles");
        return rateXProfileRepository.findAll().stream()
            .map(rateXProfileMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one rateXProfile by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RateXProfileDTO> findOne(Long id) {
        log.debug("Request to get RateXProfile : {}", id);
        return rateXProfileRepository.findById(id)
            .map(rateXProfileMapper::toDto);
    }

    /**
     * Delete the rateXProfile by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RateXProfile : {}", id);
        rateXProfileRepository.deleteById(id);
        rateXProfileSearchRepository.deleteById(id);
    }

    /**
     * Search for the rateXProfile corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RateXProfileDTO> search(String query) {
        log.debug("Request to search RateXProfiles for query {}", query);
        return StreamSupport
            .stream(rateXProfileSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(rateXProfileMapper::toDto)
            .collect(Collectors.toList());
    }
}
