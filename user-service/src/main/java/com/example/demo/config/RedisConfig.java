package com.example.demo.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

@Configuration
@EnableRedisRepositories
public class RedisConfig implements CachingConfigurer {
    @Value(value = "${spring.redis.host}")
    private String redisHost;

    @Value(value = "${spring.redis.port}")
    private String redisPort;

    @Value(value = "${redis.timeout}")
    private String redisTimeout;

    @Value(value = "${spring.redis.cloud.password}")
    private String password;

    @Value(value = "${spring.redis.cloud.host}")
    private String redisHostCloud;

    @Value(value = "${spring.redis.cloud.port}")
    private String redisPortCloud;


    @Bean(name = "jedisConnectionFactory")
    JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(redisHost, Integer.parseInt(redisPort));
        JedisClientConfiguration.JedisClientConfigurationBuilder jedisClientConfiguration = JedisClientConfiguration.builder();
        jedisClientConfiguration.connectTimeout(Duration.ofSeconds(Integer.parseInt(redisTimeout)));

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(20);
        poolConfig.setMaxIdle(10);
        poolConfig.setMinIdle(5);
        jedisClientConfiguration.usePooling().poolConfig(poolConfig);

        return new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration.build());
    }

    @Bean(name = "jedisCloudConnectionFactory")
    JedisConnectionFactory jedisCloudConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(redisHostCloud, Integer.parseInt(redisPortCloud));
        redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
        JedisClientConfiguration.JedisClientConfigurationBuilder jedisClientConfiguration = JedisClientConfiguration.builder();
        jedisClientConfiguration.connectTimeout(Duration.ofSeconds(Integer.parseInt(redisTimeout)));
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(20);
        poolConfig.setMaxIdle(10);
        poolConfig.setMinIdle(5);
        jedisClientConfiguration.usePooling().poolConfig(poolConfig);

        return new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration.build());
    }

    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(){
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }

    @Bean(name = "redisCacheManager")
    @Primary
    public RedisCacheManager redisCacheManager(@Qualifier("redisTemplate") RedisTemplate<String, Object> redisTemplate) {
        RedisCacheManager redisCacheManager = RedisCacheManager.create(redisTemplate.getConnectionFactory());
        return redisCacheManager;
    }

    @Bean(name = "redisUserTemplate")
    public RedisTemplate<String, Object> redisTemplateCloud(){
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisCloudConnectionFactory());
        return template;
    }

    @Bean(name = "redisCloudCacheManager")
    public RedisCacheManager redisCloudCacheManager(@Qualifier("redisUserTemplate") RedisTemplate<String, Object> redisTemplate) {
        RedisCacheManager redisCacheManager = RedisCacheManager.create(redisTemplate.getConnectionFactory());
        return redisCacheManager;
    }

    @Bean(name = "redisScheduleTemplate")
    public RedisTemplate<String, Object> redisScheduleTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        return redisTemplate;
    }

    @Bean(name = "redisSchedulerCacheManager")
    public RedisCacheManager redisSchedulerCacheManager(@Qualifier("redisScheduleTemplate") RedisTemplate<String, Object> redisTemplate) {
        RedisCacheManager redisCacheManager = RedisCacheManager.create(redisTemplate.getConnectionFactory());
        return redisCacheManager;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory();
    }


}