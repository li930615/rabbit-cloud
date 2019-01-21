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
public class UcenterFallbackProvider implements FallbackProvider {
    private static final Logger logger = LoggerFactory.getLogger(UcenterFallbackProvider.class);
    private static final String AUTH_SERVICE_DISABLE = "用户中心模块不可用";

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

            public InputStream getBody() {
                if ((cause != null) && (cause.getMessage() != null)) {
                    UcenterFallbackProvider.logger.error("调用:{} 异常：{}", UcenterFallbackProvider.this.getRoute(), cause.getMessage());
                    return new ByteArrayInputStream(cause.getMessage().getBytes());
                }
                UcenterFallbackProvider.logger.error("调用:{} 异常：{}", UcenterFallbackProvider.this.getRoute(), "用户中心模块不可用");
                return new ByteArrayInputStream("用户中心模块不可用".getBytes());
            }

            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                return headers;
            }
        };
    }

    public String getRoute() {
        return "rabbit-ucenter-server";
    }

    public ClientHttpResponse fallbackResponse() {
        return fallbackResponse(null);
    }
}
