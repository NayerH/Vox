package com.search.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.search.SearchApplication;
import com.search.entity.Post;
import com.search.entity.Tag;
import com.search.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Component
@RestController
@EnableRabbit
@RequestMapping("/api/post")
public class PostController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostController.class);

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routing;
    private final PostService service;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    public PostController(PostService service) {
        this.service = service;
    }

    @PostMapping
    public void save(@RequestBody final Post post) {
        service.save(post);
    }

    @GetMapping("/{id}")
    public Post findById(@PathVariable final String id) {
        return service.findById(id);
    }

    @GetMapping("/m/{id}")
    public Post findByMongoId(@PathVariable  String id) {
        return service.findByMongoId(id);
    }
    @PutMapping("/update")
    public void updateByMongoId(@RequestBody Post post) {

       service.updateByMongoId( post);
    }
    @PutMapping("/delete")
    public void deleteByMongoId(@RequestBody Post post) {
        service.deleteByMongoId( post.getMongoId());
    }

    @GetMapping("/all")
    public List<Post> findAll() {
        return service.findAll();
    }
    @DeleteMapping("/all")
    public  void delete( ) {
        service.deleteAll();
    }

    @PostMapping("/auto")
    public List<Post> autocomplete(@RequestBody Map<String, String> payload) {
        return service.autocomplete(payload.get("title"));
    }

    @PostMapping("/multi")
    public List<Post> multiple(@RequestBody Map<String, String> payload) {
        return service.multiplefields(payload.get("title"));
    }
    @GetMapping("/rabbit")
    public void sendMessage() {
        rabbitTemplate.convertAndSend(exchange,routing, "hey");
        return;
    }

    @RabbitListener(queues="search_posts")
    public void receiveMessage(String message) {

        LOGGER.info("Received message: {}", message);
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode payload = mapper.readValue(message, JsonNode.class);
            if (payload.get("command").textValue().equals("insert")){

                JsonNode actualObj =payload.get("post");
                service.insertFromQueue(actualObj, payload.get("id").textValue());
            }
            if (payload.get("command").textValue().equals("update")){
                JsonNode actualObj =payload.get("post");
                service.updateFromQueue(actualObj , payload.get("id").textValue());
            }
            if (payload.get("command").textValue().equals("delete")){
                JsonNode actualObj =payload.get("id");
                service.deleteByMongoId(actualObj.textValue());
            }


        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
//        String m = message.get("post").asText();
//        LOGGER.info("Received message: {}", m);

    }




//{
//        "id": "14",
//        "title": "first post",
//        "content": "hi",
//        "mongoId": "mongoId1",
//        "tags": [
//            {
//                "name": "prog_list"
//            },
//            {
//                "name": "cool_list"
//            }
//        ],
//        "views": 123
//    },
//
@PostMapping("/fuzzy")
public List<Post> findFuzzy(@RequestBody Map<String, String> payload) {
    return service.findFuzzy(payload.get("title"));
}
// @PostMapping("/full")
//    public List<Post> fullText(@RequestBody Map<String, String> payload) {
//        return service.fullText(payload.get("title"));
//    }
}
