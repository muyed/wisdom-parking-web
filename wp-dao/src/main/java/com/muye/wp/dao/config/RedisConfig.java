package com.muye.wp.dao.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Created by muye on 18/1/28.
 */
@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Bean
    public LettuceConnectionFactory connectionFactory(){
        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory();
        connectionFactory.setHostName(host);
        return connectionFactory;
    }

    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory connectionFactory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setEnableTransactionSupport(true);
        return  redisTemplate;
    }
}
