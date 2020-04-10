package com.chengshare.contentcenter.configuration;

import feign.Logger;
import org.springframework.context.annotation.Bean;


/**
 *
 * feign自定义配置 - 日志级别
 * @Author fate7
 * @Date 2020/4/10 2:35 下午
 **/

//@Configuration 父子上下文重复扫描，被用于全局feign配置
public class UserCenterFeignConfiguration {

    @Bean
    public Logger.Level level() {
        return Logger.Level.FULL;
    }
}
