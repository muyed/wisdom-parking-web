package com.muye.wp.wap.controller;

import com.muye.wp.common.cons.UserType;
import com.muye.wp.common.rest.Result;
import com.muye.wp.dao.domain.CarLicense;
import com.muye.wp.service.CarLicenseService;
import com.muye.wp.wap.security.Auth;
import com.muye.wp.wap.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by muye on 18/4/2.
 */
@RestController
@RequestMapping("/api/carLicense")
@Auth(UserType.GENERAL)
public class CarLicenseController {

    @Autowired
    private CarLicenseService carLicenseService;

    @GetMapping("/myCarLicense")
    public Result<List<CarLicense>> myCarLicense(){
        return Result.ok(carLicenseService.queryListByUserId(SecurityConfig.getLoginId()));
    }

    @PostMapping("/add")
    public Result add(@RequestBody CarLicense carLicense){
        carLicense.setUserId(SecurityConfig.getLoginId());
        carLicenseService.add(carLicense);
        return Result.ok();
    }

    @DeleteMapping("/del/{id}")
    public Result del(@PathVariable Long id){
        carLicenseService.delete(id, SecurityConfig.getLoginId());
        return Result.ok();
    }
}
