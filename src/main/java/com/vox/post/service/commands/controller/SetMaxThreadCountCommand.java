package com.vox.post.service.commands.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class SetMaxThreadCountCommand {
    private static final Logger logger = LoggerFactory.getLogger(SetMaxThreadCountCommand.class);
    @RabbitListener(queues = "${rabbitmq.queue2.name}")
    public void consumeMessage(String message){
        logger.info("Message received: " + message);
    }
}
