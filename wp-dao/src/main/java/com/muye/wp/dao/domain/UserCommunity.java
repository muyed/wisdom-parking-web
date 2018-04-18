package com.muye.wp.dao.domain;

import lombok.Data;

/**
 * Created by muye on 18/3/2.
 */
@Data
public class UserCommunity extends BaseDomain{

    private Long userId;
    private Long communityId;
    private Integer type;
    private String reason;
    private String floorNo;
    private String unitNo;
    private String houseNo;
}
