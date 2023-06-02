package com.vox.post.service.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vox.post.exception.ApiRequestException;
import com.vox.post.model.Post;
import com.vox.post.repository.media.MediaRepository;
import com.vox.post.repository.posts.PostRepository;
import com.vox.post.service.interfaces.ReturnOneCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

@Component
public class DeletePostCommand implements ReturnOneCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdatePostCommand.class);

    @Value("${rabbitmq.searchQueueRouting.key}")
    private String searchPostsQueueRoutingKey;

    @Value("${rabbitmq.searchExchange.name}")
    private String searchExchange;
    private final RabbitTemplate rabbitTemplate;
    private final PostRepository postRepository;
    private final MediaRepository mediaRepository;

    @Autowired
    public DeletePostCommand(PostRepository postRepository, MediaRepository mediaRepository, RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.postRepository = postRepository;
        this.mediaRepository = mediaRepository;
    }
    @Override
    public Post execute(Object o) {
        String m = (String) o;
        Post post = this.postRepository.findById(m)
                .orElseThrow(() -> new ApiRequestException("No post is available with id " + m + "to be deleted")
                );
        String mediaFilesRefrence = post.getMediaFilesRefrence();
        if ( mediaFilesRefrence != null){
            mediaRepository.deleteById(mediaFilesRefrence);
        }
        postRepository.deleteById(m);

        LinkedHashMap<String, Object> rabbitMessage = new LinkedHashMap<>();
        rabbitMessage.put("id", post.getId().toString());
        rabbitMessage.put("command", "delete");
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
        return null;
    }
}
