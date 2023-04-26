package com.vox.post.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mongodb.lang.NonNull;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Document(collection = "Post")
public class Post implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @MongoId
    private ObjectId id;

    private Long views = 0L;
    @NonNull
    private String title;
    @NonNull
    private String content;

    @NonNull
    private Long authorId;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd@HH:mm")
    private Date publishedAt = new Date();

    private List<String> tags;

    @NonNull
    private Category.CategoryEnum category;

    private List<Long> mediaFiles;

    private List<Comment> comments;


    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Long getViews() {
        return views;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @NonNull
    public String getContent() {
        return content;
    }

    public void setContent(@NonNull String content) {
        this.content = content;
    }

    @NonNull
    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(@NonNull Long authorId) {
        this.authorId = authorId;
    }

    public Date getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Date publishedAt) {
        this.publishedAt = publishedAt;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @NonNull
    public Category.CategoryEnum getCategory() {
        return category;
    }

    public void setCategory(@NonNull Category.CategoryEnum category) {
        this.category = category;
    }

    public List<Long> getMediaFiles() {
        return mediaFiles;
    }

    public void setMediaFiles(List<Long> mediaFiles) {
        this.mediaFiles = mediaFiles;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", views=" + views +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", authorId=" + authorId +
                ", publishedAt=" + publishedAt +
                ", tags=" + tags +
                ", category=" + category +
                ", mediaFiles=" + mediaFiles +
                ", comments=" + comments +
                '}';
    }

}
