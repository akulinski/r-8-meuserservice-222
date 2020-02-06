package com.akulinski.r8meservice.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuestionIdUserDTO {
    private String question;
    private Long poster;
    private Boolean rated = false;
}
