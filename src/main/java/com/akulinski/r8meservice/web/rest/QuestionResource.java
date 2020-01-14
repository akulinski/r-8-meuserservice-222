package com.akulinski.r8meservice.web.rest;

import com.akulinski.r8meservice.domain.Question;
import com.akulinski.r8meservice.domain.Rate;
import com.akulinski.r8meservice.service.QuestionService;
import com.akulinski.r8meservice.service.RateService;
import com.akulinski.r8meservice.service.dto.QuestionDTO;
import com.akulinski.r8meservice.web.rest.vm.QuestionVM;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class QuestionResource {

    private final QuestionService questionService;

    private final RateService rateService;

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
        return questionVM;
    }

    @GetMapping("/question/question-by-username/{username}")
    public ResponseEntity<List<QuestionVM>> getAllQuestionsForAnyUser(@PathVariable("username") String username) {
        return ResponseEntity.ok(questionService.getQuestionsForUser(username).stream()
            .map(this::getQuestionVM).collect(Collectors.toList()));
    }

    @DeleteMapping("/question/{id}")
    public ResponseEntity deleteById(@PathVariable("id") String id) {
        questionService.deleteById(id);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/question/add")
    public ResponseEntity addQuestion(@RequestParam("photo") MultipartFile multipartFile, @RequestParam("question") QuestionDTO questionDTO) throws URISyntaxException {

        final var question = questionService.createQuestion(questionDTO, multipartFile);
        log.debug("question added with id {}", question.getId());

        return ResponseEntity.created(new URI(String.format("/api/question/%s", question.getId()))).build();
    }

    @GetMapping("/question/{id}")
    public ResponseEntity<Question> getQuestion(@PathVariable("id") String id) {
        return ResponseEntity.ok(questionService.getById(id));
    }

    @GetMapping("/question/{id}/rates")
    public ResponseEntity<List<Rate>> getRatesForQuestion(@PathVariable("id") String id) {
        return ResponseEntity.ok(rateService.getAllQuestionsForRate(id));
    }
}
