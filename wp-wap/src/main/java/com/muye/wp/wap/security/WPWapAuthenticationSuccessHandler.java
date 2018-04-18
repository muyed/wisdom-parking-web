package com.muye.wp.wap.security;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.muye.wp.common.cons.UserType;
import com.muye.wp.common.rest.Result;
import com.muye.wp.dao.domain.Account;
import com.muye.wp.dao.domain.User;
import com.muye.wp.dao.domain.UserCarport;
import com.muye.wp.dao.domain.UserCommunity;
import com.muye.wp.dao.domain.ext.UserCommunityVO;
import com.muye.wp.service.AccountService;
import com.muye.wp.service.UserCarportService;
import com.muye.wp.service.UserCommunityService;
import com.muye.wp.service.UserService;
import com.muye.wp.wap.aop.CorsAop;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;
import java.util.Map;

/**
 * Created by muye on 18/1/26.
 */
@Component
public class WPWapAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private RequestCache requestCache = new HttpSessionRequestCache();

    @Autowired
    private UserCommunityService userCommunityService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserCarportService userCarportService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        User user = userService.queryById(SecurityConfig.getLoginId());
        Result<JSONObject> result;

        if (user.getType().intValue() == UserType.GENERAL.getType()){
            JSONObject jsonObject = new JSONObject();
            UserCommunity userCommunityQuery = new UserCommunity();
            userCommunityQuery.setUserId(user.getId());
            List<UserCommunityVO> communityList = userCommunityService.userCommunityVO(userCommunityQuery, null);

            Account account = accountService.queryByUserId(user.getId());

            UserCarport userCarportQuery = new UserCarport();
            userCarportQuery.setUserId(user.getId());
            List<UserCarport> userCarportList = userCarportService.queryByCondition(userCarportQuery, null);

            jsonObject.put("communityList", communityList);
            jsonObject.put("account", account);
            jsonObject.put("userCarportList", userCarportList);

            result = Result.ok(jsonObject);

        }else {
            result = Result.ok();
        }

        PrintWriter writer;
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
