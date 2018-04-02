package com.muye.wp.common.rest;

import com.muye.wp.common.cons.RespStatus;
import com.muye.wp.common.exception.WPException;
import lombok.Data;

/**
 * Created by muye on 18/1/26.
 */
@Data
public class Result<T> {

    private T data;

    private int code;

    private String errMsg;

    private Result(T t){
        this.code = RespStatus.OK.getCode();
        this.data = t;
    }

    private Result(WPException e){
        this.code = e.getCode();
        this.errMsg = e.getMessage();
    }

    private Result(RespStatus code){
        this.code = code.getCode();
        this.errMsg = code.getMessage();
    }

    public static <T> Result ok(T t){
        return new Result(t);
    }

    public static Result ok(){
        return Result.ok(null);
    }

    public static Result fail(WPException e){
        return new Result(e);
    }

    public static Result fail(RespStatus code){
        return new Result(code);
    }
}
