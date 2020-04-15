package com.chengshare.contentcenter.sentineltest;

import com.alibaba.csp.sentinel.adapter.servlet.callback.RequestOriginParser;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 规则区分来源
 * @Author fate7
 * @Date 2020/4/12 8:08 下午
 **/
//@Component
public class MyRequestOriginParser implements RequestOriginParser {
    @Override
    public String parseOrigin(HttpServletRequest httpServletRequest) {
        //从请求参数中获取名为origin的参数，这个返回值就是来源
        //如果获取不到origin参数，那么就抛异常
        //实际项目中建议把来源放入到header中，从header中读取，是url更美观
        String origin = httpServletRequest.getParameter("origin");
        if (StringUtils.isBlank(origin)){
            throw new IllegalArgumentException("origin must be specified");
        }
        return origin;
    }
}
