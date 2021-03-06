package com.muye.wp.service.impl;

import com.muye.wp.common.cons.*;
import com.muye.wp.common.exception.WPException;
import com.muye.wp.common.utils.CommonUtil;
import com.muye.wp.dao.domain.*;
import com.muye.wp.dao.mapper.ParkingShareMapper;
import com.muye.wp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by muye on 18/4/3.
 */
@Service
public class ParkingShareServiceImpl implements ParkingShareService{

    @Autowired(required = false)
    private ParkingShareMapper parkingShareMapper;

    @Autowired
    private UserCarportService userCarportService;

    @Autowired
    private CarportService carportService;

    @Autowired
    private CommunityService communityService;

    @Autowired
    private UserCommunityService userCommunityService;

    @Override
    public ParkingShare queryByIdForUpdate(Long id) {
        return parkingShareMapper.selectByIdForUpdate(id);
    }

    @Override
    public void update(ParkingShare share) {
        parkingShareMapper.update(share);
    }

    @Override
    public boolean isExistByCarportAndTime(Long carportId, Date time) {
        ParkingShare query = new ParkingShare();
        query.setCarportId(carportId);
        query.addRange().putColumn("start_time").putLE(time)
                .and()
                .addRange().putColumn("stop_time").putGE(time);
        List<ParkingShare> shareList = parkingShareMapper.selectListByCondition(query, null);
        return shareList.stream()
                .filter(share -> share.getStatus() == ParkingShareStatus.MATCH.getStatus() ||
                    share.getStatus() == ParkingShareStatus.PAID.getStatus() ||
                    share.getStatus() == ParkingShareStatus.UNPAID.getStatus())
                .count() > 0;
    }

    @Override
    @Transactional
    public void publish(ParkingShare share) {

        //判断是否持有车锁
        if (!userCarportService.isPossess(share.getUserId(), share.getCarportId()))
            throw new WPException(RespStatus.RESOURCE_NOT_EXIST, "你未持有该车锁");

        //判断车锁在共享时间段内是否已经有共享单了
        if (isExistByCarportAndTime(share.getCarportId(), share.getStartTime())
                || isExistByCarportAndTime(share.getCarportId(), share.getStopTime()))
            throw new WPException(RespStatus.RESOURCE_EXISTED, "该时间段内已存在共享单");

        Carport carport = carportService.queryById(share.getCarportId());
        Community community = communityService.queryById(carport.getCommunityId());

        share.setCarportNum(carport.getCarportNum());
        share.setCarportMeid(carport.getMeid());
        share.setCommunityId(carport.getCommunityId());
        share.setCommunityType(community.getType());
        share.setCommunityName(community.getCommunityName());
        share.setProvince(community.getProvince());
        share.setCity(community.getCity());
        share.setArea(community.getArea());
        share.setAddr(community.getAddr());
        share.setLatitude(carport.getLatitude());
        share.setLongitude(carport.getLongitude());
        share.setStatus(ParkingShareStatus.MATCH.getStatus());

        share.setShareNum(CommonUtil.genPayNum(ProductType.PARKING_SHARE));

        parkingShareMapper.insert(share);

        carport.setShareStatus(CarportShareStatus.PUBLISH.getStatus());
        carportService.update(carport);
    }

    @Override
    @Transactional
    public void unPublish(Long userId, Long id) {
        ParkingShare share = queryByIdForUpdate(id);

        if (share == null)
            throw new WPException(RespStatus.RESOURCE_NOT_EXIST);

        if (!userCarportService.isPossess(userId, share.getCarportId()))
            throw new WPException(RespStatus.RESOURCE_NOT_EXIST, "你未持有该车锁");

        if (share.getStatus().equals(ParkingShareStatus.MATCH.getStatus()))
            throw new WPException(RespStatus.BUSINESS_ERR, "只支持取消待匹配的共享单");

        share.setStatus(ParkingShareStatus.CANCEL.getStatus());
        update(share);

        Carport carport = carportService.queryById(share.getCarportId());
        carport.setShareStatus(CarportShareStatus.UN_PUBLISH.getStatus());
        carportService.update(carport);
    }

    @Override
    public List<ParkingShare> queryListByDistance(Long userId, BigDecimal longitude, BigDecimal latitude, Long limit) {

        List<Long> communityIdList = new ArrayList<>();

        UserCommunity query = new UserCommunity();
        query.setUserId(userId);
        query.setType(UserCommunityType.PASS.getType());
        List<UserCommunity> userCommunityList = userCommunityService.queryByCondition(query, null);
        if (CollectionUtils.isEmpty(userCommunityList)){
            return parkingShareMapper.selectByDistance(longitude, latitude, null, limit);
        }

        userCommunityList.stream().forEach(userCommunity -> communityIdList.add(userCommunity.getCommunityId()));
        return parkingShareMapper.selectByDistance(longitude, latitude, communityIdList, limit);
    }
}
