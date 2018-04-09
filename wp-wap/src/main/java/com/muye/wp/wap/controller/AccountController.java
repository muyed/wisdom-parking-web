package com.muye.wp.wap.controller;

import com.muye.wp.common.rest.Result;
import com.muye.wp.service.AccountService;
import com.muye.wp.wap.security.Auth;
import com.muye.wp.wap.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by muye on 18/4/9.
 */
@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Auth
    @GetMapping("/payCash")
    public Result<String> payCash(){
        return Result.ok(accountService.payCash(SecurityConfig.getLoginId()));
    }
}
