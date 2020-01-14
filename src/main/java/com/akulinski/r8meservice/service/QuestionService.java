package com.akulinski.r8meservice.service;

import com.akulinski.r8meservice.domain.Question;
import com.akulinski.r8meservice.repository.UserProfileRepository;
import com.akulinski.r8meservice.repository.UserRepository;
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

    private final UserRepository userRepository;

    public Question createQuestion(QuestionDTO questionDTO, MultipartFile multipartFile) {

        Question question = new Question();

        final var poster = userService.getUserWithAuthorities().orElseThrow(ExceptionUtils.getNoUserFoundExceptionSupplier());
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
        final var posterUser = userService.getUserWithAuthorities().orElseThrow(ExceptionUtils.getNoUserFoundExceptionSupplier());
        final var posterProfile = userProfileRepository.findByUser(posterUser).orElseThrow(ExceptionUtils.getNoProfileConnectedExceptionSupplier(posterUser.getId()));
        return questionSearchRepository.findAllByPoster(posterProfile.getId());
    }

    public List<Question> getQuestionsForUser(String username) {
        final var posterUser = userRepository.findOneByLogin(username).orElseThrow(ExceptionUtils.getNoUserFoundExceptionSupplier(username));
        final var posterProfile = userProfileRepository.findByUser(posterUser).orElseThrow(ExceptionUtils.getNoProfileConnectedExceptionSupplier(posterUser.getId()));
        return questionSearchRepository.findAllByPoster(posterProfile.getId());
    }

    public Question getById(String id) {
        return questionSearchRepository.findById(id)
            .orElseThrow(() -> new IllegalStateException(String.format("No Question found by id: %s", id)));
    }

    public void deleteById(String id) {
        questionSearchRepository.findById(id).ifPresent(questionSearchRepository::delete);
    }
}
