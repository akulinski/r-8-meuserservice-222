package com.akulinski.r8meservice.service;

import com.akulinski.r8meservice.domain.Rate;
import com.akulinski.r8meservice.repository.UserProfileRepository;
import com.akulinski.r8meservice.repository.search.QuestionSearchRepository;
import com.akulinski.r8meservice.repository.search.UserProfileSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@ConditionalOnProperty(value = "scheduling.enabled", havingValue = "true", matchIfMissing = false)
@Slf4j
@RequiredArgsConstructor
public class ProfileCalculationService {

    private final UserProfileRepository userProfileRepository;

    private final UserProfileSearchRepository userProfileSearchRepository;
    private final QuestionSearchRepository questionSearchRepository;


    @RabbitListener(queues = {"${properties.rateQueue}"})
    @Async
    public void handleResponseQuery(Rate rate) {
        log.info("Got data response {}", rate.toString());
        final var byId = userProfileRepository.findById(rate.getReceiver()).orElseThrow(ExceptionUtils.getNoUserFoundExceptionSupplier());

        final var newAvg = (byId.getCurrentRating() + rate.getValue()) / (byId.getRatesCount() + 1);

        byId.setCurrentRating(newAvg);
        byId.setRatesCount(byId.getRatesCount() + 1);
        byId.setLastCalcRun(Instant.now());

        userProfileRepository.save(byId);
        userProfileSearchRepository.save(byId);

        questionSearchRepository.findById(rate.getQuestion()).ifPresent(question -> {
            final var newAvgQuestion = (question.getCurrentRating() + rate.getValue()) / (question.getRatesCount() + 1);
            question.setCurrentRating(newAvgQuestion);
            question.setRatesCount(question.getRatesCount() + 1);
            questionSearchRepository.save(question);
        });
    }
}
