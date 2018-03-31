package com.muye.wp.wap.aop;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by muye on 18/3/29.
 */
@Component
@WebFilter(urlPatterns = "/*")
public class CorsAop implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        CorsAop.cors((HttpServletResponse) response);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

    public static void cors(HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin","http://localhost:8000");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
    }
}
