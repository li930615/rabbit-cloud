package com.rabbit.gateway.componet.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

/**
 * @ClassName RegistrySwaggerResourcesProvider
 * @Description Swagger注册所有微服务模块资源
 * @Author LZQ
 * @Date 2019/2/4 14:58
 **/

@Component
@Primary
public class RegistrySwaggerResourcesProvider implements SwaggerResourcesProvider {

    private final RouteLocator routeLocator;

    /*构造器注入*/
    public RegistrySwaggerResourcesProvider(RouteLocator routeLocator) {
        this.routeLocator = routeLocator;
    }

    /*将Swagger的相关请求加入到网关白名单中*/
    public List<SwaggerResource> get() {
        ArrayList<SwaggerResource> resources = new ArrayList<>();
        List<Route> routes = this.routeLocator.getRoutes();
        routes.forEach(route -> resources.add(this.swaggerResource(route.getId(), route.getFullPath().replace("**", "v2/api-docs"))));
        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion("2.0");
        return swaggerResource;
    }
}
