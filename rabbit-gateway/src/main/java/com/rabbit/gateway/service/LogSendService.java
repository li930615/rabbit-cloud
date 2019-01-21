package com.rabbit.gateway.service;

import com.netflix.zuul.context.RequestContext;

/**
 * @ClassName LogSendService
 * @Description TODO
 * @Author LZQ
 * @Date 2019/1/20 20:53
 **/
public interface LogSendService {

    void send(RequestContext paramRequestContext);
}
