package com.akulinski.r8meservice.service.dto;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class BulkCheckDTO {
    private Long user;
    List<QuestionIdUserDTO> questionIdUserDTOS;

    public BulkCheckDTO() {
        questionIdUserDTOS = new LinkedList<>();
    }
}
