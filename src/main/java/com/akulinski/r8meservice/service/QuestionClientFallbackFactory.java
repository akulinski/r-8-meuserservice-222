package com.akulinski.r8meservice.service;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
/*
@Component
@Slf4j
class QuestionClientFallbackFactory implements FallbackFactory<QuestionClient> {
    @Override
    public QuestionClient create(Throwable cause) {
        log.error(cause.getMessage());
        return (questionId, posterId) -> List.of();
    }
}*/
