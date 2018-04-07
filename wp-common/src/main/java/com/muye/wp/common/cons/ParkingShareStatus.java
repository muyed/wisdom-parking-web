package com.muye.wp.common.cons;

/**
 * Created by muye on 18/4/5.
 */
public enum ParkingShareStatus {

    MATCH(0, "待匹配"), UNPAID(1, "待支付"), PAID(2, "支付成功"), FINISH(3, "已完成"), CANCEL(4, "已取消");

    private Integer status;
    private String message;

    ParkingShareStatus(Integer status, String message){
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
