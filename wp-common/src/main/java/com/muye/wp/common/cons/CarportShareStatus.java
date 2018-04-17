package com.muye.wp.common.cons;

/**
 * Created by muye on 18/4/17.
 */
public enum CarportShareStatus {

    UN_PUBLISH(0, "未发布"), PUBLISH(1, "已发布"), SHARE(2, "已共享");

    private int status;
    private String message;

    CarportShareStatus (int status, String message){
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
