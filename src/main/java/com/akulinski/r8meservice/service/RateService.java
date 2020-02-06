package com.akulinski.r8meservice.service;

import com.akulinski.r8meservice.config.RoutingKey;
import com.akulinski.r8meservice.domain.Rate;
import com.akulinski.r8meservice.domain.User;
import com.akulinski.r8meservice.domain.UserProfile;
import com.akulinski.r8meservice.repository.UserProfileRepository;
import com.akulinski.r8meservice.repository.UserRepository;
import com.akulinski.r8meservice.repository.search.QuestionSearchRepository;
import com.akulinski.r8meservice.service.dto.RateDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;

/**
 * Service Implementation for managing {@link Rate}.
 */
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class RateService {

    @Value("${properties.exchange}")
    private String exchange;

    private final QuestionSearchRepository questionSearchRepository;

    private final UserRepository userRepository;

    private final UserProfileRepository userProfileRepository;

    private final RabbitTemplate rabbitTemplate;

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
        final UserProfile raterProfile = getUserProfile(rateDTO.getPoster());

        rate.setPoster(raterProfile.getId());
        rate.setReceiver(ratedProfile.getId());

        rate.setQuestion(questionById.getId());

        rabbitTemplate.convertAndSend(exchange, RoutingKey.ANY.toString(), rate);

        return rateDTO;
    }

    private Function<Rate, RateDTO> mapRateToDTOFunction() {
        return rate -> {
            final var posterProfile = userProfileRepository.findById(rate.getPoster()).orElseThrow(ExceptionUtils.getNoProfileConnectedExceptionSupplier(rate.getPoster()));
            final var receiverProfile = userProfileRepository.findById(rate.getReceiver()).orElseThrow(ExceptionUtils.getNoProfileConnectedExceptionSupplier(rate.getReceiver()));
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
     * Returns user profile based on passed username
     *
     * @return
     * @Param username
     */
    private UserProfile getUserProfile(String username) {
        final User user = userRepository.findOneByLogin(username).orElseThrow(ExceptionUtils.getNoUserFoundExceptionSupplier(username));
        return userProfileRepository.findByUser(user).orElseThrow(ExceptionUtils.getNoProfileConnectedExceptionSupplier(user.getId()));
    }

}
