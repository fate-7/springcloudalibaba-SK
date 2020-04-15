package com.chengshare.contentcenter.sentineltest;

import lombok.extern.slf4j.Slf4j;

/**
 * fallback方法一定要static
 * @Author fate7
 * @Date 2020/4/11 10:16 下午
 **/
@Slf4j
public class TestControllerFallbackHandlerClass {

    /**
     * 处理降级
     *  - sentinel1.6处理throwable?
     * @param a
     * @return
     */
    public static String fallback(String a){
        log.warn("降级 fallback");
        return "降级 fallback";
    }
}
