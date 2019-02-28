package com.rabbit.gateway.service.impl;

import com.rabbit.common.entity.CurrentUser;
import com.rabbit.feign.ucenter.model.entity.SysApi;
import com.rabbit.feign.ucenter.model.entity.SysPermission;
import com.rabbit.feign.ucenter.server.SysLicenseApiServer;
import com.rabbit.gateway.componet.config.ZuulConfig;
import com.rabbit.gateway.service.PermissionService;
import com.rabbit.gateway.service.SysPermissionService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName PermissionServiceImpl
 * @Description TODO
 * @Author LZQ
 * @Date 2019/1/20 21:56
 **/
@Service(value = "permissionService")
public class PermissionServiceImpl implements PermissionService {

    private static final Logger log = LoggerFactory.getLogger(PermissionServiceImpl.class);

    private AntPathMatcher antPathMatcher = new AntPathMatcher();
    @Value(value = "${redis.cache.expiration:3600}")
    private Long expiration;
    @Autowired
    private SysPermissionService sysPermissionService;
    @Autowired
    private SysLicenseApiServer sysLicenseApiServer;
    @Autowired
    private ZuulConfig zuulConfig;

    /*判断是否具有登录权限*/
    public boolean ssoHasPermission(Route route, CurrentUser currentUser) {
        //从路由中获取请求的URI
        String reqServerURI = route.getPath();
        try {
            reqServerURI = reqServerURI.substring(reqServerURI.indexOf("/", 1));
        } catch (StringIndexOutOfBoundsException e) {
            reqServerURI = "";
        }
        //如果没有开启权限拦截，直接返回true
        if (!this.zuulConfig.permissionIntercept) {
            return true;
        }
        if (currentUser == null) {
            return false;
        }
        String appCode = null;
        String routeId = route.getId();
        appCode = routeId.lastIndexOf("-") == routeId.length() - 2 ? routeId.substring(0, routeId.length() - 2) : route.getId();
        List<SysPermission> permissionList = this.sysPermissionService.listByAppCodeAndUserId(appCode, currentUser.getId());
        if (permissionList != null) {
            for (SysPermission sysPermission : permissionList) {
                String sysPermissionCodes = sysPermission.getCode();
                if (!StringUtils.isNotBlank(sysPermissionCodes))
                    continue;
                List<String> sysPermissionCodeList = Arrays.asList(sysPermissionCodes.split(","));
                for (String sysPermissionCode : sysPermissionCodeList) {
                    String permissionURI = sysPermissionCode;
                    if (this.antPathMatcher.match(permissionURI, reqServerURI)) {
                        return true;
                    }
                    if (this.antPathMatcher.match(permissionURI, "/" + reqServerURI)) {
                        return true;
                    }
                    if (this.antPathMatcher.match(permissionURI, reqServerURI + "/")) {
                        return true;
                    }
                    if (!this.antPathMatcher.match("/" + permissionURI, reqServerURI)) continue;
                    return true;
                }
            }
        }
        log.info("权限不足:{}", reqServerURI);
        return false;
    }

    public boolean apiHasPermission(Route route, String appId) {
        String reqServerURI = route.getPath();
        try {
            reqServerURI = reqServerURI.substring(reqServerURI.indexOf("/", 1));
        } catch (StringIndexOutOfBoundsException e) {
            reqServerURI = "";
        }
        if (!this.zuulConfig.permissionIntercept) {
            return true;
        }
        if (StringUtils.isBlank((CharSequence) appId)) {
            return false;
        }
        List<SysApi> apiList = this.sysLicenseApiServer.listApiByAppId(appId);
        if (apiList != null && apiList.size() > 0) {
            for (SysApi sysApi : apiList) {
                String sysPermissionCodes;
                if (!"ENABLE".equals(sysApi.getState()) || !StringUtils.isNotBlank((CharSequence) (sysPermissionCodes = sysApi.getPermission())))
                    continue;
                List<String> permissionCodeList = Arrays.asList(sysPermissionCodes.split(","));
                for (String permissionCode : permissionCodeList) {
                    String permissionURI = permissionCode;
                    if (this.antPathMatcher.match(permissionURI, reqServerURI)) {
                        return true;
                    }
                    if (this.antPathMatcher.match(permissionURI, "/" + reqServerURI)) {
                        return true;
                    }
                    if (this.antPathMatcher.match(permissionURI, reqServerURI + "/")) {
                        return true;
                    }
                    if (!this.antPathMatcher.match("/" + permissionURI, reqServerURI)) continue;
                    return true;
                }
            }
        }
        log.info("权限不足:{}", reqServerURI);
        return false;
    }
}
