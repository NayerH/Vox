package com.vox.post.service.interfaces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

//SHOULD BE THE COMMAND CLASS THAT RECEIVES THE MESSAGE TO PROCESS THE REQUEST FROM ANOTHER APP
//consumeMessage IS THE METHOD THAT WILL BE CALLED WHEN A MESSAGE IS RECEIVED (COMMAND ITSELF)
@Service
public class RabbitMQConsumer {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMQConsumer.class);
    @RabbitListener(queues = "${rabbitmq.queue1.name}")
    public void consumeMessage(String message){
        logger.info("Message received: " + message);
    }
}
