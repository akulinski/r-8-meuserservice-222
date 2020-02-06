package com.akulinski.r8meservice.domain;

import com.akulinski.r8meservice.config.DefaultInstantDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Id;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Document(indexName = "question", type = "question")
@NoArgsConstructor
public class Question implements Serializable, ProtectedResource {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    private String link;

    private String content;

    private Long poster;

    private Instant timeStamp = Instant.now();

    private Double currentRating = -1D;

    private Long ratesCount = 0L;

    public Question(String link, String content, Long poster) {
        this.link = link;
        this.content = content;
        this.poster = poster;
    }

    @Override
    public long getOwner() {
        return poster;
    }
}
