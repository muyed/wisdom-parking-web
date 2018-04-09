package com.muye.wp.common.cons;

/**
 * Created by muye on 18/4/9.
 */
public enum CommunityType {

    OPEN(1, "开放小区"), CLOSE(2, "封闭小区");

    private Integer type;
    private String message;

    CommunityType(Integer type, String message){
        this.type = type;
        this.message = message;
    }

    public Integer getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}
