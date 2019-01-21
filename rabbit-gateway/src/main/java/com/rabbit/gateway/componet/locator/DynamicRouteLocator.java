package com.rabbit.gateway.componet.locator;

import cn.hutool.core.bean.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.discovery.DiscoveryClientRouteLocator;

import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * @ClassName DynamicRouteLocator
 * @Description 动态路由:zuul是Netflix设计用来为所有面向设备、web网站提供服务的所有应用的门面，
 * zuul可以提供动态路由、监控、弹性扩展、安全认证等服务，它还可以根据需求将请求路由到多个应用中。
 * @Author LZQ
 * @Date 2019/1/20 20:00
 **/
/*在目前的应用中，zuul主要用来做如下几件事情：
动态路由：APP、web网站通过zuul来访问不同的服务提供方，且与ribbon结合，还可以路由到同一个应用不同的实例中。
安全认证：zuul作为互联网服务架构中的网关，可以用来校验非法访问、授予token、校验token等。
限流：zuul通过记录每种请求的类型来达到限制访问过多导致服务down掉的目的。
静态响应处理：直接在zuul就处理一些请求，返回响应内容，不转发到微服务内部。
区域弹性：主要是针对AWS上的应用做一些弹性扩展。*/

/*注意：zuul只有结合了eureka，才会有ribbon作为软件负载均衡，直接配置逻辑URL，不会起到负载均衡的效果，也不会有hystrix作为熔断器使用*/
public class DynamicRouteLocator extends DiscoveryClientRouteLocator {

    private static final Logger logger = LoggerFactory.getLogger(DynamicRouteLocator.class);

    private ZuulProperties zuulProperties;

    private int routeNum;

    public DynamicRouteLocator(String servletPath, DiscoveryClient discovery, ZuulProperties properties, ServiceInstance localServiceInstance, Integer routeNum) {
        super(servletPath, discovery, properties, localServiceInstance);
        this.zuulProperties = properties;
        this.routeNum = routeNum;
    }

    protected LinkedHashMap<String, ZuulProperties.ZuulRoute> locateRoutes() {
        LinkedHashMap routesMap = new LinkedHashMap();
        //从配置文件中获取路由地址迭代器
        Iterator var2 = this.zuulProperties.getRoutes().values().iterator();

        routesMap.putAll(super.locateRoutes());
        logger.debug("初始默认的路由配置完成");
        while (var2.hasNext()) {
            ZuulProperties.ZuulRoute route = (ZuulProperties.ZuulRoute) var2.next();
            String routePath = route.getPath();
            for (int i = 1; i <= this.routeNum; i++) {
                ZuulProperties.ZuulRoute newZuulRoute = new ZuulProperties.ZuulRoute();
                BeanUtil.copyProperties(route, newZuulRoute);
                //路由转换规则
                newZuulRoute.setPath(routePath.substring(0, routePath.length() - 3) + "-" + i + "/**");
                newZuulRoute.setId(route.getId() + "-" + i);
                newZuulRoute.setServiceId(route.getServiceId() + "-" + i);
                routesMap.put(newZuulRoute.getPath(), newZuulRoute);
            }
        }
        return routesMap;
    }
}
