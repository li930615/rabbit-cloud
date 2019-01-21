package com.rabbit.gateway.service.impl;

import com.netflix.zuul.context.RequestContext;
import com.rabbit.gateway.service.LogSendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName LogSendServiceImpl
 * @Description TODO
 * @Author LZQ
 * @Date 2019/1/20 20:55
 **/
@Component
public class LogSendServiceImpl implements LogSendService {

    private static final String SERVICE_ID = "serviceId";
    private Logger logger = LoggerFactory.getLogger(LogSendServiceImpl.class);

    @Override
    public void send(RequestContext paramRequestContext) {
        HttpServletRequest request = paramRequestContext.getRequest();
        String requestUri = request.getRequestURI();
        String method = request.getMethod();
    }
}
