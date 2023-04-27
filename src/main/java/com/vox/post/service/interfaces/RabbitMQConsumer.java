package com.vox.post.service.interfaces;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

//SHOULD BE THE COMMAND CLASS THAT RECEIVES THE MESSAGE TO PROCESS THE REQUEST FROM ANOTHER APP
//consumeMessage IS THE METHOD THAT WILL BE CALLED WHEN A MESSAGE IS RECEIVED (COMMAND ITSELF)
@Service
public class RabbitMQConsumer {

    @RabbitListener(queues = "${rabbitmq.queue1.name}")
    public void consumeMessage(String message){
        System.out.println("Message received: " + message);
    }
}
