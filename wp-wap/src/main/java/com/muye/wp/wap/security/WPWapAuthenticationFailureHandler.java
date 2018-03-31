package com.muye.wp.wap.security;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.muye.wp.common.cons.RespStatus;
import com.muye.wp.common.exception.WPException;
import com.muye.wp.common.rest.Result;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by muye on 18/1/26.
 */
@Component
public class WPWapAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {

        PrintWriter writer;
        Result result = Result.fail(new WPException(RespStatus.WRONG_LOGIN));
        response.setContentType("text/json;charset=UTF-8");
        writer = response.getWriter();
        writer.write(JSONObject.toJSONString(result, SerializerFeature.WriteMapNullValue));
        writer.flush();
        writer.close();
    }
}
