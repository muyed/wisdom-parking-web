package com.muye.wp.wap.controller;

import com.muye.wp.common.cons.UserType;
import com.muye.wp.common.rest.Result;
import com.muye.wp.dao.domain.Carport;
import com.muye.wp.dao.domain.UserCarport;
import com.muye.wp.dao.domain.ext.UserCarportExt;
import com.muye.wp.dao.domain.query.CarportQuery;
import com.muye.wp.dao.domain.query.UserCarportQuery;
import com.muye.wp.dao.page.Page;
import com.muye.wp.service.CarportService;
import com.muye.wp.service.UserCarportService;
import com.muye.wp.wap.security.Auth;
import com.muye.wp.wap.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/list")
    public Result<List<Carport>> list(CarportQuery query, Page page){
        return Result.ok(carportService.queryListByCondition(query, page));
    }

    @Auth({UserType.OPERATOR, UserType.PROPERTY})
    @PutMapping("/refreshBindCode")
    public Result<String> refreshBindCode(@RequestBody Carport carport){
        return Result.ok(carportService.refreshBindCode(carport.getId()));
    }


    @Auth(value = UserType.GENERAL, cret = true)
    @PostMapping("/bind")
    public Result<String> bind(@RequestBody UserCarportExt userCarport){
        userCarport.setUserId(SecurityConfig.getLoginId());
        return Result.ok(userCarportService.bind(userCarport));
    }

    @Auth(UserType.GENERAL)
    @GetMapping("/myCarport")
    public Result<List<UserCarport>> myCarport(){
        UserCarportQuery query = new UserCarportQuery();
        query.setUserId(SecurityConfig.getLoginId());
        return Result.ok(userCarportService.queryByCondition(query, null));
    }

    @Auth(UserType.GENERAL)
    @PostMapping("/changeAlias")
    public Result changeAlias(@RequestBody UserCarport userCarport){
        userCarportService.changeAlias(SecurityConfig.getLoginId(), userCarport.getId(), userCarport.getAlias());
        return Result.ok(null);
    }
}
