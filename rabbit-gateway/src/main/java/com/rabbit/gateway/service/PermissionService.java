package com.rabbit.gateway.service;

import com.rabbit.common.entity.CurrentUser;
import org.springframework.cloud.netflix.zuul.filters.Route;

/**
 * @ClassName PermissionService
 * @Description 权限验证
 * @Author LZQ
 * @Date 2019/1/20 20:40
 **/
public interface PermissionService {

    boolean ssoHasPermission(Route paramRoute, CurrentUser paramCurrentUser);

    boolean apiHasPermission(Route paramRoute, String paramString);
}
