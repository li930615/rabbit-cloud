package com.rabbit.gateway.componet.handler;

import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.repository.DefaultRateLimiterErrorHandler;
import com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.config.repository.RateLimiterErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @ClassName ZuulRateLimiterErrorHandler
 * @Description TODO
 * @Author LZQ
 * @Date 2019/1/20 20:57
 **/
@Component
/*限流错误处理器*/
public class ZuulRateLimiterErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(ZuulRateLimiterErrorHandler.class);

    @Bean
    public RateLimiterErrorHandler rateLimitErrorHandler() {
        return new DefaultRateLimiterErrorHandler() {
            public void handleSaveError(String key, Exception e) {
                ZuulRateLimiterErrorHandler.log.error("保存key:[{}]异常", key, e);
            }

            public void handleFetchError(String key, Exception e) {
                ZuulRateLimiterErrorHandler.log.error("路由失败:[{}]异常", key);
            }

            public void handleError(String msg, Exception e) {
                ZuulRateLimiterErrorHandler.log.error("限流异常:[{}]", msg, e);
            }
        };
    }
}
