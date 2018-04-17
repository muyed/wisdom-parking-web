package com.muye.wp.common.cons;

/**
 * Created by muye on 18/4/9.
 */
public enum ParkingTicketStatus {

    UNPAID(0, "待支付"),
    PAID(1, "已支付"),
    PARKING(2, "停车中"),
    FINISH(3, "正常完成"),
    OVERDUE_UNPAID(4, "逾期完结未支付"),
    OVERDUE_PAID(5, "逾期完结已支付"),
    CANCEL(6, "已取消");

    private Integer status;
    private String message;

    ParkingTicketStatus(Integer status, String message){
        this.status = status;
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
