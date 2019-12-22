package com.akulinski.r8meservice.service;

import com.akulinski.r8meservice.domain.Rate;
import com.akulinski.r8meservice.domain.RateXProfile;
import com.akulinski.r8meservice.domain.User;
import com.akulinski.r8meservice.domain.UserProfile;
import com.akulinski.r8meservice.repository.RateRepository;
import com.akulinski.r8meservice.repository.RateXProfileRepository;
import com.akulinski.r8meservice.repository.UserProfileRepository;
import com.akulinski.r8meservice.repository.UserRepository;
import com.akulinski.r8meservice.repository.search.RateSearchRepository;
import com.akulinski.r8meservice.repository.search.RateXProfileSearchRepository;
import com.akulinski.r8meservice.security.SecurityUtils;
import com.akulinski.r8meservice.service.dto.RateDTO;
import com.akulinski.r8meservice.service.mapper.RateMapper;
import org.apache.kafka.common.errors.AuthenticationException;
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
 * Service Implementation for managing {@link Rate}.
 */
@Service
@Transactional
public class RateService {

    private final Logger log = LoggerFactory.getLogger(RateService.class);

    private final RateRepository rateRepository;

    private final RateMapper rateMapper;

    private final RateSearchRepository rateSearchRepository;

    private final UserRepository userRepository;

    private final UserProfileRepository userProfileRepository;

    private final RateXProfileRepository rateXProfileRepository;

    private final RateXProfileSearchRepository rateXProfileSearchRepository;

    public RateService(RateRepository rateRepository, RateMapper rateMapper, RateSearchRepository rateSearchRepository, UserRepository userRepository, UserProfileRepository userProfileRepository, RateXProfileRepository rateXProfileRepository, RateXProfileSearchRepository rateXProfileSearchRepository) {
        this.rateRepository = rateRepository;
        this.rateMapper = rateMapper;
        this.rateSearchRepository = rateSearchRepository;
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.rateXProfileRepository = rateXProfileRepository;
        this.rateXProfileSearchRepository = rateXProfileSearchRepository;
    }

    /**
     * Save a rate.
     *
     * @param rateDTO the entity to save.
     * @return the persisted entity.
     */
    public RateDTO save(RateDTO rateDTO) {
        log.debug("Request to save Rate : {}", rateDTO);
        Rate rate = rateMapper.toEntity(rateDTO);
        rate = rateRepository.save(rate);
        RateDTO result = rateMapper.toDto(rate);
        rateSearchRepository.save(rate);

        final UserProfile ratedProfile = getUserProfile(rateDTO.getRated());
        final UserProfile raterProfile = getUserProfile(rateDTO.getRating());

        final RateXProfile rateXProfile = new RateXProfile();

        rateXProfile.setRate(rate);
        rateXProfile.setRater(raterProfile);
        rateXProfile.setRated(ratedProfile);

        rateXProfileRepository.save(rateXProfile);
        rateXProfileSearchRepository.save(rateXProfile);

        return result;
    }

    /**
     * Get all the rates.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RateDTO> findAll() {
        log.debug("Request to get all Rates");
        return rateRepository.findAll().stream()
            .map(rateMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one rate by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RateDTO> findOne(Long id) {
        log.debug("Request to get Rate : {}", id);
        return rateRepository.findById(id)
            .map(rateMapper::toDto);
    }

    /**
     * Delete the rate by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Rate : {}", id);
        final var byId = rateRepository.findById(id).orElseThrow(()->new IllegalArgumentException("Rate not found by id: "+id));
        rateXProfileRepository.deleteAllByRate(byId);
        rateXProfileSearchRepository.deleteAllByRate(byId);

        rateRepository.deleteById(id);
        rateSearchRepository.deleteById(id);
    }

    /**
     * Search for the rate corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RateDTO> search(String query) {
        log.debug("Request to search Rates for query {}", query);
        return StreamSupport
            .stream(rateSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(rateMapper::toDto)
            .collect(Collectors.toList());
    }


    /**
     * Calculates average of rates for user
     *
     * @return average of rates
     */
    public Double calcAverage(){
        final UserProfile profile = getUserProfile();
        final List<RateXProfile> allByRated = rateXProfileRepository.findAllByRated(profile);
        final Double average = allByRated.stream().mapToDouble(rate -> rate.getRate().getValue()).average().orElseGet(() -> -1.0);
        profile.setCurrentRating(average);
        userProfileRepository.save(profile);

        return average;
    }

    /**
     * Returns user profile based on spring security context
     *
     * @return
     */
    private UserProfile getUserProfile() {
        final String username = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AuthenticationException("User not logged in"));
        final User user = userRepository.findOneByLogin(username).orElseThrow(() -> new IllegalStateException(String.format("No user found with username: %s", username)));
        return userProfileRepository.findByUser(user).orElseThrow(() -> new IllegalStateException(String.format("No profile found for user: %s", username)));
    }

    /**
     * Returns user profile based on passed username
     *
     * @Param username
     * @return
     */
    private UserProfile getUserProfile(String username) {
        final User user = userRepository.findOneByLogin(username).orElseThrow(() -> new IllegalStateException(String.format("No user found with username: %s", username)));
        return userProfileRepository.findByUser(user).orElseThrow(() -> new IllegalStateException(String.format("No profile found for user: %s", username)));
    }
}
