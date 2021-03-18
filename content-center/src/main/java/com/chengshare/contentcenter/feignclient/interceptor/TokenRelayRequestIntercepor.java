package com.chengshare.contentcenter.feignclient.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author fate7
 * @Date 2020/4/19 2:57 下午
 **/
public class TokenRelayRequestIntercepor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        //1. 获取到Token
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = attributes.getRequest();

        String token = request.getHeader("X-Token");

        //2. 将token传递
        if (StringUtils.isNotBlank(token)) {
            requestTemplate.header("X-Token", token);
        }
    }
}
