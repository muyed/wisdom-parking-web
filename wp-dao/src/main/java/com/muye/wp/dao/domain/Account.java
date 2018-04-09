package com.muye.wp.dao.domain;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by muye on 18/4/9.
 */
@Data
public class Account extends BaseDomain {

    private Long userId;        //用户id
    private BigDecimal balance; //账户余额
    private BigDecimal cash;    //账户押金
}
