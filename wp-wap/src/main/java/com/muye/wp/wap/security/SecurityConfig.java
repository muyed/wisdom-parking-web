package com.muye.wp.wap.security;

import com.muye.wp.dao.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * Created by muye on 18/1/26.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private WPWapAuthenticationProvider provider;

    @Autowired
    private WPWapAuthenticationSuccessHandler successHandler;

    @Autowired
    private WPWapAuthenticationFailureHandler failureHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .cors().disable()
                .authorizeRequests()
                .antMatchers("/",
                        "/api/reg/**",
                        "/api/login/**",
                        "/api/pay/mayi/callback",
                        "/api/pay/wx/callback").permitAll()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .anyRequest().authenticated()
                .and()
                .authenticationProvider(provider)
                .formLogin().loginPage("/api/login").successHandler(successHandler).failureHandler(failureHandler).permitAll()
                .and()
                .logout().permitAll();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DefaultCookieSerializer defaultCookieSerializer(){
        DefaultCookieSerializer defaultCookieSerializer = new DefaultCookieSerializer();
        defaultCookieSerializer.setUseHttpOnlyCookie(false);
        return defaultCookieSerializer;
    }

    public static Long getLoginId(){
        return ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
    }
}
