package com.muye.wp.service;

import com.muye.wp.dao.domain.ParkingShare;

import java.util.Date;

/**
 * Created by muye on 18/4/3.
 */
public interface ParkingShareService {

    void publish(ParkingShare share);

    /**
     * 指定车锁指定时间是否存在共享单
     */
    boolean isExistByCarportAndTime(Long carportId, Date time);
}
