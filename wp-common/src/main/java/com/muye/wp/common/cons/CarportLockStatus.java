package com.muye.wp.common.cons;

/**
 * Created by muye on 18/4/17.
 */
public enum CarportLockStatus {

    LOCK(0, "已锁定"), OPEN(1, "已打开");

    private int status;
    private String message;

    CarportLockStatus(int status, String message){
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
