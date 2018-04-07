package com.muye.wp.wap.config;

import com.muye.wp.common.component.SmsComponent;
import com.muye.wp.common.utils.SpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Created by muye on 18/1/26.
 */
@SpringBootApplication(scanBasePackages = {
        "com.muye.wp.wap",
        "com.muye.wp.service",
        "com.muye.wp.dao",
        "com.muye.wp.common",
        "com.muye.wp.pay"})
public class AppConfig {

    public static void main(String[] args){
        SpringUtil.setApplicationContext(new SpringApplication(AppConfig.class).run(args));
    }

    @Bean
    public SmsComponent smsComponent(){
        return new SmsComponent();
    }
}
