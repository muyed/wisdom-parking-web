package com.muye.wp.dao.domain;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by muye on 18/3/1.
 */
@Data
public class CommunityModule extends BaseDomain {

    private Long communityId;
    private String moduleName;
    private BigDecimal longitude;
    private BigDecimal latitude;
}
