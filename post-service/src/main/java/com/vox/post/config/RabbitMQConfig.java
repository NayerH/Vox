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
    @Value("${rabbitmq.queue3.name}")
    private String queue3;
    @Value("${rabbitmq.queue4.name}")
    private String queue4;
    @Value("${rabbitmq.queue5.name}")
    private String queue5;
    @Value("${rabbitmq.queue6.name}")
    private String queue6;
    @Value("${rabbitmq.queue7.name}")
    private String queue7;
    @Value("${rabbitmq.queue8.name}")
    private String queue8;
    @Value("${rabbitmq.queue9.name}")
    private String queue9;
    @Value("${rabbitmq.queue10.name}")
    private String queue10;

    @Value("${rabbitmq.searchQueue.name}")
    private String searchPostsQueue;
    @Value("${rabbitmq.exchange.name}")
    private String exchange;
    @Value("${rabbitmq.searchExchange.name}")
    private String searchExchange;

    @Value("${rabbitmq.routing1.key}")
    private String key1;
    @Value("${rabbitmq.routing2.key}")
    private String key2;
    @Value("${rabbitmq.routing3.key}")
    private String key3;
    @Value("${rabbitmq.routing4.key}")
    private String key4;
    @Value("${rabbitmq.routing5.key}")
    private String key5;
    @Value("${rabbitmq.routing6.key}")
    private String key6;
    @Value("${rabbitmq.routing7.key}")
    private String key7;
    @Value("${rabbitmq.routing8.key}")
    private String key8;
    @Value("${rabbitmq.routing9.key}")
    private String key9;
    @Value("${rabbitmq.routing10.key}")
    private String key10;
    @Value("${rabbitmq.searchQueueRouting.key}")
    private String searchPostsQueueRoutingKey;

    @Bean
    public Queue queue1(){
        return new Queue(queue1);
    }
    @Bean
    public Queue queue2(){
        return new Queue(queue2);
    }
    @Bean
    public Queue queue3(){
        return new Queue(queue3);
    }
    @Bean
    public Queue queue4(){
        return new Queue(queue4);
    }
    @Bean
    public Queue queue5(){
        return new Queue(queue5);
    }
    @Bean
    public Queue queue6(){
        return new Queue(queue6);
    }
    @Bean
    public Queue queue7(){
        return new Queue(queue7);
    }
    @Bean
    public Queue queue8(){
        return new Queue(queue8);
    }
    @Bean
    public Queue queue9(){
        return new Queue(queue9);
    }
    @Bean
    public Queue queue10(){
        return new Queue(queue10);
    }
    @Bean
    public Queue searchPostsQueue(){
        return new Queue(searchPostsQueue);
    }
    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(exchange);
    }

    @Bean
    public TopicExchange searchExchange(){
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
    public Binding binding3(){
        return BindingBuilder.bind(queue3()).to(exchange()).with(key3);
    }
    @Bean
    public Binding binding4(){
        return BindingBuilder.bind(queue4()).to(exchange()).with(key4);
    }
    @Bean
    public Binding binding5(){
        return BindingBuilder.bind(queue5()).to(exchange()).with(key5);
    }
    @Bean
    public Binding binding6(){
        return BindingBuilder.bind(queue6()).to(exchange()).with(key6);
    }
    @Bean
    public Binding binding7(){
        return BindingBuilder.bind(queue7()).to(exchange()).with(key7);
    }
    @Bean
    public Binding binding8(){
        return BindingBuilder.bind(queue8()).to(exchange()).with(key8);
    }
    @Bean
    public Binding binding9(){
        return BindingBuilder.bind(queue9()).to(exchange()).with(key9);
    }
    @Bean
    public Binding binding10(){
        return BindingBuilder.bind(queue10()).to(exchange()).with(key10);
    }

    @Bean
    public Binding bindingSearchPosts(){
        return BindingBuilder.bind(searchPostsQueue()).to(searchExchange()).with(searchPostsQueueRoutingKey);
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