package com.muye.wp.wap.controller;

import com.muye.wp.common.cons.UserType;
import com.muye.wp.common.rest.Result;
import com.muye.wp.dao.domain.ParkingShare;
import com.muye.wp.service.ParkingShareService;
import com.muye.wp.wap.security.Auth;
import com.muye.wp.wap.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

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

    @Auth(UserType.GENERAL)
    @PostMapping("/unPublish")
    public Result unPublish(@RequestBody ParkingShare share){
        parkingShareService.unPublish(SecurityConfig.getLoginId(), share.getId());
        return Result.ok();
    }

    @Auth(UserType.GENERAL)
    @GetMapping("/loadByDistance/{longitude}/{latitude}/{limit}")
    public Result<List<ParkingShare>> loadByDistance(@PathVariable BigDecimal longitude, @PathVariable BigDecimal latitude, @PathVariable Long limit){
        return Result.ok(parkingShareService.queryListByDistance(SecurityConfig.getLoginId(), longitude, latitude, limit));
    }
}
