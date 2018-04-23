package com.muye.wp.wap.controller;

import com.muye.wp.common.cons.UserType;
import com.muye.wp.common.rest.Result;
import com.muye.wp.dao.domain.UserBank;
import com.muye.wp.service.UserBankService;
import com.muye.wp.wap.security.Auth;
import com.muye.wp.wap.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by muye on 18/4/23.
 */
@RestController
@RequestMapping("/api/bank")
public class UserBankController {

    @Autowired
    private UserBankService userBankService;

    @Auth(value = UserType.GENERAL, cret = true)
    @PostMapping("/add")
    public Result add(@RequestBody UserBank userBank){

        userBank.setUserId(SecurityConfig.getLoginId());
        userBankService.add(userBank);
        return Result.ok();
    }

}
