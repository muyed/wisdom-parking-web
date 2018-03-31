package com.muye.wp.dao.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by muye on 18/1/25.
 */
@Data
public class BaseDomain implements Serializable {

    private Long id;

    private Date createTime;

    private Date modifyTime;
}
