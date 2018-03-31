package com.muye.wp.service.impl;

import com.muye.wp.common.cons.RespStatus;
import com.muye.wp.common.cons.UserCommunityType;
import com.muye.wp.common.exception.WPException;
import com.muye.wp.dao.domain.Carport;
import com.muye.wp.dao.domain.UserCommunity;
import com.muye.wp.dao.domain.query.UserCommunityQuery;
import com.muye.wp.dao.mapper.UserCommunityMapper;
import com.muye.wp.dao.page.Page;
import com.muye.wp.service.CarportService;
import com.muye.wp.service.CommunityService;
import com.muye.wp.service.UserCommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by muye on 18/3/5.
 */
@Service
public class UserCommunityServiceImpl implements UserCommunityService {

    @Autowired
    private UserCommunityMapper userCommunityMapper;

    @Autowired
    private CommunityService communityService;

    @Autowired
    private CarportService carportService;

    @Override
    public void userAuth(UserCommunity userCommunity) {

        if (communityService.queryById(userCommunity.getCommunityId()) == null)
            throw new WPException(RespStatus.RESOURCE_NOT_EXIST);

        UserCommunityQuery query = new UserCommunityQuery();
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
        UserCommunity userCommunity = new UserCommunity();
        userCommunity.setId(id);
        userCommunity.setType(type);
        userCommunityMapper.updateSelective(userCommunity);
    }

    @Override
    public List<UserCommunity> queryByCondition(UserCommunityQuery query, Page page) {
        return userCommunityMapper.selectByCondition(query, page);
    }

    @Override
    public Boolean isAtUserCommunity(Long carportId, Long userId) {
        Carport carport = carportService.queryById(carportId);
        if (carport == null) return false;
        UserCommunityQuery query = new UserCommunityQuery();
        query.setUserId(userId);
        query.setCommunityId(carport.getCommunityId());
        query.setType(UserCommunityType.PASS.getType());
        if (CollectionUtils.isEmpty(userCommunityMapper.selectByCondition(query, null))) return false;
        return true;
    }
}
