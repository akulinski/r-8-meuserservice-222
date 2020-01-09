package com.akulinski.r8meservice.service;

import com.akulinski.r8meservice.repository.UserProfileRepository;
import com.akulinski.r8meservice.repository.search.UserProfileSearchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@ConditionalOnProperty(value = "scheduling.enabled", havingValue = "true", matchIfMissing = false)
@Slf4j
public class ProfileCalculationService {

    private final UserProfileRepository userProfileRepository;

    private final UserProfileSearchRepository userProfileSearchRepository;

    private final RateService rateService;

    public ProfileCalculationService(UserProfileRepository userProfileRepository, UserProfileSearchRepository userProfileSearchRepository, RateService rateService) {
        this.userProfileRepository = userProfileRepository;
        this.userProfileSearchRepository = userProfileSearchRepository;
        this.rateService = rateService;
    }

    @Scheduled(fixedRateString = "${scheduler.profilecalc}")
    @Transactional
    public void calcProfileAverage() {
        long startTime = System.currentTimeMillis();

        log.info("Running profile scheduler ");

        userProfileRepository.findAllStream().forEach(userProfile -> {

            final var currentRating = rateService.calcAverage(userProfile);

            userProfile.setCurrentRating(currentRating);
            userProfileRepository.save(userProfile);
            userProfileSearchRepository.save(userProfile);
        });
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;

        log.info("Profile scheduler finished");
        log.info("Profile scheduler took: {}", elapsedTime);
    }
}
