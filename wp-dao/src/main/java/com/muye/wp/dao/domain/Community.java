package com.muye.wp.dao.domain;

import lombok.Data;

/**
 * Created by muye on 18/2/28.
 *
 * 小区实体类
 */
@Data
public class Community extends BaseDomain {

    private String communityName;   //小区名称
    private Integer type;           //小区类型
    private String province;        //小区所在省
    private String city;            //小区所在市
    private String area;            //小区所在区
    private String addr;            //小区详细地址
}
