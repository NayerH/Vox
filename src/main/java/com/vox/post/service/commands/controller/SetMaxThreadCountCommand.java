package com.vox.post.service.commands.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executor;

@Service
public class SetMaxThreadCountCommand {
    private static final Logger logger = LoggerFactory.getLogger(SetMaxThreadCountCommand.class);

    @Qualifier("taskExecutor")
    @Autowired
    private Executor executor;

    @Value("${id}")
    private String id;
    @RabbitListener(queues = "${rabbitmq.queue2.name}")
    public void consumeMessage(JsonNode message){
        String id = message.get("id").asText();
        if(id.equals(this.id)) {
            logger.info("Message received: " + message);
            Integer maxThreadCount = Integer.parseInt(message.get("maxThreadCount").asText());
            ((ThreadPoolTaskExecutor) executor).setMaxPoolSize(maxThreadCount);
        }
    }
}
