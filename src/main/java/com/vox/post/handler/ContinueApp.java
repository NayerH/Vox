package com.vox.post.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.client.MongoClient;
import com.vox.post.service.commands.controller.SetMQCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

@Service
public class ContinueApp {

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private MongoClient mongoClient;

    @Autowired
    private JedisConnectionFactory redisConnectionFactory;
    private static final Logger logger = LoggerFactory.getLogger(SetMQCommand.class);

    @Value("${id}")
    private String id;

    @RabbitListener(queues = "${rabbitmq.queue9.name}")
    public void consumeMessage(JsonNode message){
        String id = message.get("id").asText();
        if(id.equals(this.id)) {
            logger.info("Message received: " + message);
//            TODO: Let the endpoint start accepting requests
            taskExecutor.initialize();
            mongoClient.startSession();
            redisConnectionFactory.getConnection();
        }
    }
}
