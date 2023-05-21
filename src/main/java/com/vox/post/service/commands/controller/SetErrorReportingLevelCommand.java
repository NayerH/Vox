package com.vox.post.service.commands.controller;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Iterator;

@Service
public class SetErrorReportingLevelCommand {
    private static final Logger logger = LoggerFactory.getLogger(SetErrorReportingLevelCommand.class);

    @Value("${id}")
    private String id;
    @RabbitListener(queues = "${rabbitmq.queue10.name}")
    public void consumeMessage(JsonNode message){
        String id = message.get("id").asText();
        if(id.equals(this.id)) {
            logger.info("Message received: " + message);
            Level level;
            if(message.get("level") != null){
                String levelStr = message.get("level").asText();
                if(levelStr.equals("ERROR"))
                    level = Level.toLevel("ERROR");
                else if(levelStr.equals("WARN"))
                    level = Level.toLevel("WARN");
                else if(levelStr.equals("INFO"))
                    level = Level.toLevel("INFO");
                else if(levelStr.equals("DEBUG"))
                    level = Level.toLevel("DEBUG");
                else if(levelStr.equals("TRACE"))
                    level = Level.toLevel("TRACE");
                else
                    level = Level.toLevel("ERROR");
            } else {
                level = Level.toLevel("INFO");
            }
            ch.qos.logback.classic.Logger remoteLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
            remoteLogger.setLevel(level);
        }
    }
}
