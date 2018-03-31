package com.muye.wp.common.cons;

/**
 * Created by muye on 18/1/29.
 */
public enum RedisKey {
    REG_PHONE,
    REG_SMS,
    LOGIN_PHONE,
    LOGIN_SMS,
    ;

    @Override
    public String toString() {
        return this.name() + "_";
    }
}
