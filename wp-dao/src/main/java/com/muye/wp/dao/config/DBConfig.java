package com.muye.wp.dao.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.muye.wp.dao.page.PageInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by muye on 18/1/23.
 */
@Configuration
public class DBConfig {

    private static Logger logger = LoggerFactory.getLogger(DBConfig.class);

    @Bean(name = "mainDS")
    public DruidDataSource mainDataSource(
            @Value("${spring.datasource.url}") String url,
            @Value("${spring.datasource.username}") String username,
            @Value("${spring.datasource.password}") String password) {
        logger.info("config main db");

        DruidDataSource ds = new DruidDataSource();
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setInitialSize(0);
        ds.setMaxActive(20);
        ds.setMinIdle(0);
        ds.setMaxWait(5000);
        ds.setValidationQuery("select 1");
        ds.setTestOnBorrow(false);
        ds.setTestOnReturn(false);
        ds.setTestWhileIdle(true);
        ds.setTimeBetweenEvictionRunsMillis(60000);
        ds.setMinEvictableIdleTimeMillis(25200000);
        ds.setRemoveAbandoned(true);
        ds.setRemoveAbandonedTimeout(1800);
        ds.setLogAbandoned(true);
        try {
            ds.setFilters("mergeStat");
        }catch (Exception e){
            logger.error("mainDS error ",e);
        }

        return ds;
    }

    @Bean(name = "mainSqlSessionFactory")
    public SqlSessionFactory mainSqlSessionFactory(@Qualifier("mainDS") DruidDataSource mainDS) throws Exception {
        final SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(mainDS);
        factoryBean.setTypeHandlersPackage("com.muye.wp.dao.typehandlers");
        factoryBean.setPlugins(new Interceptor[]{new PageInterceptor()});
        SqlSessionFactory sqlSessionFactory = factoryBean.getObject();
        sqlSessionFactory.getConfiguration().setMapUnderscoreToCamelCase(true);
        return sqlSessionFactory;
    }

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer(){
        logger.info("config main mapper");
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setBasePackage("com.muye.wp.dao.mapper");
        configurer.setSqlSessionFactoryBeanName("mainSqlSessionFactory");
        return configurer;
    }
}
