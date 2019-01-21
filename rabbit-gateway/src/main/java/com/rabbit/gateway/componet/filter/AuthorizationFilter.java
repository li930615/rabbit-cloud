package com.rabbit.gateway.componet.filter;

import com.netflix.zuul.context.RequestContext;
import com.rabbit.auth.core.util.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

/**
 * @ClassName AuthorizationFilter
 * @Description TODO
 * @Author LZQ
 * @Date 2019/1/20 20:48
 **/
@Component("AuthorizationFilter")
public class AuthorizationFilter {

    private static final Logger log = LoggerFactory.getLogger(AuthorizationFilter.class);

    public static final String SERVER_STATE_PARM = "SERVER_STATE";

    public static Map<String, Boolean> SERVER_STATE = new Hashtable();

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (StringUtils.isBlank(token)) {
            token = CookieUtil.getValue(request, "Authorization");
        }
        if (StringUtils.isBlank(token)) {
            token = request.getParameter("Authorization");
        }
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.set("Authorization", token);
        ctx.set("SERVER_STATE", request.getParameter("SERVER_STATE"));
        filterChain.doFilter(request, response);
    }
}
