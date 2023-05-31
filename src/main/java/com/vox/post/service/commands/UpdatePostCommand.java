package com.vox.post.service.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vox.post.exception.ApiRequestException;
import com.vox.post.model.Category;
import com.vox.post.model.Post;
import com.vox.post.repository.posts.PostRepository;
import com.vox.post.service.interfaces.UpdateCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;

@Component
public class UpdatePostCommand implements UpdateCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdatePostCommand.class);

    @Value("${rabbitmq.searchQueueRouting.key}")
    private String searchPostsQueueRoutingKey;

    @Value("${rabbitmq.searchExchange.name}")
    private String searchExchange;
    private final RabbitTemplate rabbitTemplate;
    private final PostRepository postRepository;
    @Autowired
    public UpdatePostCommand(PostRepository postRepository ,  RabbitTemplate rabbitTemplate) {
        this.postRepository = postRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public Post execute(String postId,
                        String title,
                        String content,
                        List<String> tags,
                        Category.CategoryEnum category)
    {
        Post post = this.postRepository.findById(postId).orElseThrow(
                () -> new ApiRequestException("Post with id " + postId + " does not exist")
        );
        if (checkValidString(title, post.getTitle())){
            post.setTitle(title);
        }
        if (checkValidString(content, post.getContent())){
            post.setContent(content);
        }
        if(tags != null){
            post.setTags(tags);
        }
        if(category != null){
            post.setCategory(category);
        }
        LinkedHashMap<String, Object> rabbitMessage = new LinkedHashMap<>();
        rabbitMessage.put("post", post);
        rabbitMessage.put("id", post.getId().toString());
        rabbitMessage.put("command", "update");
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
        this.postRepository.save(post);
        return post;
    }

    private Boolean checkValidString(String o1, String o2) {
        if (o1 == null) return false;
        if (o1.length() == 0) return false;
        return !o1.equals(o2);
    }
}
