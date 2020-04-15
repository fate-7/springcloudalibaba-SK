package com.chengshare.contentcenter;

import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import com.chengshare.contentcenter.sentineltest.TestControllerRestTempleSentiel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.chengshare.contentcenter.dao")
//全局配置
//@EnableFeignClients(defaultConfiguration = GlobalFeignConfiguration.class)
@EnableFeignClients
@EnableBinding({Source.class})
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
