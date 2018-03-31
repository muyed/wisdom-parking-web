package com.muye.wp.embed.server.config;

import com.muye.wp.embed.server.processor.Processor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Created by muye on 18/2/26.
 */
@SpringBootApplication(scanBasePackages = {
        "com.muye.wp.embed.server"})
public class AppConfig {

    public static void main(String[] args){
        new SpringApplication(AppConfig.class).run(args);
    }

    @Bean
    public Processor processor(){
        return new Processor(5555);
    }
}
