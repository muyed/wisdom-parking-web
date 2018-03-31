package com.muye.wp.dao.domain;

import lombok.Data;

/**
 * Created by muye on 18/1/25.
 */
@Data
public class User extends BaseDomain {

    private String username;

    private String password;

    private String phone;

    private Integer type;

    private String realName;

    private String identityCard;
}
