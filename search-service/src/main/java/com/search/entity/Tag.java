package com.search.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.search.Indices;
import org.springframework.data.domain.Persistable;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

@Document(indexName = Indices.TAG_INDEX)
@Setting(settingPath = "static/es-settings.json")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Tag  implements Persistable<String> {


    @Field(type = FieldType.Text)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getId() {
        return null;
    }

    public Tag(String name){
        this.setName(name);
    }
    @Override
    public boolean isNew() {
        return false;
    }
}
