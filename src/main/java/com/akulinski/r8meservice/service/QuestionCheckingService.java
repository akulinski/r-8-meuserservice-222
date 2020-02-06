package com.akulinski.r8meservice.service;

import com.akulinski.r8meservice.domain.Question;
import com.akulinski.r8meservice.domain.UserProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionCheckingService {

    private final QuestionClient questionClient;

    public boolean userRatedQuestion(Question question, UserProfile userProfile) {
        final var ratesForQuestionAndPoster = questionClient.getRatesForQuestionAndPoster(question.getId(), userProfile.getId());

        if (ratesForQuestionAndPoster.isEmpty()) {
            return false;
        }

        return true;
    }
}
