package com.rabbit.gateway.componet.config;

import com.rabbit.gateway.componet.locator.DynamicRouteLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName ZuulConfig
 * @Description TODO
 * @Author LZQ
 * @Date 2019/1/20 19:58
 **/
@Configuration
public class ZuulConfig {

    @Autowired
    private Registration registration;

    @Autowired
    private DiscoveryClient discovery;

    @Autowired
    ZuulProperties zuulProperties;

    @Autowired
    ServerProperties server;

    @Value("${rabbit.gateway.routeNum}")
    /*设置服务扩展数量，生产环境无需指定*/
    public Integer routeNum;

    @Value("${rabbit.gateway.permissionIntercept}")
    /*开启权限拦截*/
    public boolean permissionIntercept;
    @Bean
    /*配置动态路由*/
    public DynamicRouteLocator dynamicRouteLocator()
    {
        return new DynamicRouteLocator(this.server.getServletPrefix(), this.discovery, this.zuulProperties, this.registration, this.routeNum);
    }
}
