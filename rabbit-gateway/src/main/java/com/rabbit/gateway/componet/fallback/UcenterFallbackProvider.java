package com.rabbit.gateway.componet.fallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @ClassName UcenterFallbackProvider
 * @Description TODO
 * @Author LZQ
 * @Date 2019/1/20 20:32
 **/
@Component
/*自定义Zuul回退机制处理器*/
public class UcenterFallbackProvider implements FallbackProvider {
    private static final Logger logger = LoggerFactory.getLogger(UcenterFallbackProvider.class);
    private static final String AUTH_SERVICE_DISABLE = "用户中心模块不可用";

    /*自定义服务端响应*/
    /*网关向api服务请求是失败了，但是消费者客户端向网关发起的请求是OK的，不应该把api的404,500等问题抛给客户端
     *网关和api服务集群对于客户端来说是黑盒子*/
    public ClientHttpResponse fallbackResponse(final Throwable cause) {
        return new ClientHttpResponse() {
            public HttpStatus getStatusCode() {
                return HttpStatus.SERVICE_UNAVAILABLE;
            }

            public int getRawStatusCode() {
                return HttpStatus.SERVICE_UNAVAILABLE.value();
            }

            public String getStatusText() {
                return HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase();
            }

            public void close() {
            }

            /*如果用户请求失败，则返回"认证授权模块不可用"给用户（友好提示。因为微服务出现宕机后，客户端再请求时候就会返回 fallback 等字样的字符串提示）*/
            public InputStream getBody() {
                if ((cause != null) && (cause.getMessage() != null)) {
                    UcenterFallbackProvider.logger.error("调用:{} 异常：{}", UcenterFallbackProvider.this.getRoute(), cause.getMessage());
                    return new ByteArrayInputStream(cause.getMessage().getBytes());
                }
                UcenterFallbackProvider.logger.error("调用:{} 异常：{}", UcenterFallbackProvider.this.getRoute(), "用户中心模块不可用");
                return new ByteArrayInputStream("用户中心模块不可用".getBytes());
            }

            /*和body中的内容编码一致，否则容易乱码*/
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                return headers;
            }
        };
    }


    /*返回值表示需要针对此微服务做回退处理（即rabbit-auth-server），该名称一定要是注册进入 eureka 微服务中的那个 serviceId 名称*/
    public String getRoute() {
        return "rabbit-ucenter-server";
    }

    /*不需要将fallback返回给用户*/
    public ClientHttpResponse fallbackResponse() {
        return fallbackResponse(null);
    }
}
