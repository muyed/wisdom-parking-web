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

    private Long id;        //ticketId

    private String carLicense;      //车牌号

    @Override
    public String toString() {
        return "TempCarLicense{" +
                "id=" + id +
                ", carLicense='" + carLicense + '\'' +
                '}';
    }
}
