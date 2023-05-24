package com.vox.post.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.client.MongoClient;
import com.vox.post.service.commands.controller.SetMQCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
public class FreezeApp {

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    @Qualifier(value = "postMongoClient")
    private MongoClient postClient;

    @Autowired
    @Qualifier("mediaMongoClient")
    private MongoClient mediaClient;

    @Autowired
    @Qualifier("jedisConnectionFactory")
    JedisConnectionFactory jedisConnectionFactory;

    @Autowired
    @Qualifier("jedisCloudConnectionFactory")
    JedisConnectionFactory jedisCloudConnectionFactory;

    @Autowired
    private ConfigurableApplicationContext context;

    private static final Logger logger = LoggerFactory.getLogger(SetMQCommand.class);

    @Value("${id}")
    private String id;

    @RabbitListener(queues = "${rabbitmq.queue8.name}")
    public void consumeMessage(JsonNode message){
        String id = message.get("id").asText();
        if(id.equals(this.id)) {
            logger.info("Message received: " + message);
//            TODO: Stop the endpoint from accepting requests
            taskExecutor.shutdown();
            postClient.close();
            mediaClient.close();
            jedisConnectionFactory.getConnection().close();
            jedisCloudConnectionFactory.getConnection().close();
            ConfigurableEnvironment environment = context.getEnvironment();
            MutablePropertySources propertySources = environment.getPropertySources();
            Map<String,Object> properties = new HashMap<>();
            properties.put("disable.post.controller", "true");
            propertySources.addFirst(new MapPropertySource("runtimeProperties", properties));
        }
    }


}
