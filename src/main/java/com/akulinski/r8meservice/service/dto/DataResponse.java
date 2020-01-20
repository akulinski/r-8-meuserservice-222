package com.akulinski.r8meservice.service.dto;

import com.akulinski.r8meservice.domain.Rate;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Data
public class DataResponse implements Serializable {
    private List<Rate> ratesList;
    private String questionId;
    private Instant timestamp;
}
