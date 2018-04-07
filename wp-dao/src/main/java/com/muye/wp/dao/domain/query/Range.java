package com.muye.wp.dao.domain.query;

import lombok.Data;

/**
 * Created by muye on 18/4/3.
 */
@Data
public class Range<T> {

    private Query query;
    private String column;
    private T gt;   //大于
    private T lt;   //小于
    private T ge;   //大于等于
    private T le;   //小于等于

    public Range<T> putColumn(String column){
        this.column = column;
        return this;
    }

    public Range<T> putGT(T GT){
        this.gt = GT;
        return this;
    }

    public Range<T> putLT(T LT){
        this.lt = LT;
        return this;
    }

    public Range<T> putGE(T GE){
        this.ge = GE;
        return this;
    }

    public Range<T> putLE(T LE){
        this.le = LE;
        return this;
    }

    public Query and (){
        return query;
    }
}
