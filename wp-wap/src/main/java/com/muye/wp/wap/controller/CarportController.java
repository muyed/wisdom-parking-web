package com.muye.wp.wap.controller;

import com.alibaba.fastjson.JSONObject;
import com.muye.wp.common.cons.UserType;
import com.muye.wp.common.rest.Result;
import com.muye.wp.dao.domain.Carport;
import com.muye.wp.dao.domain.Community;
import com.muye.wp.dao.domain.UserCarport;
import com.muye.wp.dao.domain.ext.UserCarportExt;
import com.muye.wp.dao.page.Page;
import com.muye.wp.service.CarportService;
import com.muye.wp.service.CommunityService;
import com.muye.wp.service.UserCarportService;
import com.muye.wp.wap.security.Auth;
import com.muye.wp.wap.security.SecurityConfig;
import netscape.javascript.JSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @Autowired
    private CommunityService communityService;

    @Auth({UserType.OPERATOR, UserType.PROPERTY})
    @PostMapping("/add")
    public Result add(@RequestBody Carport carport){
        carportService.add(carport);
        return Result.ok(null);
    }

    @Auth({UserType.OPERATOR, UserType.PROPERTY})
    @GetMapping("/list")
    public Result<List<Carport>> list(Carport query, Page page){
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
        UserCarport query = new UserCarport();
        query.setUserId(SecurityConfig.getLoginId());

        List<UserCarport> userCarportList = userCarportService.queryByCondition(query, null);
        List<JSONObject> voList = new ArrayList<>(userCarportList.size());

        userCarportList.forEach(userCarport -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", userCarport.getUserId());
            jsonObject.put("carportId", userCarport.getCarportId());
            jsonObject.put("alias", userCarport.getAlias());
            jsonObject.put("payNum", userCarport.getPayNum());
            jsonObject.put("deposit", userCarport.getDeposit());
            jsonObject.put("status", userCarport.getStatus());
            jsonObject.put("parent", userCarport.getParent());

            Carport carport = carportService.queryById(userCarport.getCarportId());
            Community community = communityService.queryById(carport.getCommunityId());

            jsonObject.put("province", community.getProvince());
            jsonObject.put("city", community.getCity());
            jsonObject.put("area", community.getArea());
            jsonObject.put("addr", community.getAddr());
            jsonObject.put("communityName", community.getCommunityName());
            voList.add(jsonObject);
        });

        return Result.ok(voList);
    }

    @Auth(UserType.GENERAL)
    @PostMapping("/changeAlias")
    public Result changeAlias(@RequestBody UserCarport userCarport){
        userCarportService.changeAlias(SecurityConfig.getLoginId(), userCarport.getId(), userCarport.getAlias());
        return Result.ok(null);
    }

    @Auth(value = {UserType.GENERAL, UserType.OPERATOR, UserType.PROPERTY})
    @PostMapping("/lock")
    public Result lock(@RequestBody Carport carport){
        carportService.lock(SecurityConfig.getLoginId(), carport.getId());
        return Result.ok();
    }

    @Auth(value = {UserType.GENERAL, UserType.OPERATOR, UserType.PROPERTY})
    @PostMapping("/unLock")
    public Result unLock(@RequestBody Carport carport){
        carportService.unLock(SecurityConfig.getLoginId(), carport.getId());
        return Result.ok();
    }
}
