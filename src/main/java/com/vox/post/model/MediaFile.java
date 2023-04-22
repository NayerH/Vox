package com.vox.post.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mongodb.lang.NonNull;
import jakarta.persistence.Id;

import java.util.Date;

public class MediaFile {
    @Id
    private Long id;

    @NonNull
    private Long uploaderId;

    private String title;

    private String description;
    @NonNull
    private String fileType;
    @NonNull
    private String filePath;
    @NonNull
    private Long fileSize;

    @JsonFormat(pattern = "yyyy-MM-dd@HH:mm")
    private Date publishedAt = new Date();

}
