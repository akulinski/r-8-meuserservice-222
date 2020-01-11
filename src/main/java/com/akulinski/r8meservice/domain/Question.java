package com.akulinski.r8meservice.domain;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;
import javax.persistence.Index;
import java.io.Serializable;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

@Data
@Document(indexName = "question", type = "question")
public class Question implements Serializable, ProtectedResource {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    private String link;

    private String content;

    private Long poster;

    private Instant timeStamp = Instant.now();

    @Field(includeInParent = true, type = FieldType.Nested)
    private List<Rate> rates;

    public Question() {
        rates = new LinkedList<>();
    }

    public Question(String link, String content, Long poster) {
        this.link = link;
        this.content = content;
        this.poster = poster;
        rates = new LinkedList<>();
    }

    @Override
    public long getOwner() {
        return poster;
    }
}
