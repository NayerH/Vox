package com.vox.post.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mongodb.lang.NonNull;
import jakarta.persistence.Id;
import jakarta.persistence.UniqueConstraint;

import java.util.Date;

public class Comment {
    @Id
    private Long id;

    @NonNull
    private Long userId;

    @NonNull
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd@HH:mm")
    private Date publishedAt = new Date();

}
