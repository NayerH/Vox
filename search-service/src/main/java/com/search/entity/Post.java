package com.search.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.search.Indices;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.util.List;


@Document(indexName = Indices.POST_INDEX)
@Setting(settingPath = "static/es-settings.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Post implements Persistable<String> {

    @Id
    @Field(type = FieldType.Keyword)
    private String id;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String content;

    @Field(type = FieldType.Text)
    private String mongoId;

//    @Field(type = FieldType.Nested, includeInParent = true)
//    private List<Tag> tags;

    @Field(type = FieldType.Text)
    private String tag;
    @Field(type = FieldType.Long)
    private Long views;

    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return false;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMongoId() {
        return mongoId;
    }

    public void setMongoId(String mongoId) {
        this.mongoId = mongoId;
    }

//    public List<Tag> getTags() {
//        return tags;
//    }

//    public void setTags(List<Tag> tags) {
//        this.tags = tags;
//    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Long getViews() {
        return views;
    }

    public void setViews(Long views) {
        this.views = views;
    }
}