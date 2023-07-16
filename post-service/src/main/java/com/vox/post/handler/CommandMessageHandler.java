package com.vox.post.handler;

import com.vox.post.message.CommandMessage;
import com.vox.post.message.CommandType;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class CommandMessageHandler {
    @RabbitListener(queues = "${rabbitmq.queue2.name}")
    @RabbitHandler
    public void handleCommandMessage(CommandMessage commandMessage){
        if(commandMessage.getCommandType().equals(CommandType.ADD)){
            System.out.println("Add Command Received: " + commandMessage);
        } else if (commandMessage.getCommandType().equals(CommandType.UPDATE)) {
            System.out.println("Update Command Received: " + commandMessage);
        } else if (commandMessage.getCommandType().equals(CommandType.DELETE)) {
            System.out.println("Delete Command Received: " + commandMessage);
        }
    }
}
