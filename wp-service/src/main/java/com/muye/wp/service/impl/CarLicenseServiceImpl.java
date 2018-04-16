package com.muye.wp.service.impl;

import com.muye.wp.common.cons.RespStatus;
import com.muye.wp.common.cons.UserCommunityType;
import com.muye.wp.common.exception.WPException;
import com.muye.wp.dao.domain.CarLicense;
import com.muye.wp.dao.domain.UserCommunity;
import com.muye.wp.dao.mapper.CarLicenseMapper;
import com.muye.wp.embed.server.service.EmbedService;
import com.muye.wp.service.CarLicenseService;
import com.muye.wp.service.UserCommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muye on 18/4/2.
 */
@Service
public class CarLicenseServiceImpl implements CarLicenseService {

    @Autowired(required = false)
    private CarLicenseMapper carLicenseMapper;

    @Autowired
    private UserCommunityService userCommunityService;

    @Autowired
    private EmbedService embedService;

    @Override
    public List<CarLicense> queryListByUserId(Long userId) {
        return carLicenseMapper.selectListByUserId(userId);
    }

    @Override
    public void add(CarLicense carLicense) {

        if (carLicenseMapper.selectByUserIdAndLicense(carLicense.getUserId(), carLicense.getLicense()) != null){
            throw new WPException(RespStatus.RESOURCE_EXISTED);
        }

        carLicenseMapper.insert(carLicense);

        UserCommunity query = new UserCommunity();
        query.setUserId(carLicense.getUserId());
        query.setType(UserCommunityType.PASS.getType());
        List<UserCommunity> userCommunityList = userCommunityService.queryByCondition(query, null);
        List<String> carLicenseList = new ArrayList<>();
        carLicenseList.add(carLicense.getLicense());
        userCommunityList.forEach(userCommunity -> embedService.addCarLicense(userCommunity.getCommunityId(), carLicenseList));
    }

    @Override
    public void delete(Long id, Long userId) {

        CarLicense carLicense = carLicenseMapper.selectById(id);
        List<String> carLicenseList = new ArrayList<>();
        carLicenseList.add(carLicense.getLicense());

        carLicenseMapper.delete(id, userId);

        UserCommunity query = new UserCommunity();
        query.setUserId(carLicense.getUserId());
        query.setType(UserCommunityType.PASS.getType());
        List<UserCommunity> userCommunityList = userCommunityService.queryByCondition(query, null);
        userCommunityList.forEach(userCommunity -> embedService.delCarLicense(userCommunity.getCommunityId(), carLicenseList));
    }
}
