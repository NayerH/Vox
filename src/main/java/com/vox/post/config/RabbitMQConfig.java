package com.vox.post.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${rabbitmq.queue1.name}")
    private String queue1;
    @Value("${rabbitmq.queue2.name}")
    private String queue2;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing1.key}")
    private String key1;
    @Value("${rabbitmq.routing2.key}")
    private String key2;

    @Bean
    public Queue queue1(){
        return new Queue(queue1);
    }
    @Bean
    public Queue queue2(){
        return new Queue(queue2);
    }
    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(exchange);
    }
    @Bean
    public Binding binding1(){
        return BindingBuilder.bind(queue1()).to(exchange()).with(key1);
    }
    @Bean
    public Binding binding2(){
        return BindingBuilder.bind(queue2()).to(exchange()).with(key2);
    }
    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
