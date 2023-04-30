package com.vox.post.config.mongo;

import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@Configuration
public class MultipleMongoConfig {
    @Primary
    @Bean(name = "postMongoProperties")
    @ConfigurationProperties(prefix = "mongodb.post")
    public MongoProperties getPostsDbProps() {
        return new MongoProperties();
    }

    @Bean(name = "mediaMongoProperties")
    @ConfigurationProperties(prefix = "mongodb.media")
    public MongoProperties getMediaDbProps() {
        return new MongoProperties();
    }

    @Primary
    @Bean(name = "postMongoTemplate")
    public MongoTemplate postsDbMongoTemplate() {
        return new MongoTemplate(postsMongoDatabaseFactory(getPostsDbProps()));
    }

    private MongoDatabaseFactory postsMongoDatabaseFactory(MongoProperties postsDbProps) {
        return new SimpleMongoClientDatabaseFactory(postsDbProps.getUri());
    }

    @Bean(name = "mediaMongoTemplate")
    public MongoTemplate mediaDbMongoTemplate() {
        return new MongoTemplate(mediaMongoDatabaseFactory(getMediaDbProps()));
    }

    private MongoDatabaseFactory mediaMongoDatabaseFactory(MongoProperties mediaDbProps) {
        return new SimpleMongoClientDatabaseFactory(mediaDbProps.getUri());
    }
}
