package com.akulinski.r8meservice.web.rest.vm;

import lombok.Data;

import java.io.Serializable;

@Data
public class QuestionVM implements Serializable {

    private String id;

    private String link;

    private String content;

    private Double currentRating;

    private Long ratesCount;
}
