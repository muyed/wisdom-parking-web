package com.muye.wp.wap.controller;

import com.muye.wp.common.cons.UserType;
import com.muye.wp.common.rest.Result;
import com.muye.wp.pay.mayi.MayiPay;
import com.muye.wp.wap.security.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by muye on 18/2/15.
 */
@RestController
@Auth
@RequestMapping("/api/pay")
public class PayController {

    private static final Logger logger = LoggerFactory.getLogger(PayController.class);

    @Autowired
    private MayiPay mayiPay;

    @Auth(UserType.GENERAL)
    @GetMapping(value = "/mayi/{orderNum}")
    public Result<String> mayiBindCarport(@PathVariable String orderNum){

        String payInfo = mayiPay.genPayInfo(orderNum);
        return Result.ok(payInfo);
    }

    @RequestMapping(value = "/mayi/callback")
    public String mayiCallback(HttpServletRequest request){

        mayiPay.callback(request);
        return "success";
    }
}
