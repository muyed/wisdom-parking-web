package com.muye.wp.dao.domain;

import lombok.Data;

/**
 * Created by muye on 18/6/4.
 */
@Data
public class Notice extends BaseDomain {

    private String title;

    private String body;

    private Integer status;
}
