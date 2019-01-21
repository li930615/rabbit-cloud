package com.rabbit.common.bean.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FilterUrlsPropertiesConfig
 * @Description TODO
 * @Author LZQ
 * @Date 2019/1/19 18:16
 **/
@Configuration
@ConditionalOnExpression("!'${urls}'.isEmpty()") //@ConditionalOnExpression注解，在特定情况下初始化bean(urls为空时初始化)
@ConfigurationProperties(prefix="urls")//加上该注解后,就会注入在application.yml中server开头的属性
public class FilterUrlsPropertiesConfig {

    private List<String> anon = new ArrayList<>();

    public List<String> getAnon() {
        return anon;
    }

    public void setAnon(List<String> anon) {
        this.anon = anon;
    }
}
