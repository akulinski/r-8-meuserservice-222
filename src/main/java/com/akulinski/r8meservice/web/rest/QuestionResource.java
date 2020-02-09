package com.akulinski.r8meservice.web.rest;

import com.akulinski.r8meservice.domain.Question;
import com.akulinski.r8meservice.security.SecurityUtils;
import com.akulinski.r8meservice.service.*;
import com.akulinski.r8meservice.service.dto.QuestionCheckDTO;
import com.akulinski.r8meservice.web.rest.vm.QuestionVM;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class QuestionResource {

    private final QuestionService questionService;

    private final QuestionCheckingService questionCheckingService;
    private final UserProfileService userProfileService;
    private final QuestionClient questionClient;

    @GetMapping("/question")
    public ResponseEntity<List<QuestionVM>> getAllQuestionsForUser() {

        return ResponseEntity.ok(questionService.getQuestionsForUser().stream()
            .map(this::getQuestionVM).collect(Collectors.toList()));
    }

    public QuestionVM getQuestionVM(Question question) {
        QuestionVM questionVM = new QuestionVM();
        questionVM.setContent(question.getContent());
        questionVM.setLink(question.getLink());
        questionVM.setId(question.getId());
        questionVM.setCurrentRating(question.getCurrentRating());
        questionVM.setRatesCount(question.getRatesCount());
        return questionVM;
    }

    @GetMapping("/question/question-by-username/{username}")
    public ResponseEntity<List<QuestionVM>> getAllQuestionsForAnyUser(@PathVariable("username") String username) {
        final var questionsForUser = questionService.getQuestionsForUser(username);
        long profileIdFromUsername = userProfileService.getProfileIdFromUsername(SecurityUtils.getCurrentUserLogin().orElseThrow(ExceptionUtils.getNoLoginInContextExceptionSupplier()));
        List<QuestionCheckDTO> questionCheckDTOs = new ArrayList<>();

        questionsForUser.forEach(question -> {
            QuestionCheckDTO questionCheckDTO = new QuestionCheckDTO();
            questionCheckDTO.setQuestion(question.getId());
            questionCheckDTO.setPoster(profileIdFromUsername);
            questionCheckDTOs.add(questionCheckDTO);
        });

        final var bulkCheckDTOResponse = questionClient.checkBulk(questionCheckDTOs);

        final var collect = questionsForUser.stream().map(this::getQuestionVM).collect(Collectors.toList());

        bulkCheckDTOResponse.forEach(questionIdUserDTO -> {
            final var questionVM1 = collect.stream().filter(questionVM -> Objects.equals(questionVM.getId(), questionIdUserDTO.getQuestion())).findFirst().orElse(new QuestionVM());
            collect.remove(questionVM1);
            questionVM1.setAlreadyRated(questionIdUserDTO.isRated());
            collect.add(questionVM1);
        });

        return ResponseEntity.ok(collect);
    }

    @DeleteMapping("/question/{id}")
    public ResponseEntity deleteById(@PathVariable("id") String id) {
        questionService.deleteById(id);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/question/add")
    public ResponseEntity addQuestion(@RequestParam("photo") MultipartFile multipartFile, @RequestParam("question") String questionJson) throws URISyntaxException {

        final var question = questionService.createQuestion(questionJson, multipartFile);
        log.debug("question added with id {}", question.getId());

        return ResponseEntity.created(new URI(String.format("/api/question/%s", question.getId()))).build();
    }

    @GetMapping("/question/{id}")
    public ResponseEntity<Question> getQuestion(@PathVariable("id") String id) {
        return ResponseEntity.ok(questionService.getById(id));
    }

}
