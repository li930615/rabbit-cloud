package com.rabbit.common.bean.resolver;

import com.google.gson.Gson;
import com.rabbit.common.entity.CurrentUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName CurrentUserArgumentResolver
 * @Description SpringMVC的HandlerMethodArgumentResolver解析器；其功能就是解析request请求参数并绑定数据到Controller的入参上。
 * @Author LZQ
 * @Date 2019/1/19 18:48
 **/
public class CurrentUserArgumentResolver implements HandlerMethodArgumentResolver {

    private static final Logger log = LoggerFactory.getLogger(CurrentUserArgumentResolver.class);

    @Override
    /*解析器是否支持当前参数*/
    public boolean supportsParameter(MethodParameter parameter) {
        /*指定参数如果被应用MyParam注解，则使用该解析器。如果直接返回true，则代表该解析器用于所有参数*/
        return parameter.getParameterType().equals(CurrentUser.class);
    }

    @Override
    /*将request中的请求参数解析到当前Controller参数上
      @param parameter 需要被解析的Controller参数，此参数必须首先传给supportsParameter并返回true
      @param mavContainer 当前request的ModelAndViewContainer
      @param webRequest 当前request
      @param binderFactory 生成 WebDataBinder实例的工厂
      @return 解析后的Controller参数*/
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        //获取当前用户信息（什么时候放进去的？）
        String currentUserStr = request.getHeader("current_user");
        if (StringUtils.isBlank(currentUserStr)) {
            log.warn("resolveArgument error current_user is empty");
            return null;
        }
        CurrentUser currentUser = new Gson().fromJson(currentUserStr, CurrentUser.class);
        return currentUser;
    }
}
