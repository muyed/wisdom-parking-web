package com.muye.wp.dao.domain;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by muye on 18/3/15.
 */
@Data
public class UserCarport extends BaseDomain {

    private Long userId;        //用户id
    private Long carportId;     //车位id
    private String alias;       //车锁别名
    private String payNum;      //押金支付单号
    private BigDecimal deposit; //押金
    private Integer status;     //0-待支付押金 1-押金支付成功 2-押金支付失败 3-已到期
    private Long parent;        //主账户id
}
