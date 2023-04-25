package com.vox.post.service.interfaces;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public abstract class sendRabbitMQCommand implements Command {
    @Value("${rabbitmq.userexchange.name")
    private String exchange;
    @Value("${rabbitmq.routing.key")
    private String routingKey;
    private final RabbitTemplate rabbitTemplate;
    @Autowired
    public sendRabbitMQCommand(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(String message){
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }

}
