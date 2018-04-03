package com.muye.wp.wap.controller;

import com.muye.wp.common.cons.UserType;
import com.muye.wp.common.rest.Result;
import com.muye.wp.dao.domain.ParkingShare;
import com.muye.wp.service.ParkingShareService;
import com.muye.wp.wap.security.Auth;
import com.muye.wp.wap.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by muye on 18/4/3.
 */
@RestController
@RequestMapping("/api/share")
public class ParkingShareController {

    @Autowired
    private ParkingShareService parkingShareService;

    @Auth(UserType.GENERAL)
    @PostMapping("/publish")
    public Result publish(@RequestBody ParkingShare share){
        share.setUserId(SecurityConfig.getLoginId());
        parkingShareService.publish(share);
        return Result.ok();
    }
}
