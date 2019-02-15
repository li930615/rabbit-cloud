package com.rabbit.gateway.componet.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.rabbit.auth.core.entity.AuthType;
import com.rabbit.auth.core.entity.Authorization;
import com.rabbit.common.bean.config.FilterUrlsPropertiesConfig;
import com.rabbit.common.constant.SecurityConst;
import com.rabbit.common.entity.CurrentUser;
import com.rabbit.feign.auth.model.R;
import com.rabbit.gateway.componet.config.RabbitAuthClientConfig;
import com.rabbit.gateway.service.AuthService;
import com.rabbit.gateway.service.PermissionService;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @ClassName AccessFilter
 * @Description API网关入口
 * @Author LZQ
 * @Date 2019/1/20 20:34
 **/
@Component
public class AccessFilter extends ZuulFilter {

    private static final Logger log = LoggerFactory.getLogger(AccessFilter.class);

    @Autowired
    private AuthService authService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RabbitAuthClientConfig rabbitAuthClientConfig;

    @Autowired
    private FilterUrlsPropertiesConfig filterUrlsPropertiesConfig;

    @Autowired
    private final RouteLocator routeLocator;
    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    /*该类型的filters在请求路由到目标源服务之前执行：一般用来实现Authentication、选择目标源服务地址等*/
    public String filterType() {
        return "pre";
    }

    /*表示该过滤器的优先级别（数字越大，优先级越低）*/
    public int filterOrder() {
        return -2;
    }

    /*代表该过滤器是否生效（返回true为生效）*/
    public boolean shouldFilter() {
        return true;
    }

    /*构造方法（定位服务源地址）*/
    public AccessFilter(RouteLocator routeLocator) {
        this.routeLocator = routeLocator;
    }

    public void loginTest(HttpServletRequest request) {
    }

