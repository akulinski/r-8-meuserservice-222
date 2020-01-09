package com.akulinski.r8meservice.web.rest;

import com.akulinski.r8meservice.domain.Question;
import com.akulinski.r8meservice.domain.Rate;
import com.akulinski.r8meservice.service.QuestionService;
import com.akulinski.r8meservice.service.RateService;
import com.akulinski.r8meservice.service.dto.QuestionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class QuestionResource {

    private final QuestionService questionService;

    private final RateService rateService;

    @GetMapping("/question")
    public ResponseEntity<List<Question>> getAllQuestionsForUser() {
        return ResponseEntity.ok(questionService.getQuestionsForUser());
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
