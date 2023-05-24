package com.vox.post.config.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Primary
    @Bean(name = "postMongoTemplate")
    public MongoTemplate postsDbMongoTemplate() {
        return new MongoTemplate(postsMongoDatabaseFactory(postMongoClient(),getPostsDbProps()));
    }

    @Primary
    @Bean(name = "postMongoClient")
    public MongoClient postMongoClient(){
        MongoProperties postsDbProps = getPostsDbProps();
        return MongoClients.create(
                MongoClientSettings.builder().applyConnectionString(new ConnectionString(postsDbProps.getUri()))
                        .applyToConnectionPoolSettings(builder -> builder.maxWaitTime(10,SECONDS).maxSize(200))
                        .build()
        );
    }

    private MongoDatabaseFactory postsMongoDatabaseFactory(@Qualifier("postMongoClient") MongoClient mongoClient, MongoProperties postsDbProps) {
        return new SimpleMongoClientDatabaseFactory(mongoClient, postsDbProps.getDatabase());
    }

    @Bean(name = "mediaMongoProperties")
    @ConfigurationProperties(prefix = "mongodb.media")
    public MongoProperties getMediaDbProps() {
        return new MongoProperties();
    }

    @Bean(name = "mediaMongoTemplate")
    public MongoTemplate mediaDbMongoTemplate() {
        return new MongoTemplate(mediaMongoDatabaseFactory(mediaMongoClient() ,getMediaDbProps()));
    }

    @Bean(name = "mediaMongoClient")
    public MongoClient mediaMongoClient(){
        MongoProperties mediaDbProps = getMediaDbProps();
        return MongoClients.create(
                MongoClientSettings.builder().applyConnectionString(new ConnectionString(mediaDbProps.getUri()))
                        .applyToConnectionPoolSettings(builder -> builder.maxWaitTime(10,SECONDS).maxSize(200))
                        .build()
        );
    }

    private MongoDatabaseFactory mediaMongoDatabaseFactory(@Qualifier("mediaMongoClient") MongoClient mongoClient ,MongoProperties mediaDbProps) {
        return new SimpleMongoClientDatabaseFactory(mongoClient, mediaDbProps.getDatabase());
    }
}
