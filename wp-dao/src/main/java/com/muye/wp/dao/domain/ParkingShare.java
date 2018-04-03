package com.muye.wp.dao.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by muye on 18/4/3.
 */
@Data
public class ParkingShare {

    private String shareNum;    //共享单号
    private Long userId;        //共享单发布者
    private Long carportId;     //车位
    private Date startTime;     //共享开始时间
    private Date stopTime;      //共享结束时间
    private BigDecimal price;   //每小时价格
    private Integer status;     //共享单状态
    private String carportMeid; //车位锁设备码
    private String carportNum;  //车位号
    private Long communityId;  //小区id
    private String province;    //省
    private String city;        //市
    private String area;        //区
    private BigDecimal longitude;   //经度
    private BigDecimal latitude;    //纬度
}
