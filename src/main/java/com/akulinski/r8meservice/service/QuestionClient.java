package com.akulinski.r8meservice.service;

import com.akulinski.r8meservice.domain.Rate;
import com.akulinski.r8meservice.service.dto.QuestionCheckDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "ratestorageservice2")
public interface QuestionClient {

    @GetMapping("/api/v1/rate/{questionId}/{posterId}")
    List<Rate> getRatesForQuestionAndPoster(@PathVariable("questionId") String questionId, @PathVariable("posterId") long posterId);

    @PostMapping("/api/v1/check-question")
    List<QuestionCheckDTO> checkBulk(@RequestBody List<QuestionCheckDTO> questionCheckDTOS);
}


