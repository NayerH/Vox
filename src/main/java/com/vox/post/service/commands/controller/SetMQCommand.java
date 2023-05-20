package com.vox.post.service.commands.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class SetMQCommand {
    private static final Logger logger = LoggerFactory.getLogger(SetMQCommand.class);

    @Autowired
    private Environment environment;

    @Autowired
    private CachingConnectionFactory connectionFactory;

    @Value("${id}")
    private String id;
    @RabbitListener(queues = "${rabbitmq.queue1.name}")
    public void consumeMessage(JsonNode message){
        String id = message.get("id").asText();
        if(id.equals(this.id)){
            logger.info("Message received: " + message);
            String newHost = message.get("ip").asText();
            String newPort = message.get("port").asText();
            connectionFactory.setHost(newHost);
            connectionFactory.setPort(Integer.parseInt(newPort));
            connectionFactory.resetConnection();
        }
    }
}
