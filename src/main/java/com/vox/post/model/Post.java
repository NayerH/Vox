package com.vox.post.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mongodb.lang.NonNull;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;
import java.util.List;

@Document(collection = "Post")
public class Post {
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

    private List<MediaFile> mediaFiles;
    @DBRef
    private List<Comment> comments;

}
