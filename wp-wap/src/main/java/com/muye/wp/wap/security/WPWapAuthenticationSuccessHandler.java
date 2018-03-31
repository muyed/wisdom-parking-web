package com.muye.wp.wap.security;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.muye.wp.common.rest.Result;
import com.muye.wp.wap.aop.CorsAop;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by muye on 18/1/26.
 */
@Component
public class WPWapAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private RequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        PrintWriter writer;
        Result<Map> result = Result.ok(null);
        response.setStatus(200);
        response.setContentType("text/json;charset=UTF-8");
        CorsAop.cors(response);
        writer = response.getWriter();
        writer.write(JSONObject.toJSONString(result, SerializerFeature.WriteMapNullValue));
        writer.flush();
        writer.close();
        requestCache.removeRequest(request, response);
        clearAuthenticationAttributes(request);
    }

    protected final void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
}
