package com.vox.post.service.commands;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.vox.post.model.Post;
import com.vox.post.repository.posts.PostRepository;
import com.vox.post.service.interfaces.ReturnOneCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jackson.JsonObjectSerializer;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.LinkedHashMap;

@Component
public class AddPostCommand implements ReturnOneCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddPostCommand.class);

    @Value("${rabbitmq.searchQueueRouting.key}")
    private String searchPostsQueueRoutingKey;

    @Value("${rabbitmq.searchExchange.name}")
    private String searchExchange;
    private final RabbitTemplate rabbitTemplate;

    private final PostRepository postRepository;
    private final RedisCacheManager redisCacheManager;
    @Autowired
    public AddPostCommand(PostRepository postRepository, @Qualifier("redisCacheManager") RedisCacheManager redisCacheManager,  RabbitTemplate rabbitTemplate) {
        this.postRepository = postRepository;
        this.redisCacheManager = redisCacheManager;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public Post execute(Object o) {
        Post p = (Post) o;
        Post savedPost =  postRepository.save(p);
        LinkedHashMap<String, Object> rabbitMessage = new LinkedHashMap<>();
        rabbitMessage.put("post", savedPost);
        rabbitMessage.put("id", savedPost.getId().toString());
        rabbitMessage.put("command", "insert");
        LOGGER.info(String.format("Sent JSON message -> %s", rabbitMessage));
        ObjectMapper mapper = new ObjectMapper();
        // Try block to check for exceptions
        try {
            String postJsonString
                    = mapper.writeValueAsString(rabbitMessage);
            rabbitTemplate.convertAndSend(searchExchange, searchPostsQueueRoutingKey, postJsonString );

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
//        redisCacheManager.getCache("posts").put(savedPost.getId(), savedPost);
        return savedPost;
    }
}
