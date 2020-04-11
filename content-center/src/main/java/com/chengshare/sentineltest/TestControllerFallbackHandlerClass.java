package com.chengshare.sentineltest;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author fate7
 * @Date 2020/4/11 10:16 下午
 **/
@Slf4j
public class TestControllerFallbackHandlerClass {

    /**
     * 处理降级
     *  - sentinel1.6处理throwable?
     * @param a
     * @param e
     * @return
     */
    public String fallback(String a){
        log.warn("降级 fallback");
        return "降级 fallback";
    }
}
