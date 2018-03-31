package com.muye.wp.wap.controller;

import com.muye.wp.wap.security.Auth;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by muye on 18/3/27.
 */
@RestController
@Auth
public class IndexController {

    @GetMapping("/")
    public String index(){
        return "welcome to jsppi";
    }
}
