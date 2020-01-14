package com.akulinski.r8meservice.web.rest.vm;

import lombok.Data;

import java.time.Instant;

@Data
public class QuestionNotificationVM {
    private String username;
    private String content;
    private Instant timestamp;
}
