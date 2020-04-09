package com.chengshare.contentcenter.configuration;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 默认不支持nacos的权重，所以可以自定义一个支持nacos权重的Rule ，实现IRule或者继承AbstractLoadBalancerRule
 * @Author fate7
 * @Date 2020/4/9 6:04 下午
 **/
@Slf4j
public class NacosWeightedRule extends AbstractLoadBalancerRule {


    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {
        //读取配置文件，并初始化
    }

    @Override
    public Server choose(Object key) {
        try{
            BaseLoadBalancer loadBalancer = (BaseLoadBalancer) this.getLoadBalancer();
            //log.info("lb={}", loadBalancer);

            //想要请求的LoadBalancer名称
            String name = loadBalancer.getName();
            //实现负载均衡算法
            //拿到服务发现的api
            NamingService namingService = nacosDiscoveryProperties.namingServiceInstance();
            //nacos client自动通过基于权重的负载均衡算法，选择一个实例
            Instance instance = namingService.selectOneHealthyInstance(name);

            log.info("port = {} instance = {}", instance.getPort(),instance);
            return new NacosServer(instance);

        } catch (NacosException e) {
            e.printStackTrace();
        }
        return null;
    }
}

/*
spring cloud commons --> 定义标准
spring cloud loadbalancer --> 没有权重的概念
spring cloud alibaba --> 整合ribbon实现权重负载均衡
 */
