package com.chengshare.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractNameValueGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

/**
 * SpringCloud Gateway自定义filter
 * filter的生命周期
 * pre: gateway请求转发之前
 * post: gateway请求转发之后
 *
 * 实现的两种方式
 * 1. AbstractGatewayFilterFactory
 * 2. AbstractNameValueGatewayFilterFactory
 *
 * 核心api
 * exchange.getRequest().mutate().xxx
 * exchange.mutate().request
 * chain.filter
 *
 * 注意点：名字一定是GatewayFilterFactory结尾
 * 配置写前缀： - PreLog=a, b a是name， b是value
 *
 * 过滤器执行顺序：order越小越先执行
 * 局部过滤器从第一个配置order从1开始递增
 * 局部过滤器自定义order -new OrderedGatewayFilter
 * @Author fate7
 * @Date 2020/4/16 11:37 上午
 **/
@Slf4j
@Component
public class PreLogGatewayFilterFactory extends AbstractNameValueGatewayFilterFactory {
    @Override
    public GatewayFilter apply(NameValueConfig config) {
        GatewayFilter filter = ((exchange, chain) -> {
            log.info("请求到达={}:{}", config.getName(), config.getValue());
            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate().build();
            ServerWebExchange modifitedExchange = exchange.mutate().request(modifiedRequest).build();
            return chain.filter(modifitedExchange);
        });
        return new OrderedGatewayFilter(filter, 10000);
    }
}
