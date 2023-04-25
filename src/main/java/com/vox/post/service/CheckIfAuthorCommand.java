package com.vox.post.service;

import com.vox.post.service.interfaces.AuthenticationCommand;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.stereotype.Service;

@Service
public class CheckIfAuthorCommand implements AuthenticationCommand {
    @Value("${rabbitmq.userexchange.name")
    private String exchange;
    @Value("${rabbitmq.routing.key")
    private String routingKey;
    @Autowired
    private RabbitTemplate rabbitTemplate;


    public void sendMessage(String message){
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }

    @Override
    public Boolean execute(MongoId userId) {

        return null;
    }
}
