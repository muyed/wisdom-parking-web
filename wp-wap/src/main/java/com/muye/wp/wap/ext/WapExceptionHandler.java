package com.muye.wp.wap.ext;

import com.muye.wp.common.cons.RespStatus;
import com.muye.wp.common.exception.WPException;
import com.muye.wp.common.rest.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by muye on 18/2/1.
 */
@ControllerAdvice
public class WapExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(WapExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result handler(Exception e){
        e.printStackTrace();
        logger.error("message:{}, e: {}", e.getMessage(), e);
        if (e instanceof WPException){
            return Result.fail((WPException) e);
        }

        return Result.fail(new WPException(RespStatus.SYS_ERR, e));
    }
}
