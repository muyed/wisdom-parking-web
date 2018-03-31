package com.muye.wp.dao.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by muye on 18/2/8.
 */
@Data
public class Reservation extends BaseDomain {

    private Long userId;
    private String reservationNum;
    private String plateNumber;
    private Integer status;
    private Date payDeadline;
    private Date startTime;
    private Date endTime;
    private Date parkingStartTime;
    private Date parkingEndTime;
    private Integer longOverdue;
    private Date tipOverdueTime;
    private Long carportId;
    private BigDecimal price;
    private BigDecimal amount;
    private BigDecimal overdueAmount;
    private String carportMeid;
    private Long communityId;
    private String province;
    private String city;
    private String area;
}
