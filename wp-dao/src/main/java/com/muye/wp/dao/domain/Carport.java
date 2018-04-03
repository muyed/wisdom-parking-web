package com.muye.wp.dao.domain;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by muye on 18/3/6.
 */
@Data
public class Carport extends BaseDomain {

    private Long communityModuleId; //小区模块id
    private Long communityId;       //冗余小区id
    private String carportNum;      //车位号
    private String meid;            //设备编号
    private String bindCode;        //绑定码
    private BigDecimal longitude;   //经度
    private BigDecimal latitude;    //纬度
    private Integer shareStatus;    //共享状态
    private Integer lockStatus;     //地锁锁定状态
}
