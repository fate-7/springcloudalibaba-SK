package com.chengshare.contentcenter.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Sentinel规则中的流控模式 链路模式 将限制指定链路的流量 细粒度的api访问限制
 * @Author fate7
 * @Date 2020/4/11 5:48 下午
 **/

@Slf4j
@Service
public class TestService {

    @SentinelResource("common")
    public String common() {
        log.info("common..");
        return "common";
    }
}
