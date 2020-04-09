package com.chengshare.contentcenter.configuration;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.core.Balancer;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 同集群内优先加载
 * @Author fate7
 * @Date 2020/4/9 6:20 下午
 **/
@Slf4j
public class NacosSameClusterWeightedRule extends AbstractLoadBalancerRule {


    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }

    @Override
    public Server choose(Object key) {

        try{

            String clusterName = nacosDiscoveryProperties.getClusterName();

            BaseLoadBalancer loadBalancer = (BaseLoadBalancer) this.getLoadBalancer();

            //想要请求的LoadBalancer名称
            String name = loadBalancer.getName();
            //实现负载均衡算法
            //拿到服务发现的api
            NamingService namingService = nacosDiscoveryProperties.namingServiceInstance();

            //1。找出指定服务的所有实例 A
            List<Instance> instances = namingService.selectInstances(name, true);
            //2。过滤相同集群下的所有实例 B
            List<Instance> sameClusterInsatnces = instances.stream()
                    .filter(instance -> Objects.equals(instance.getClusterName(), clusterName)).collect(Collectors.toList());
            //3。如果B是空，就用A
            List<Instance> instancesToBeChosen = new ArrayList<>();
            if (CollectionUtils.isEmpty(sameClusterInsatnces)) {
                instancesToBeChosen = instances;
                log.warn("发生跨集群调用, name = {}, clusterName = {}, instances = {}",
                        name,
                        clusterName,
                        instances
                );
            } else {
                instancesToBeChosen = sameClusterInsatnces;
            }
            //4。基于权重的负载均衡算法 nacos client跟踪源码实现
            Instance instance = ExtendBalancer.getHostByRandomWeightR(instancesToBeChosen);
            log.info("选择的实例 port={}, instance = {}", instance.getPort(), instance);

            return new NacosServer(instance);
        } catch (NacosException e) {
            log.error("发生异常", e);
            return null;
        }
    }
}

class ExtendBalancer extends Balancer {
    public static Instance getHostByRandomWeightR(List<Instance> hosts) {
        return getHostByRandomWeight(hosts);
    }
}
