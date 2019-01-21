package com.rabbit.gateway.service;

import com.rabbit.feign.ucenter.model.entity.SysPermission;
import com.rabbit.feign.ucenter.server.SysPermissionServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName SysPermissionService
 * @Description TODO
 * @Author LZQ
 * @Date 2019/1/20 21:57
 **/
@Service("sysPermissionService")
public class SysPermissionService {

    @Autowired
    private SysPermissionServer sysPermissionServer;

    @Cacheable(value={"USER_PERMISSION"}, key="'sysPermission_list_appCode-' + #appCode + 'userId-' + #id")
    public List<SysPermission> listByAppCodeAndUserId(String appCode, String id)
    {
        return this.sysPermissionServer.listByAppCodeAndUserId(appCode, id);
    }
}
