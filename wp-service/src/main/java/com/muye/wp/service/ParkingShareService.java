package com.muye.wp.service;

import com.muye.wp.dao.domain.ParkingShare;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by muye on 18/4/3.
 */
public interface ParkingShareService {

    void publish(ParkingShare share);

    void unPublish(Long userId, Long id);

    /**
     * 指定车锁指定时间是否存在共享单
     */
    boolean isExistByCarportAndTime(Long carportId, Date time);

    /**
     * 根据指定经纬度查询距离最近的共享单
     */
    List<ParkingShare> queryListByDistance(BigDecimal longitude, BigDecimal latitude, Long limit);
}
