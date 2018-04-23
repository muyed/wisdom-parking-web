package com.muye.wp.service.impl;

import com.muye.wp.common.cons.*;
import com.muye.wp.common.exception.WPException;
import com.muye.wp.common.utils.CommonUtil;
import com.muye.wp.dao.domain.CapitalFlow;
import com.muye.wp.dao.domain.Carport;
import com.muye.wp.dao.domain.UserCarport;
import com.muye.wp.dao.domain.ext.UserCarportExt;
import com.muye.wp.dao.mapper.UserCarportMapper;
import com.muye.wp.dao.page.Page;
import com.muye.wp.service.CapitalFlowService;
import com.muye.wp.service.CarportService;
import com.muye.wp.service.UserCarportService;
import com.muye.wp.service.UserCommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

/**
 * Created by muye on 18/3/15.
 */
@Service
public class UserCarportServiceImpl implements UserCarportService {

    @Autowired(required = false)
    private UserCarportMapper userCarportMapper;

    @Autowired
    private UserCommunityService userCommunityService;

    @Autowired
    private CarportService carportService;

    @Autowired
    private CapitalFlowService capitalFlowService;

    @Override
    public List<UserCarport> queryByCondition(UserCarport query, Page page) {
        return userCarportMapper.selectByCondition(query, page);
    }

    @Override
    public UserCarport queryById(Long id) {
        return userCarportMapper.selectById(id);
    }

    @Override
    public UserCarport queryByIdForUpdate(Long id) {
        return userCarportMapper.selectByIdForUpdate(id);
    }

    @Override
    public UserCarport queryByPayNum(String payNum) {
        return userCarportMapper.queryByPayNum(payNum);
    }

    @Override
    public void update(UserCarport userCarport) {
        userCarportMapper.update(userCarport);
    }

    @Override
    @Transactional
    public String bind(UserCarportExt userCarport) {
        Carport carport = carportService.queryById(userCarport.getCarportId());
        if (carport == null){
            throw new WPException(RespStatus.RESOURCE_NOT_EXIST, "车位不存在");
        }
        if (!userCommunityService.isAtUserCommunity(userCarport.getCarportId(), userCarport.getUserId())){
            throw new WPException(RespStatus.AUTH_CARPORT_FAIL, "您不在车位所在小区,无法进行认证");
        }
        if (!carport.getBindCode().equals(userCarport.getBindCode())){
            throw new WPException(RespStatus.AUTH_CARPORT_FAIL, "绑定码不正确");
        }

        //幂等
        UserCarport query = new UserCarport();
        query.setCarportId(userCarport.getCarportId());
        query.setUserId(userCarport.getUserId());
        List<UserCarport> list = queryByCondition(query, null);
        Optional<UserCarport> optional = list.stream()
                .filter(uc -> uc.getStatus() == UserCarportStatus.PAID.getStatus() || uc.getStatus() == UserCarportStatus.UNPAID.getStatus())
                .findFirst();
        if (optional.isPresent()){
            throw new WPException(RespStatus.RESOURCE_EXISTED);
        }
        userCarport.setPayNum(CommonUtil.genPayNum(ProductType.CARPORT_DEPOSIT));
        userCarport.setStatus(UserCarportStatus.UNPAID.getStatus());
        userCarport.setDeposit(SysConfig.CARPORT_DEPOSIT);
        userCarportMapper.insert(userCarport);

        capitalFlowService.add(userCarport.getUserId(), CapitalFlowDirection.OUT, ProductType.CARPORT_DEPOSIT,
                userCarport.getPayNum(), userCarport.getDeposit());

        return userCarport.getPayNum();
    }

    @Override
    public void changeAlias(Long userId, Long userCarportId, String alias) {
        UserCarport userCarport = queryById(userCarportId);
        if (userCarport == null || !userCarport.getUserId().equals(userId)){
            throw new WPException(RespStatus.RESOURCE_NOT_EXIST);
        }
        userCarport.setAlias(alias);
        update(userCarport);
    }

    @Override
    public boolean isPossess(Long userId, Long carportId) {
        UserCarport query = new UserCarport();
        query.setUserId(userId);
        query.setCarportId(carportId);
        query.setStatus(UserCarportStatus.PAID.getStatus());
        return !CollectionUtils.isEmpty(queryByCondition(query, null));
    }
}
