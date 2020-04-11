package com.chengshare.sentineltest;

import com.alibaba.cloud.sentinel.rest.SentinelClientHttpResponse;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.web.client.RestTemplate;

/**
 * @Author fate7
 * @Date 2020/4/11 10:42 下午
 **/
@Slf4j
public class TestControllerRestTempleSentiel {

    public static SentinelClientHttpResponse fallback(HttpRequest request,
                                                      byte[] body, ClientHttpRequestExecution execution, BlockException e){
        log.warn("降级 fallback");
        return new SentinelClientHttpResponse("custom fallback info");
    }

    public static SentinelClientHttpResponse block(HttpRequest request,
                                                   byte[] body, ClientHttpRequestExecution execution, BlockException e){
        log.warn("限流或降级 block",e);
        return new SentinelClientHttpResponse("custom block info");
    }
}
