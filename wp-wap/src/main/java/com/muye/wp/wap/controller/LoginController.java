package com.muye.wp.wap.controller;

import com.alibaba.fastjson.JSONObject;
import com.muye.wp.common.component.RedisComponent;
import com.muye.wp.common.component.SmsComponent;
import com.muye.wp.common.cons.RespStatus;
import com.muye.wp.common.cons.RedisKey;
import com.muye.wp.common.cons.UserType;
import com.muye.wp.common.exception.WPException;
import com.muye.wp.common.rest.Result;
import com.muye.wp.common.utils.CommonUtil;
import com.muye.wp.dao.domain.ext.UserExt;
import com.muye.wp.service.UserService;
import com.muye.wp.wap.security.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * Created by muye on 18/1/26.
 */
@RestController
@RequestMapping("/api")
@Auth
public class LoginController {

    @Autowired
    private SmsComponent smsComponent;

    @Autowired
    private RedisComponent redisComponent;

    @Autowired
    private UserService userService;

    @Autowired
    @Lazy
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${sms.temp.reg}")
    private String regTemp;

    @Value("${sms.temp.login}")
    private String loginTemp;

    @RequestMapping("/login")
    public Result login(){
       return Result.fail(new WPException(RespStatus.NOT_LOGIN));
    }

    @GetMapping(path = "/login/sms/{phone}")
    public Result loginSms(@PathVariable String phone){
        if (redisComponent.get(RedisKey.LOGIN_SMS + phone) != null){
            throw new WPException(RespStatus.SMS_OFTEN);
        }
        redisComponent.set(RedisKey.LOGIN_SMS + phone, true, 1l, TimeUnit.MINUTES);

        String code = CommonUtil.genRandomNum(6);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        smsComponent.send(phone, loginTemp, jsonObject.toJSONString());

        redisComponent.set(RedisKey.LOGIN_PHONE + phone, code, 5 * 60 * 1000, TimeUnit.MINUTES);
        return Result.ok(null);
    }

    @GetMapping(path = "/reg/sms/{phone}")
    public Result regSms(@PathVariable String phone){

        if (redisComponent.get(RedisKey.REG_SMS + phone) != null){
            throw new WPException(RespStatus.SMS_OFTEN);
        }
        redisComponent.set(RedisKey.REG_SMS + phone, true, 1l, TimeUnit.MINUTES);

        String code = CommonUtil.genRandomNum(6);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        smsComponent.send(phone, regTemp, jsonObject.toJSONString());

        redisComponent.set(RedisKey.REG_PHONE + phone, code, 5 * 60 * 1000, TimeUnit.MINUTES);
        return Result.ok(null);
    }

    @PostMapping(path = "/reg")
    public Result reg(@RequestBody UserExt user){
        if (!user.getCode().equals(redisComponent.get(RedisKey.REG_PHONE + user.getPhone()))){
            throw new WPException(RespStatus.SMS_CODE_WRONG);
        }

        if (userService.selectByPhoneAndType(user.getPhone(), UserType.GENERAL.getType()) != null){
            throw new WPException(RespStatus.RESOURCE_EXISTED);
        }

        if (userService.selectByUsernameAndType(user.getUsername(), UserType.GENERAL.getType()) != null){
            throw new WPException(RespStatus.RESOURCE_EXISTED);
        }

        user.setType(1);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userService.reg(user);
        return Result.ok(null);
    }
}
