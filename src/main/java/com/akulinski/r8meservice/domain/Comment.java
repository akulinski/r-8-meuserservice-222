package com.akulinski.r8meservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Id;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Comment.
 */
@Document(indexName = "comment", type = "comment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    private String comment;

    private Instant timeStamp = Instant.now();

    private long poster;

    private long receiver;
}
