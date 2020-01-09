package com.akulinski.r8meservice.service;

import com.akulinski.r8meservice.domain.Question;
import com.akulinski.r8meservice.repository.UserProfileRepository;
import com.akulinski.r8meservice.repository.search.QuestionSearchRepository;
import com.akulinski.r8meservice.service.dto.QuestionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionService {

    private final QuestionSearchRepository questionSearchRepository;

    private final PhotoStorageService photoStorageService;

    private final UserService userService;

    private final UserProfileRepository userProfileRepository;

    public Question createQuestion(QuestionDTO questionDTO, MultipartFile multipartFile) {

        Question question = new Question();

        final var poster = userService.getUserWithAuthorities().orElseThrow(() -> new IllegalStateException("User no found"));
        question.setPoster(poster.getId());
        var savedQuestion = questionSearchRepository.save(question);

        try {
            final var link = photoStorageService.storeQuestionPhoto(poster, savedQuestion.getId(), multipartFile);
            question.setLink(link);
            question.setContent(questionDTO.getContent());
            savedQuestion = questionSearchRepository.save(question);
        } catch (IOException e) {
            log.debug(e.getLocalizedMessage());
            questionSearchRepository.delete(question);
        }

        return savedQuestion;
    }

    public List<Question> getQuestionsForUser() {
        final var posterUser = userService.getUserWithAuthorities().orElseThrow(() -> new IllegalStateException("User no found"));
        final var posterProfile = userProfileRepository.findByUser(posterUser).orElseThrow(() -> new IllegalStateException(String.format("No profile connected to user: %d", posterUser.getId())));
        return questionSearchRepository.findAllByPoster(posterProfile.getId());
    }

    public Question getById(String id) {
        return questionSearchRepository.findById(id)
            .orElseThrow(() -> new IllegalStateException(String.format("No Question found by id: %s", id)));
    }
}
