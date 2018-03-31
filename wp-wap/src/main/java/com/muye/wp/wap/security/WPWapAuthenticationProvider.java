package com.muye.wp.wap.security;

import com.alibaba.fastjson.JSONObject;
import com.muye.wp.common.component.RedisComponent;
import com.muye.wp.common.cons.RespStatus;
import com.muye.wp.common.cons.RedisKey;
import com.muye.wp.common.cons.UserType;
import com.muye.wp.dao.domain.User;
import com.muye.wp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by muye on 18/1/26.
 */
@Component
public class WPWapAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private RedisComponent redisComponent;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        StringBuffer sb = new StringBuffer();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        try {
            BufferedReader reader = request.getReader();
            reader.mark(10000);
            String line;
            while ((line = reader.readLine()) != null){
                sb.append(line);
            }
            reader.reset();
        }catch (IOException e){
        }

        User param = JSONObject.parseObject(sb.toString(), User.class);

        if (param == null){
            throw new BadCredentialsException(RespStatus.WRONG_LOGIN.getMessage());
        }

        int type = param.getType();
        String userName = param.getUsername();
        String password = param.getPassword();

        User user;
        try {
            //电话号码短信验证码登录
            Long.valueOf(userName);
            user = userService.selectByPhoneAndType(userName, type);
            if (user == null || !password.equals(redisComponent.get(RedisKey.LOGIN_PHONE + userName))){
                throw new BadCredentialsException(RespStatus.WRONG_LOGIN.getMessage());
            }
        }catch (NumberFormatException e){
            //用户名密码登录
            user = userService.selectByUsernameAndType(userName, type);
            if (user == null || !bCryptPasswordEncoder.matches(password, user.getPassword())) {
                throw new BadCredentialsException(RespStatus.WRONG_LOGIN.getMessage());
            }
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(UserType.of(type).name()));

        return new UsernamePasswordAuthenticationToken(user, password, authorities);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
