package com.muye.wp.service.impl;

import com.muye.wp.common.cons.RespStatus;
import com.muye.wp.common.exception.WPException;
import com.muye.wp.dao.domain.ParkingShare;
import com.muye.wp.dao.domain.UserCarport;
import com.muye.wp.dao.mapper.ParkingShareMapper;
import com.muye.wp.service.ParkingShareService;
import com.muye.wp.service.UserCarportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by muye on 18/4/3.
 */
@Service
public class ParkingShareServiceImpl implements ParkingShareService{

    @Autowired(required = false)
    private ParkingShareMapper parkingShareMapper;

    @Autowired
    private UserCarportService userCarportService;

    @Override
    public boolean isExistByCarportAndTime(Long carportId, Date time) {
        return false;
    }

    @Override
    @Transactional
    public void publish(ParkingShare share) {

        //判断是否持有车锁
        if (!userCarportService.isPossess(share.getUserId(), share.getCarportId()))
            throw new WPException(RespStatus.RESOURCE_NOT_EXIST, "你未持有改车锁");

        //判断车锁在共享时间段内是否已经有共享单了
    }
}
