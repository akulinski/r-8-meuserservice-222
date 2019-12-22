package com.akulinski.r8meservice.service;

import com.akulinski.r8meservice.repository.RateXProfileRepository;
import com.akulinski.r8meservice.repository.UserProfileRepository;
import com.akulinski.r8meservice.repository.search.UserProfileSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileCalculationService {
    private final Logger log = LoggerFactory.getLogger(ProfileCalculationService.class);

    private final UserProfileRepository userProfileRepository;

    private final UserProfileSearchRepository userProfileSearchRepository;

    private final RateXProfileRepository rateXProfileRepository;

    public ProfileCalculationService(UserProfileRepository userProfileRepository, UserProfileSearchRepository userProfileSearchRepository, RateXProfileRepository rateXProfileRepository) {
        this.userProfileRepository = userProfileRepository;
        this.userProfileSearchRepository = userProfileSearchRepository;
        this.rateXProfileRepository = rateXProfileRepository;
    }

    @Scheduled(fixedRateString = "${scheduler.profilecalc}")
    @Transactional
    public void calcProfileAverage(){
        log.info("Running profile scheduler ");

        userProfileRepository.findAllStream().forEach(userProfile -> {

            final var allByRated = rateXProfileRepository.findAllByRated(userProfile);

            final var currentRating = allByRated.stream().mapToDouble(rateXProfile -> rateXProfile.getRate().getValue()).average().orElseGet(() -> -1.0);

            userProfile.setCurrentRating(currentRating);
            userProfileRepository.save(userProfile);
            userProfileSearchRepository.save(userProfile);
        });

        log.info("Profile scheduler finished");
    }
}
