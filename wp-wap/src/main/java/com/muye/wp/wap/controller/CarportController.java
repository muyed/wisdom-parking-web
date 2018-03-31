package com.muye.wp.wap.controller;

import com.muye.wp.common.cons.UserType;
import com.muye.wp.common.rest.Result;
import com.muye.wp.dao.domain.Carport;
import com.muye.wp.dao.domain.UserCarport;
import com.muye.wp.dao.domain.ext.UserCarportExtend;
import com.muye.wp.service.CarportService;
import com.muye.wp.service.UserCarportService;
import com.muye.wp.wap.security.Auth;
import com.muye.wp.wap.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by muye on 18/3/8.
 */
@RestController
@RequestMapping("/api/carport")
public class CarportController {

    @Autowired
    private CarportService carportService;

    @Autowired
    private UserCarportService userCarportService;

    @Auth({UserType.OPERATOR, UserType.PROPERTY})
    @PostMapping("/add")
    public Result add(@RequestBody Carport carport){
        carportService.add(carport);
        return Result.ok(null);
    }

    @Auth({UserType.OPERATOR, UserType.PROPERTY})
    @PutMapping("/refreshBindCode")
    public Result<String> refreshBindCode(@RequestBody Carport carport){
        return Result.ok(carportService.refreshBindCode(carport.getId()));
    }


    @Auth(UserType.GENERAL)
    @PostMapping("/bind")
    public Result<String> bind(@RequestBody UserCarportExtend userCarport){
        userCarport.setUserId(SecurityConfig.getLoginId());
        return Result.ok(userCarportService.bind(userCarport));
    }
}
