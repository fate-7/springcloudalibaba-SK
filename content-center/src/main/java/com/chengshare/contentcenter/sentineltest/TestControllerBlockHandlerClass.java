package com.chengshare.contentcenter.sentineltest;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author fate7
 * @Date 2020/4/11 10:14 下午
 **/
@Slf4j
public class TestControllerBlockHandlerClass {

    /**
     * 处理限流或降级
     * @param a
     * @param e
     * @return
     */
    public static String block(String a, BlockException e){
        log.warn("限流或降级 block",e);
        return "限流或降级 block";
    }
}
