package com.vox.post.service.commands.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SetMaxDbConnectionsCountCommand {
    private static final Logger logger = LoggerFactory.getLogger(SetMaxDbConnectionsCountCommand.class);

    @Value("${id}")
    private String id;
    @RabbitListener(queues = "${rabbitmq.queue3.name}")
    public void consumeMessage(JsonNode message){
        String id = message.get("id").asText();
        if(id.equals(this.id)) {
            logger.info("Message received: " + message);
        }
    }
}
