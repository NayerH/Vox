package com.vox.post.service.commands.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.stereotype.Service;

import static java.util.concurrent.TimeUnit.SECONDS;

@Service
public class SetMaxDbConnectionsCountCommand {
    private static final Logger logger = LoggerFactory.getLogger(SetMaxDbConnectionsCountCommand.class);

    @Autowired
    @Qualifier(value = "postMongoClient")
    private MongoClient postClient;

    @Autowired
    @Qualifier("mediaMongoClient")
    private MongoClient mediaClient;

    @Autowired
    private MongoProperties postsDbProps;

    @Autowired
    private MongoProperties mediaDbProps;

    @Value("${id}")
    private String id;
    @RabbitListener(queues = "${rabbitmq.queue3.name}")
    public void consumeMessage(JsonNode message){
        String id = message.get("id").asText();
        if(id.equals(this.id)) {
            logger.info("Message received: " + message);
            int maxDbConnectionsCount = message.get("maxDbConnectionsCount").asInt();
            MongoClient tempPostClient = MongoClients.create(
                    MongoClientSettings.builder().applyConnectionString(new ConnectionString(postsDbProps.getUri()))
                            .applyToConnectionPoolSettings(builder -> builder.maxWaitTime(10,SECONDS).maxSize(maxDbConnectionsCount))
                            .build()
            );
            postClient.close();
            postClient = tempPostClient;
            MongoClient tempMediaClient = MongoClients.create(
                    MongoClientSettings.builder().applyConnectionString(new ConnectionString(mediaDbProps.getUri()))
                            .applyToConnectionPoolSettings(builder -> builder.maxWaitTime(10,SECONDS).maxSize(maxDbConnectionsCount))
                            .build()
            );
            mediaClient.close();
            mediaClient = tempMediaClient;
        }
    }
}
