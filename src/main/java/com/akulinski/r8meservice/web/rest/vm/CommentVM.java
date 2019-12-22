package com.akulinski.r8meservice.web.rest.vm;

import java.time.Instant;

public class CommentVM {
    private final String comment;
    private final String commenterLogin;
    private final String commenterLink;
    private final Instant timestamp;

    public CommentVM(String comment, String commenterLogin, String commenterLink, Instant timestamp) {
        this.comment = comment;
        this.commenterLogin = commenterLogin;
        this.commenterLink = commenterLink;
        this.timestamp = timestamp;
    }

    public String getComment() {
        return comment;
    }

    public String getCommenterLogin() {
        return commenterLogin;
    }

    public String getCommenterLink() {
        return commenterLink;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
