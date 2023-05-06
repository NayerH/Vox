package com.vox.post.config.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.internal.SimpleMongoClient;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import static java.util.concurrent.TimeUnit.SECONDS;

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
        MongoClient mongoClient = MongoClients.create(
                MongoClientSettings.builder().applyConnectionString(new ConnectionString(postsDbProps.getUri()))
                        .applyToConnectionPoolSettings(builder -> builder.maxWaitTime(10,SECONDS).maxSize(200))
                        .build()
        );
//        return new SimpleMongoClientDatabaseFactory(postsDbProps.getUri());
        return new SimpleMongoClientDatabaseFactory(mongoClient, postsDbProps.getDatabase());
    }

    @Bean(name = "mediaMongoTemplate")
    public MongoTemplate mediaDbMongoTemplate() {
        return new MongoTemplate(mediaMongoDatabaseFactory(getMediaDbProps()));
    }

    private MongoDatabaseFactory mediaMongoDatabaseFactory(MongoProperties mediaDbProps) {
        MongoClient mongoClient = MongoClients.create(
                MongoClientSettings.builder().applyConnectionString(new ConnectionString(mediaDbProps.getUri()))
                        .applyToConnectionPoolSettings(builder -> builder.maxWaitTime(10,SECONDS).maxSize(200))
                        .build()
        );
        return new SimpleMongoClientDatabaseFactory(mongoClient, mediaDbProps.getDatabase());
//        return new SimpleMongoClientDatabaseFactory(mediaDbProps.getUri());
    }
}
