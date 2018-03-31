package com.muye.wp.wap.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

/**
 * Created by muye on 18/1/28.
 */
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 60 * 60 * 24 * 30)
public class HttpSessionRedisConfig extends AbstractHttpSessionApplicationInitializer {

    @Value("${spring.redis.host}")
    private String host;

    public HttpSessionRedisConfig(){
        super(HttpSessionRedisConfig.class);
    }
}
