package com.muye.wp.dao.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by muye on 18/4/9.
 */
@Data
public class ParkingTicket extends BaseDomain {

    private Long userId;            //用户id
    private Long communityId;       //小区id
    private Long carportId;         //车位id
    private Long parkingShareId;    //共享单id
    private String ticketNum;       //停车单号
    private Integer status;         //状态

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date appointmentStartTime;  //预约停车开始时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date appointmentEndTime;    //预约停车结束时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;             //实际停车开始时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;               //实际停车结束时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payDeadlineTime;       //支付截止时间

    private String carLicense;          //停车车牌号
    private String phone;               //停车单联系号码
    private BigDecimal price;           //每小时价格
    private BigDecimal parkingFee;      //停车费用
    private Integer overdue;            //逾期时长（小时）
    private BigDecimal overdueFee;      //逾期费用
    private String carportNum;          //车位号
    private String carportMeid;         //车位设备号
    private String province;            //省
    private String city;                //市
    private String area;                //区
    private String openCode;            //开锁码
}
