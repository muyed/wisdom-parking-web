package com.muye.wp.common.utils;

import org.springframework.context.ApplicationContext;

/**
 * Created by muye on 18/3/31.
 */
public class SpringUtil {

    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext applicationContext){
        SpringUtil.applicationContext = applicationContext;
    }

    public static <T> T getBean(String beanName, Class<T> clazz){
        return SpringUtil.applicationContext.getBean(beanName, clazz);
    }
}
