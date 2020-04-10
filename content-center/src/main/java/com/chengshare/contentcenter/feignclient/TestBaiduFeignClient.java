package com.chengshare.contentcenter.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author fate7
 * @Date 2020/4/10 4:23 下午
 **/
@FeignClient(name = "xxx", url = "http://www.baidu.com")
public interface TestBaiduFeignClient {

    @GetMapping("")
    public String index();
}
