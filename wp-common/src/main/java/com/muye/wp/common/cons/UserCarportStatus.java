package com.muye.wp.common.cons;

/**
 * Created by muye on 18/3/30.
 */
public enum UserCarportStatus {

    UNPAID(0, "待支付"), PAID(1, "已支付"), PAY_FAILED(2, "支付失败"), WITHDRAW(3, "已退款");

    private Integer status;
    private String message;

    UserCarportStatus(Integer status, String message){
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
