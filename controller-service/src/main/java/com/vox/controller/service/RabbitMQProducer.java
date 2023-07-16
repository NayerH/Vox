package com.vox.controller.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducer {
    @Value("${rabbitmq.exchange.name}")
    private String exchange;
    private final RabbitTemplate rabbitTemplate;
    private static final Logger logger = LoggerFactory.getLogger(RabbitMQProducer.class);
    @Autowired
    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    public void sendMessage(String routingKey, JsonNode message) {
        logger.info("Message sent on " + routingKey + ": " + message);
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}
