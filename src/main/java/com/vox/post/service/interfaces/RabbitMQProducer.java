package com.vox.post.service.interfaces;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//SHOULD BE INSTANTIATED IN THE COMMAND CLASS THAT SENDS A MESSAGE ON THAT QUEUE
@Component
public class RabbitMQProducer {
    @Value("${rabbitmq.exchange.name")
    private String exchange;
    @Value("${rabbitmq.routing1.key")
    private String routingKey;
    private final RabbitTemplate rabbitTemplate;
    @Autowired
    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(String message){
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }

}
