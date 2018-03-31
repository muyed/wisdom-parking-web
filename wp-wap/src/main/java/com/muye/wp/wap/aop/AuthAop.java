package com.muye.wp.wap.aop;

import com.muye.wp.common.cons.RespStatus;
import com.muye.wp.common.cons.UserType;
import com.muye.wp.common.exception.WPException;
import com.muye.wp.wap.security.Auth;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

/**
 * Created by muye on 18/2/28.
 */
@Component
@Aspect
public class AuthAop {

    @Before("execution(* com.muye.wp.wap.controller.*.*(..)) && @annotation(org.springframework.web.bind.annotation.RequestMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)" +
            "")
    public void authAop(JoinPoint point){

        //获取方法上的Auth注解
        Auth auth = null;
        try {
            auth = (Auth) Stream.of(((MethodSignature) point.getSignature()).getMethod().getAnnotations())
                    .filter(a -> a.annotationType() == Auth.class)
                    .findFirst()
                    .get();
        }catch (NoSuchElementException e){
        }

        //如果方法上没有Auth注解取类上面的Auth注解
        if (auth == null){
            try {
                auth = (Auth) Stream.of(point.getTarget().getClass().getAnnotations())
                        .filter(a -> a.annotationType() == Auth.class)
                        .findFirst()
                        .get();
            }catch (NoSuchElementException e){
                throw new WPException(RespStatus.AUTH_ERR);
            }

        }

        UserType[] userTypes = auth.value();
        //为空表示所有人可访问
        if (userTypes.length == 0){
            return;
        }

        //访问该资源需要的权限
        List<GrantedAuthority> authorities = new ArrayList<>();
        Stream.of(userTypes).forEach(userType -> authorities.add(new SimpleGrantedAuthority(userType.name())));

        //验证权限
        boolean isPass = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream()
                .filter(authorities::contains)
                .findFirst()
                .isPresent();

        if (!isPass)
            throw new WPException(RespStatus.AUTH_NOT);
    }
}
