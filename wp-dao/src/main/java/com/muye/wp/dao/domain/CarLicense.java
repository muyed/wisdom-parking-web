package com.muye.wp.dao.domain;

import lombok.Data;

/**
 * Created by muye on 18/4/2.
 */
@Data
public class CarLicense extends BaseDomain {

    private Long userId;    //用户id
    private String license; //车牌号
}
