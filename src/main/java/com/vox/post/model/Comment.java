package com.vox.post.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mongodb.lang.NonNull;
import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Document
public class Comment {
    @Id
    private String id;

    private String userId;

    @NonNull
    private String content;

    private List<Comment> replies;

    @JsonFormat(pattern = "yyyy-MM-dd@HH:mm")
    private LocalDate publishedAt = LocalDate.now();

    public Comment() {
        this.id = UUID.randomUUID().toString();
    }

    public void addReply(Comment reply) {
        if(this.replies == null)
            this.replies = new ArrayList<>();
        this.replies.add(reply);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    @NonNull
    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", content='" + content + '\'' +
                ", replies=" + replies +
                ", publishedAt=" + publishedAt +
                '}';
    }

    public void setContent(@NonNull String content) {
        this.content = content;
    }

    public List<Comment> getReplies() {
        return replies;
    }

    public void setReplies(List<Comment> replies) {
        this.replies = replies;
    }

    public LocalDate getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDate publishedAt) {
        this.publishedAt = publishedAt;
    }
}
