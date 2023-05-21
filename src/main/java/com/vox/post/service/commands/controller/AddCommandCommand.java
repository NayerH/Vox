package com.vox.post.service.commands.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.vox.post.service.PostService;
import com.vox.post.service.interfaces.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AddCommandCommand {
    private static final Logger logger = LoggerFactory.getLogger(AddCommandCommand.class);
    private PostService postService;
    private ObjectMapper objectMapper;
    @Value("${id}")
    private String id;

    @RabbitListener(queues = "${rabbitmq.queue4.name}")
    public void consumeMessage(JsonNode message){
        String id = message.get("id").asText();
        if(id.equals(this.id)) {
            JsonNode commandNode = message.get("command");
            Command command = null;
            try {
                command = objectMapper.treeToValue(commandNode, Command.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            String commandName = message.get("commandName").asText();
            logger.info("Message received: " + message);
            postService.addCommand(commandName, command);
        }
    }

    public AddCommandCommand(PostService postService, ObjectMapper objectMapper) {
        this.postService = postService;
        this.objectMapper = objectMapper;
    }
}
