package com.muye.wp.wap.controller;

import com.muye.wp.common.cons.UserType;
import com.muye.wp.common.rest.Result;
import com.muye.wp.dao.domain.User;
import com.muye.wp.service.UserService;
import com.muye.wp.wap.security.Auth;
import com.muye.wp.wap.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by muye on 18/3/7.
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Auth(UserType.GENERAL)
    @PostMapping("/idCardAuth")
    public Result<Boolean> idCardAuth(@RequestBody User user){
        Long userId = SecurityConfig.getLoginId();
        userService.idcardAuth(userId, user.getRealName(), user.getIdentityCard());
        return Result.ok();
    }
}
