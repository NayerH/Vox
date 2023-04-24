package com.vox.post.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mongodb.lang.NonNull;
import jakarta.persistence.Id;

import java.util.Date;
import java.util.List;

public class Comment {
    @Id
    private Long id;

    @NonNull
    private Long userId;

    @NonNull
    private String content;


    private List<Comment> replies;

    @JsonFormat(pattern = "yyyy-MM-dd@HH:mm")
    private Date publishedAt = new Date();

}
