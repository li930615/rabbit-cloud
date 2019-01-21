package com.rabbit.common.bean.config;

import com.rabbit.common.bean.resolver.CurrentUserArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * @ClassName WebMvcConfig
 * @Description TODO
 * @Author LZQ
 * @Date 2019/1/19 18:45
 **/
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private CacheManager cacheManager;

    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers){
        argumentResolvers.add(new CurrentUserArgumentResolver());
    }
}
