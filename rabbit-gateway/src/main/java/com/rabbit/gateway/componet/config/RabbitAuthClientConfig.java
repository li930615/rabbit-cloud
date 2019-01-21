package com.rabbit.gateway.componet.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName RabbitAuthConfig
 * @Description TODO
 * @Author LZQ
 * @Date 2019/1/20 19:56
 **/
@Configuration
public class RabbitAuthClientConfig {

    @Value("${rabbit.auth.server}")
    public String server;

    @Value("${rabbit.auth.clientId}")
    public String clientId;

    @Value("${rabbit.auth.clientSecret}")
    public String clientSecret;
}
