package com.chengshare.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.PRESERVE_HOST_HEADER_ATTRIBUTE;

/**
 * 传递host
 *
 * @author chengzhiqi
 * @date 2021/1/31 1:58 下午
 **/
@Slf4j
@Component
public class HostHeaderGatewayFilterFactory extends AbstractGatewayFilterFactory<HostHeaderGatewayFilterFactory.Config> {


    @Override
    public List<String> shortcutFieldOrder() {
        return Collections.singletonList("enabled");
    }

    @Autowired
    public HostHeaderGatewayFilterFactory(){
        super(Config.class);
    }


    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (!config.isEnabled()) {
                return chain.filter(exchange);
            }
            exchange.getAttributes().put(PRESERVE_HOST_HEADER_ATTRIBUTE, true);
            return chain.filter(exchange);
        };
    }

    public static class Config {
        private boolean enabled;

        public Config() {
        }

        boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

}
