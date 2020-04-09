package ribbonconfiguration;

import com.chengshare.contentcenter.configuration.NacosFinalRule;
import com.chengshare.contentcenter.configuration.NacosSameClusterWeightedRule;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.PingUrl;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 这个configuration一定要放在启动类之外的包中，默认没有Zone等同轮询算法
 * spring中的父子上下文，spring启动类会扫描启动类所在包下的所有component
 * 这里属于父上下文，Ribbon的属于子上下文,为实现细粒度控制，为指定服务指定负载均衡策略，如果被父上下文扫描到，会变成全局的负载均衡策略
 * springmvc整合时分别配置不扫描controller和只扫描controller,否则会导致事务失效
 * @Author fate7
 * @Date 2020/4/9 4:11 下午
 **/


@Configuration
public class RibbonConfiguration {

    @Bean
    public IRule ribbonRule() {
        return new NacosFinalRule();
    }

    @Bean
    public IPing ping() {
        return new PingUrl();
    }
}
