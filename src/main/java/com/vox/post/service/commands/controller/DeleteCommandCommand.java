package com.vox.post.service.commands.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vox.post.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DeleteCommandCommand {
    private static final Logger logger = LoggerFactory.getLogger(DeleteCommandCommand.class);
    private PostService postService;
    @Value("${id}")
    private String id;

    @RabbitListener(queues = "${rabbitmq.queue5.name}")
    public void consumeMessage(JsonNode message){
        String id = message.get("id").asText();
        if(id.equals(this.id)) {
            String commandName = message.get("commandName").asText();
            logger.info("Message received: " + message);
            postService.removeCommand(commandName);
        }
    }

    public DeleteCommandCommand(PostService postService, ObjectMapper objectMapper) {
        this.postService = postService;
    }
}
