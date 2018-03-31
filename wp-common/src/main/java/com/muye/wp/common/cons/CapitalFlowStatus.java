package com.muye.wp.common.cons;

/**
 * Created by muye on 18/3/30.
 */
public enum CapitalFlowStatus {

    ING(1, "执行中"), SUCCEED(2, "成功"), FAILED(3, "失败");

    private Integer status;
    private String message;

    CapitalFlowStatus(Integer status, String message){
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
