package com.akulinski.r8meservice.service.dto;

import lombok.Data;

@Data
public class QuestionCheckDTO {

    private long poster;

    private String question;

    private boolean isRated;
}
