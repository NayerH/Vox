package com.vox.post.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.lang.NonNull;

import java.util.Date;
import java.util.UUID;


public class MediaFile {
    @Id
    private String id = UUID.randomUUID().toString();

    @Field
    private String title;
    @Field
    private String description;
    @Field
    private String fileType;
    @Field
    private String filePath;
    @Field
    private Long fileSize;

    @JsonFormat(pattern = "yyyy-MM-dd@HH:mm")
    @Field
    private final Date publishedAt = new Date();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NonNull
    public String getFileType() {
        return fileType;
    }

    public void setFileType(@NonNull String fileType) {
        this.fileType = fileType;
    }

    @NonNull
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(@NonNull String filePath) {
        this.filePath = filePath;
    }

    @NonNull
    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(@NonNull Long fileSize) {
        this.fileSize = fileSize;
    }

    public Date getPublishedAt() {
        return publishedAt;
    }

    @Override
    public String toString() {
        return "MediaFile{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", fileType='" + fileType + '\'' +
                ", filePath='" + filePath + '\'' +
                ", fileSize=" + fileSize +
                ", publishedAt=" + publishedAt +
                '}';
    }
}
