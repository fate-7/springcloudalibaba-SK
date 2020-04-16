package com.chengshare.gateway;

import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * 自定义谓词路由工厂自定义规则：朝九晚五
 * @Author fate7
 * @Date 2020/4/16 10:12 上午
 **/
@Component
public class TimeBetweenRoutePredicateFactory extends AbstractRoutePredicateFactory<TimeBetweenConfig> {

    public TimeBetweenRoutePredicateFactory() {
        super(TimeBetweenConfig.class);
    }

    /**
     * 谓词路由工厂的核心方法
     * @param config
     * @return
     */
    @Override
    public Predicate<ServerWebExchange> apply(TimeBetweenConfig config) {
        LocalTime start = config.getStart();
        LocalTime end = config.getEnd();
        return exchange -> {
            LocalTime now = LocalTime.now();
            return now.isAfter(start) && now.isBefore(end);
        };
    }

    /**
     * 控制配置类和配置文件的映射关系
     * @return
     */
    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("start", "end");
    }
}
