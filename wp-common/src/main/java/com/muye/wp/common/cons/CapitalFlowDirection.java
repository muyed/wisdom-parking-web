package com.muye.wp.common.cons;

/**
 * Created by muye on 18/3/30.
 */
public enum CapitalFlowDirection {

    IN(1), OUT(2);

    private Integer direction;

    CapitalFlowDirection(Integer direction){
        this.direction = direction;
    }

    public Integer getDirection() {
        return direction;
    }
}
