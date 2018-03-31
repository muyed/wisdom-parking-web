package com.muye.wp.common.exception;

import com.muye.wp.common.cons.RespStatus;

/**
 * Created by muye on 18/1/25.
 */
public class WPException extends RuntimeException {

    private int code;
    private String message;

    public WPException(RespStatus code){
        super(code.getMessage());
        this.code = code.getCode();
        this.message = code.getMessage();
    }

    public WPException(RespStatus code, String message){
        super(message);
        this.code = code.getCode();
        this.message = message;
    }

    public WPException(RespStatus code, Exception e){
        super(code.getMessage(), e);
        this.message = code.getMessage();
        this.code = code.getCode();
    }

    public int getCode() {
        return code;
    }
}
