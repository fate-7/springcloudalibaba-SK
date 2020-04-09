package com.chengshare.contentcenter.configuration;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Configuration;
import ribbonconfiguration.RibbonConfiguration;

/**
 * java配置类配置Ribbon的规则
 * javaConfig：更加灵活，spring上下文的缺点，需要重新打包发布
 * 配置文件：易上手，配置直观，无需重新打包，发布优先级更高
 *
 * 总结：
 * 尽量使用属性配置
 * 微服务尽量保持单一方式
 * @Author fate7
 * @Date 2020/4/9 4:08 下午
 **/

//@Configuration
//@RibbonClient(name = "user-center", configuration = RibbonConfiguration.class)
//全局配置
@RibbonClients(defaultConfiguration = NacosWeightedRule.class)
public class UserCenterRibbonConfiguration {
}
