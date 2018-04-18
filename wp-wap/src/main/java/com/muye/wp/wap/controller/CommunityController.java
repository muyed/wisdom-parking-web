package com.muye.wp.wap.controller;

import com.alibaba.fastjson.JSONObject;
import com.muye.wp.common.cons.UserCommunityType;
import com.muye.wp.common.cons.UserType;
import com.muye.wp.common.rest.Result;
import com.muye.wp.dao.domain.*;
import com.muye.wp.dao.page.Page;
import com.muye.wp.service.*;
import com.muye.wp.wap.security.Auth;
import com.muye.wp.wap.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muye on 18/3/1.
 */
@RestController
@RequestMapping("/api/community")
public class CommunityController {

    @Autowired
    private CommunityService communityService;

    @Autowired
    private CommunityModuleService communityModuleService;

    @Autowired
    private UserCommunityService userCommunityService;

    @Autowired
    private UserService userService;

    @Autowired
    private CarportService carportService;

    /**
     * 添加小区
     */
    @Auth(UserType.OPERATOR)
    @PostMapping
    public Result add(@RequestBody Community community){
        communityService.add(community);
        return Result.ok(null);
    }

    /**
     *  修改小区
     */
    @Auth(UserType.OPERATOR)
    @PutMapping
    public Result update(@RequestBody Community community){
        communityService.update(community);
        return Result.ok(null);
    }

    /**
     * 删除小区
     */
    @Auth(UserType.OPERATOR)
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id){
        communityService.delete(id);
        return Result.ok(null);
    }

    /**
     * 查询小区列表
     */
    @Auth({UserType.OPERATOR, UserType.GENERAL, UserType.PROPERTY})
    @GetMapping("/list")
    public Result<List<Community>> list(Community query, Page page){
        return Result.ok(communityService.queryByCondition(query, page));
    }

    /**
     * 添加小区模块
     */
    @Auth(UserType.OPERATOR)
    @PostMapping("/module")
    public Result addModule(@RequestBody CommunityModule communityModule){
        communityModuleService.add(communityModule);
        return Result.ok(null);
    }

    /**
     * 修改小区模块
     */
    @Auth(UserType.OPERATOR)
    @PutMapping("/module")
    public Result updateModule(@RequestBody CommunityModule communityModule){
        communityModuleService.update(communityModule);
        return Result.ok(null);
    }

    /**
     * 删除小区模块
     */
    @Auth(UserType.OPERATOR)
    @DeleteMapping("/module/{id}")
    public Result deleteModule(@PathVariable Long id){
        communityModuleService.delete(id);
        return Result.ok(null);
    }

    /**
     * 根据小区查询小区模块列表
     */
    @Auth({UserType.GENERAL, UserType.OPERATOR, UserType.PROPERTY})
    @GetMapping("/module/{communityId}/{pageNo}/{pageSize}")
    public Result<List<CommunityModule>> moduleListByCommunity(@PathVariable Long communityId,
                                                               @PathVariable Integer pageNo,
                                                               @PathVariable Integer pageSize){
        CommunityModule query = new CommunityModule();
        query.setCommunityId(communityId);
        Page page = new Page();
        page.setPageNo(pageNo);
        page.setPageSize(pageSize);
        return Result.ok(communityModuleService.queryByCondition(query, page));
    }

    /**
     * 用户发起认证为小区业主
     */
    @Auth(UserType.GENERAL)
    @PostMapping("/user/auth")
    public Result userAuth(@RequestBody UserCommunity userCommunity){
        userCommunity.setUserId(SecurityConfig.getLoginId());
        userCommunity.setType(UserCommunityType.VERIFYING.getType());
        userCommunityService.userAuth(userCommunity);
        return Result.ok(null);
    }

    /**
     * 物业或运营审核用户发起的小区业主认证请求
     */
    @Auth({UserType.OPERATOR, UserType.PROPERTY})
    @PutMapping("/user/audit")
    public Result userAudit(@RequestBody UserCommunity userCommunity){
        userCommunityService.userAudit(userCommunity.getId(), userCommunity.getType());
        return Result.ok(null);
    }

    /**
     * 条件查询用户小区关联信息
     */
    @Auth({UserType.GENERAL, UserType.OPERATOR, UserType.PROPERTY})
    @GetMapping("/user/list")
    public Result<List<JSONObject>> userList(UserCommunity query, Page page){

        //前端用户只能查询自己的记录
        User user = userService.queryById(SecurityConfig.getLoginId());
        if (user.getType() == UserType.GENERAL.getType()){
            query.setUserId(user.getId());
        }

        List<UserCommunity> list = userCommunityService.queryByCondition(query, page);

        List<JSONObject> vo = new ArrayList<>(list.size());
        list.forEach(userCommunity -> {
            Community community = communityService.queryById(userCommunity.getCommunityId());
            List<Carport> carportList = carportService.queryListByUserIdAndCommunityId(userCommunity.getUserId(), userCommunity.getCommunityId());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("communityId", userCommunity.getCommunityId());
            jsonObject.put("type", userCommunity.getType());
            jsonObject.put("reason", userCommunity.getReason());
            jsonObject.put("communityName", community.getCommunityName());
            jsonObject.put("communityType", community.getType());
            jsonObject.put("province", community.getProvince());
            jsonObject.put("city", community.getCity());
            jsonObject.put("area", community.getArea());
            jsonObject.put("addr", community.getAddr());
            jsonObject.put("carportList", carportList);
            vo.add(jsonObject);
        });

        return Result.ok(vo);
    }
}
