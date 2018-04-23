package com.muye.wp.dao.domain;

import lombok.Data;

/**
 * Created by muye on 18/4/23.
 */
@Data
public class UserBank extends BaseDomain {

    private Long userId;
    private String bankName;
    private String bankAccount;
    private String accountName;
    private String bankAddr;
    private Integer bankCode;
}
