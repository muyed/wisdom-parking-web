package com.muye.wp.embed.mode;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by muye on 18/4/12.
 *
 * 临时车牌模型
 */
@Data
public class TempCarLicense implements Serializable {

    private Long id;

    private String carLicense;      //车牌号

    private int start;             //有效期开始时间 yyyyMMddHHmm

    private int end;               //有效期终止时间 yyyyMMddHHmm

}
