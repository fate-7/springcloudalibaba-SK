package com.chengshare.contentcenter;

import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import com.chengshare.sentineltest.TestControllerBlockHandlerClass;
import com.chengshare.sentineltest.TestControllerFallbackHandlerClass;
import com.chengshare.sentineltest.TestControllerRestTempleSentiel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.chengshare")
//全局配置
//@EnableFeignClients(defaultConfiguration = GlobalFeignConfiguration.class)
@EnableFeignClients
public class ContentCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContentCenterApplication.class, args);
    }

    @Bean
    @LoadBalanced
    //restTemple整合Sentinel
    @SentinelRestTemplate(
            blockHandler = "block",
            blockHandlerClass = TestControllerRestTempleSentiel.class,
            fallback = "fallback",
            fallbackClass = TestControllerRestTempleSentiel.class
            )
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
