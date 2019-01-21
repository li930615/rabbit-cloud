package com.rabbit.gateway.service;

import com.rabbit.feign.auth.model.R;
import com.rabbit.feign.auth.server.AuthServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @ClassName AuthService
 * @Description TODO
 * @Author LZQ
 * @Date 2019/1/20 16:39
 **/
@Service("authService")
public class AuthService {

    @Autowired
    private AuthService authService;
    @Autowired
    private AuthServer authServer;

    public R ssoVerify(String token) {
        R r = this.authService.ssoVerifyByServer(token);
        if (!r.success()) {
            this.authService.delSsoVerifyCache(token);
        }
        return r;
    }

    @Cacheable(value = {"SSO_TOKEN_VERIFY"}, key = "'authorization-' + #token")
    public R ssoVerifyByServer(String token) {
        return this.authServer.ssoVerify(token);
    }

    @CacheEvict(value = {"SSO_TOKEN_VERIFY"}, key = "'authorization-' + #token")
    public void delSsoVerifyCache(String token) {
    }
}
