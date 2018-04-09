package com.muye.wp.dao.domain;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by muye on 18/3/29.
 */
@Data
public class CapitalFlow extends BaseDomain {

    private Long userId;
    private Integer direction;  //资金流向 1进账 2支出

    /**
     * @see com.muye.wp.common.cons.ProductType#type
     */
    private Integer type;       //类型
    private String orderNum;    //订单号
    private BigDecimal amount;  //金额
    private Integer status;     //状态    1执行中 2成功 3失败
}
