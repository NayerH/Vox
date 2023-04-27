package com.vox.post.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.lang.NonNull;

import java.util.Date;
import java.util.UUID;


@Document
public class MediaFile {
    @Id
    private String id;

    @NonNull
    @Field
    private Long uploaderId;

    @Field
    private String title;
    @Field
    private String description;
    @NonNull
    @Field
    private String fileType;
    @NonNull
    @Field
    private String filePath;
    @NonNull
    @Field
    private Long fileSize;

    @JsonFormat(pattern = "yyyy-MM-dd@HH:mm")
    @Field
    private final Date publishedAt = new Date();

    public MediaFile(@NonNull Long uploaderId, String title, String description, @NonNull String fileType, @NonNull String filePath, @NonNull Long fileSize) {
        this.uploaderId = uploaderId;
        this.title = title;
        this.description = description;
        this.fileType = fileType;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }

    public MediaFile() {
    }

    public MediaFile(String id, @NonNull Long uploaderId, String title, String description, @NonNull String fileType, @NonNull String filePath, @NonNull Long fileSize) {
        this.id = id;
        this.uploaderId = uploaderId;
        this.title = title;
        this.description = description;
        this.fileType = fileType;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }

    public MediaFile getMediaFile() {
        return new MediaFile(UUID.randomUUID().toString(), uploaderId, title, description, fileType, filePath, fileSize);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "MediaFile{" +
                "id='" + id + '\'' +
                ", uploaderId=" + uploaderId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", fileType='" + fileType + '\'' +
                ", filePath='" + filePath + '\'' +
                ", fileSize=" + fileSize +
                ", publishedAt=" + publishedAt +
                '}';
    }
}
