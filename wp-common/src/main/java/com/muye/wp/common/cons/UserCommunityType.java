package com.muye.wp.common.cons;

/**
 * Created by muye on 18/3/30.
 */
public enum UserCommunityType {

    VERIFYING(1, "审核中"), PASS(2, "审核通过"), NOPASS(3, "审核不通过"), DEL(4, "已删除");

    private Integer type;
    private String message;

    UserCommunityType(Integer type, String message){
        this.message = message;
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}
