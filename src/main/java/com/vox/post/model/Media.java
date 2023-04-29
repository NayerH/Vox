package com.vox.post.model;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Document(collection = "Media")
public class Media {

    @MongoId
    private ObjectId id;

    private List<MediaFile> mediaFiles;

    private String uploaderId;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public List<MediaFile> getMediaFiles() {
        return mediaFiles;
    }

    public void setMediaFiles(List<MediaFile> mediaFiles) {
        this.mediaFiles = mediaFiles;
    }

    public String getUploaderId() {
        return uploaderId;
    }

    public void setUploaderId(String uploaderId) {
        this.uploaderId = uploaderId;
    }

    public Media(List<MediaFile> mediaFiles, String uploaderId) {
        this.mediaFiles = mediaFiles;
        this.uploaderId = uploaderId;
    }

    @Override
    public String toString() {
        return "Media{" +
                "id=" + id +
                ", mediaFiles=" + mediaFiles +
                '}';
    }
}
