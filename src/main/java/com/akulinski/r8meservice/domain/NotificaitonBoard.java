package com.akulinski.r8meservice.domain;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Id;
import java.time.Instant;
import java.util.List;

@Data
@Document(indexName = "notification-board")
public class NotificaitonBoard {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    private long profileId;

    private Instant timestamp = Instant.now();

    private List<Question> questionList;
}
