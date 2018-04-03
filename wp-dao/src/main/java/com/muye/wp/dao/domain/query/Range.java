package com.muye.wp.dao.domain.query;

import lombok.Data;

/**
 * Created by muye on 18/4/3.
 */
@Data
public class Range<T> {

    private String column;
    private T GT;   //大于
    private T LT;   //小于
    private T GE;   //大于等于
    private T LE;   //小于等于

    public Range<T> putColumn(String column){
        this.column = column;
        return this;
    }

    public Range<T> putGT(T GT){
        this.GT = GT;
        return this;
    }

    public Range<T> putLT(T LT){
        this.LT = LT;
        return this;
    }

    public Range<T> putGE(T GE){
        this.GE = GE;
        return this;
    }

    public Range<T> putLE(T LE){
        this.LE = LE;
        return this;
    }
}
