package com.muye.wp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.muye.wp.common.cons.RespStatus;
import com.muye.wp.common.cons.UserCommunityType;
import com.muye.wp.common.cons.UserType;
import com.muye.wp.common.exception.WPException;
import com.muye.wp.dao.domain.*;
import com.muye.wp.dao.domain.ext.UserCommunityVO;
import com.muye.wp.dao.mapper.UserCommunityMapper;
import com.muye.wp.dao.page.Page;
import com.muye.wp.embed.server.door.service.DoorEmbedService;
import com.muye.wp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muye on 18/3/5.
 */
@Service
public class UserCommunityServiceImpl implements UserCommunityService {

    @Autowired(required = false)
    private UserCommunityMapper userCommunityMapper;

    @Autowired
    private CommunityService communityService;

    @Autowired
    private CarportService carportService;

    @Autowired
    private CarLicenseService carLicenseService;

    @Autowired
    private DoorEmbedService doorEmbedService;

    @Autowired
    private UserService userService;

    @Override
    public void userAuth(UserCommunity userCommunity) {

        if (communityService.queryById(userCommunity.getCommunityId()) == null)
            throw new WPException(RespStatus.RESOURCE_NOT_EXIST);

        UserCommunity query = new UserCommunity();
        query.setUserId(userCommunity.getUserId());
        query.setCommunityId(userCommunity.getCommunityId());
        query.setType(userCommunity.getType());
        if (userCommunityMapper.selectByCondition(query, null).size() > 0){
            throw new WPException(RespStatus.RESOURCE_EXISTED);
        }
        userCommunityMapper.insert(userCommunity);
    }

    @Override
    public void userAudit(Long id, Integer type) {
        UserCommunity userCommunity = userCommunityMapper.selectByIdForUpdate(id);
        userCommunity.setType(type);
        userCommunityMapper.updateSelective(userCommunity);

        List<CarLicense> carLicenseList = carLicenseService.queryListByUserId(userCommunity.getUserId());
        List<String> carLicenseNumList = new ArrayList<>(carLicenseList.size());
        carLicenseList.forEach(carLicense -> carLicenseNumList.add(carLicense.getLicense()));
        if (UserCommunityType.PASS.getType().equals(type)){
            doorEmbedService.addCarLicense(userCommunity.getCommunityId(), carLicenseNumList);
        }else {
            doorEmbedService.delCarLicense(userCommunity.getCommunityId(), carLicenseNumList);
        }

    }

    @Override
    public List<UserCommunity> queryByCondition(UserCommunity query, Page page) {
        return userCommunityMapper.selectByCondition(query, page);
    }

    @Override
    public Boolean isAtUserCommunity(Long carportId, Long userId) {
        Carport carport = carportService.queryById(carportId);
        if (carport == null) return false;
        UserCommunity query = new UserCommunity();
        query.setUserId(userId);
        query.setCommunityId(carport.getCommunityId());
        query.setType(UserCommunityType.PASS.getType());
        if (CollectionUtils.isEmpty(userCommunityMapper.selectByCondition(query, null))) return false;
        return true;
    }

    @Override
    public List<UserCommunityVO> userCommunityVO(UserCommunity query, Page page) {

        List<UserCommunity> list = queryByCondition(query, page);

        List<UserCommunityVO> voList = new ArrayList<>(list.size());
        list.forEach(userCommunity -> {
            Community community = communityService.queryById(userCommunity.getCommunityId());
            List<Carport> carportList = carportService.queryListByUserIdAndCommunityId(userCommunity.getUserId(), userCommunity.getCommunityId());
            UserCommunityVO vo = new UserCommunityVO();
            vo.setCommunityId(userCommunity.getCommunityId());
            vo.setType(userCommunity.getType());
            vo.setReason(userCommunity.getReason());
            vo.setCommunityName(community.getCommunityName());
            vo.setCommunityType(community.getType());
            vo.setProvince(community.getProvince());
            vo.setCity(community.getCity());
            vo.setArea(community.getArea());
            vo.setAddr(community.getAddr());
            vo.setCarportList(carportList);
            voList.add(vo);
        });


        return voList;
    }
}
