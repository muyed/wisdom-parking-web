package com.muye.wp.common.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Created by muye on 18/1/28.
 */
@Component
public class RedisComponent {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public Object get(String key){
        return redisTemplate.opsForValue().get(key);
    }

    public void set(String key, Object value, long time, TimeUnit timeUnit){
        redisTemplate.opsForValue().set(key, value, time, timeUnit);
    }
}
