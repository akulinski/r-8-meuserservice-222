package com.akulinski.r8meservice.service;

import com.akulinski.r8meservice.domain.*;
import com.akulinski.r8meservice.repository.UserProfileRepository;
import com.akulinski.r8meservice.repository.UserRepository;
import com.akulinski.r8meservice.repository.search.QuestionSearchRepository;
import com.akulinski.r8meservice.security.SecurityUtils;
import com.akulinski.r8meservice.service.dto.RateDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Rate}.
 */
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class RateService {


    private final QuestionSearchRepository questionSearchRepository;

    private final UserRepository userRepository;

    private final UserProfileRepository userProfileRepository;

    /**
     * Save a rate.
     *
     * @param rateDTO the entity to save.
     * @return the persisted entity.
     */
    public RateDTO save(RateDTO rateDTO) {
        log.debug("Request to save Rate : {}", rateDTO);
        Rate rate = new Rate();
        rate.setValue(rateDTO.getValue());

        var questionById = questionSearchRepository.findById(rateDTO.getQuestionId()).orElseThrow(() -> new IllegalStateException("No question found by id"));

        final UserProfile ratedProfile = getUserProfile(rateDTO.getRated());
        final UserProfile raterProfile = getUserProfile(rateDTO.getRating());

        rate.setPoster(raterProfile.getId());
        rate.setReceiver(ratedProfile.getId());

        rate.setQuestion(questionById.getId());

        questionById.getRates().add(rate);

        return rateDTO;
    }

    /**
     * Get all the rates.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RateDTO> findAll() {
        log.debug("Request to get all Rates");
        return questionSearchRepository.findAllQuestions().stream()
            .map(Question::getRates)
            .flatMap(Collection::stream)
            .map(mapRateToDTOFunction())
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @OwnerCheck
    public void deleteRate(Rate rate) {
        questionSearchRepository.findById(rate.getQuestion()).ifPresent(question -> {
            question.getRates().remove(rate);
            questionSearchRepository.save(question);
        });
    }

    /**
     * Get all the rates for user
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RateDTO> findAll(String useranme) {
        log.debug("Request to get all Rates for user {}", useranme);

        final var user = userRepository.findOneByLogin(useranme).orElseThrow(() -> new IllegalStateException(String.format("No user found by username: %s", useranme)));
        final var profile = userProfileRepository.findByUser(user).orElseThrow(() -> new IllegalStateException(String.format("No profile connected to user %s", user.getId())));

        return questionSearchRepository.findAllByPoster(profile.getId()).stream()
            .map(Question::getRates)
            .flatMap(Collection::stream)
            .map(mapRateToDTOFunction())
            .collect(Collectors.toCollection(LinkedList::new));
    }

    private Function<Rate, RateDTO> mapRateToDTOFunction() {
        return rate -> {
            final var posterProfile = userProfileRepository.findById(rate.getPoster()).orElseThrow(() -> new IllegalStateException(String.format("No user profile for id: %d", rate.getPoster())));
            final var receiverProfile = userProfileRepository.findById(rate.getReceiver()).orElseThrow(() -> new IllegalStateException(String.format("No user profile for id: %d", rate.getReceiver())));
            RateDTO rateDTO = new RateDTO();
            rateDTO.setValue(rate.getValue());
            rateDTO.setTimeStamp(rate.getTimeStamp());
            rateDTO.setRated(receiverProfile.getUser().getLogin());
            rateDTO.setPoster(posterProfile.getUser().getLogin());
            rateDTO.setPosterLink(posterProfile.getUser().getImageUrl());
            return rateDTO;
        };
    }


    /**
     * Calculates average of rates for user
     *
     * @return average of rates
     */
    public Double calcAverage() {
        final UserProfile profile = getUserProfile();
        final List<Question> allByRated = questionSearchRepository.findAllByPoster(profile.getId());

        final Double average = allByRated.stream()
            .map(Question::getRates)
            .flatMap(Collection::stream)
            .mapToDouble(Rate::getValue)
            .average().orElse(-1.0);

        profile.setCurrentRating(average);
        userProfileRepository.save(profile);

        return average;
    }


    /**
     * Calculates average of rates for user
     *
     * @return average of rates
     */
    public Double calcAverage(UserProfile profile) {
        final List<Question> allByRated = questionSearchRepository.findAllByPoster(profile.getId());

        final Double average = allByRated.stream()
            .map(Question::getRates)
            .flatMap(Collection::stream)
            .mapToDouble(Rate::getValue)
            .average().orElse(-1.0);

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
     * @return
     * @Param username
     */
    private UserProfile getUserProfile(String username) {
        final User user = userRepository.findOneByLogin(username).orElseThrow(() -> new IllegalStateException(String.format("No user found with username: %s", username)));
        return userProfileRepository.findByUser(user).orElseThrow(() -> new IllegalStateException(String.format("No profile found for user: %s", username)));
    }

    public List<Rate> getAllQuestionsForRate(String id) {
        return questionSearchRepository.findById(id)
            .orElseThrow(() -> new IllegalStateException(String.format("No question found by id: %s", id)))
            .getRates();
    }
}