    /*逻辑处理*/
    public Object run() {
        long startTimeMillis = System.currentTimeMillis();
        //获取当前请求的上下文
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.getResponse().setContentType("text/html;charset=UTF-8");
        HttpServletRequest request = ctx.getRequest();
        HttpServletResponse response = ctx.getResponse();
        //在上下文中加入已经通过网关验证的信息
        ctx.addZuulRequestHeader("is_after_gateway", "true");
        ctx.set("startTime", Long.valueOf(startTimeMillis));

        ctx.addZuulRequestHeader("request_protocol", request.isSecure() ? "https://" : "http://");
        //根据请求的URI路由到服务源
        Route route = this.routeLocator.getMatchingRoute(request.getRequestURI());
        //
        if (!checkServerState(ctx, route.getId())) {
            return null;
        }
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        String requestURI = request.getRequestURI();

        if (requestURI.equals("/auth/sso/login")) {
            loginTest(request);
        }

        String token = (String) ctx.get("Authorization");
        //如果是退出系统操作，则删除登录验证的缓存token
        if (requestURI.equals("/auth/sso/logout")) {
            this.authService.delSsoVerifyCache(token);
        }

        String uri = request.getRequestURI().substring(request.getContextPath().length());
        if (isMatCh(uri)) {
            return null;
        }
        log.info("请求URI：{}", requestURI);
        Authorization authorization = null;
        StringBuffer errMsg = new StringBuffer();
        if (StringUtils.isBlank(token)) {
            errMsg.append("Authorization is null");
        } else {
            //解析token携带的认证信息
            authorization = parseAuthorization(token);
            if (null == authorization) {
                errMsg.append("authorization parse fail");
            } else {
                //获取认证类型
                AuthType authType = authorization.getAuthType();
                //登录验证
                if (AuthType.SSO == authType) {
                    R r = this.authService.ssoVerify(token);
                    if (!r.success()) {
                        errMsg.append("sso verify fail");
                    }

                }

            }

        }

        /*返回验证信息*/
        if (StringUtils.isNotBlank(errMsg.toString())) {
            ctx.setSendZuulResponse(false);
            if (isAjax(request)) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("error", HttpStatus.UNAUTHORIZED.getReasonPhrase());
                jsonObj.put("message", errMsg.toString());
                ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
                ctx.setResponseBody(jsonObj.toString());
            } else {
                try {
                    String redirectURI = this.rabbitAuthClientConfig.server.concat("/sso/login") + "?server=" + request.getRequestURL().toString() + "&" + "client_id" + "=" + this.rabbitAuthClientConfig.clientId;
                    response.sendRedirect(redirectURI);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        boolean hasPermission = false;
        AuthType authType = authorization.getAuthType();
        Map ssoTokenMap = (Map) authorization.getAuthToken();
        switch (authType) {
            case SSO:
                /*当前请求为登录请求时，将当前用户信息放入网关请求中*/
                CurrentUser currentUser = new Gson().fromJson(ssoTokenMap.get("user").toString(), CurrentUser.class);
                ctx.addZuulRequestHeader("current_user", JSON.toJSONString(currentUser));
                //如果有登录权限则返回true
                if (!hasPermission && (this.permissionService.ssoHasPermission(route, currentUser)))
                    hasPermission = true;
                break;
            case API:
                /*如果为API接口调用，则根据路由地址和appId判断该请求是否有权限*/
                String appId = (String) ssoTokenMap.get("appId");
                if (this.permissionService.apiHasPermission(route, appId))
                    hasPermission = true;
                break;
        }

        log.info("gateway end 总耗时 {} ms", Long.valueOf(System.currentTimeMillis() - startTimeMillis));

        if (!hasPermission) {
            ctx.setSendZuulResponse(false);
            if (isAjax(request)) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("error", HttpStatus.FORBIDDEN.getReasonPhrase());
                jsonObj.put("message", "没有权限访问");
                ctx.setResponseStatusCode(HttpStatus.FORBIDDEN.value());
                ctx.setResponseBody(jsonObj.toString());
            } else {
                ctx.setResponseStatusCode(HttpStatus.FORBIDDEN.value());
                ctx.setResponseBody("没有权限访问");
            }
            return null;
        }
        return null;
    }

    /*解析认证信息*/
    private Authorization parseAuthorization(String token) {
        Authorization authorization = null;
        /*给用户加入一个自定义的“证书”*/
        Claims claims = SecurityConst.parseJWT(token);
        if (claims != null) {
            AuthType authType = new Gson().fromJson(claims.get("auth_type").toString(), AuthType.class);
            Object object = claims.get("auth_token");
            authorization = new Authorization(authType, object);
        }
        return authorization;
    }

    private boolean isAjax(HttpServletRequest request) {
        if (!"GET".equals(request.getMethod())) {
            return true;
        }
        String requestType = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(requestType)) {
            return true;
        }
        String accept = request.getHeader("Accept");
        if ((StringUtils.isBlank(accept)) || (!accept.contains("text/html"))) {
            return true;
        }
        return false;
    }

    /*获取配置文件中需要路由的url*/
    private boolean isMatCh(String url) {
        List<String> anonList = this.filterUrlsPropertiesConfig.getAnon();
        for (String anon : anonList) {
            if ((this.antPathMatcher.match(anon, url)) || (this.antPathMatcher.match("/zuul" + anon, url))) {
                return true;
            }
        }
        return false;
    }

    /*确定服务源状态是否正常*/
    private boolean checkServerState(RequestContext ctx, String serverId) {
        String serverStateParm = (String) ctx.get("SERVER_STATE");
        Boolean serverState = AuthorizationFilter.SERVER_STATE.get(serverId);
        //服务源状态为空时返回true
        if (null == serverState) {
            serverState = Boolean.valueOf(true);
        }
        if ("TRUE".equals(serverStateParm)) {
            serverState = Boolean.valueOf(true);
            AuthorizationFilter.SERVER_STATE.remove(serverId);
        } else if ("FALSE".equals(serverStateParm)) {
            serverState = Boolean.valueOf(false);
            AuthorizationFilter.SERVER_STATE.put(serverId, serverState);
        }
        //
        if (!serverState.booleanValue()) {
            ctx.setSendZuulResponse(false);
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("error", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            jsonObj.put("message", "服务被异常关闭，请联系李智强");
            ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
            ctx.setResponseBody(jsonObj.toString());
        }
        return serverState.booleanValue();
    }
}
