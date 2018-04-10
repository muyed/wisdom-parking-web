package com.muye.wp.common.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Created by muye on 18/4/10.
 */
public class DateUtil {

    /**
     * 两个时间相差多少小时 不满一小时向上取一小时
     */
    public static int ceilHours(Date before, Date after){
        LocalDateTime dateTime1 = before.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime dateTime2 = after.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return (int) Math.ceil(ChronoUnit.SECONDS.between(dateTime1, dateTime2) / 3600d);
    }

    /**
     * 两个时间相差多少分钟
     */
    public static int betweenMin(Date before, Date after){
        LocalDateTime dateTime1 = before.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime dateTime2 = after.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return (int) ChronoUnit.MINUTES.between(dateTime1, dateTime2);
    }
}
