package com.chengshare.contentcenter.sentinel;

import com.alibaba.csp.sentinel.adapter.servlet.callback.UrlCleaner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 支持Restful风格的url
 *
 * @Author fate7
 * @Date 2020/4/12 8:12 下午
 **/
@Slf4j
@Component
public class MyUrlCleaner implements UrlCleaner {
    @Override
    public String clean(String s) {
        log.info("originUrl={}", s);
        // 让 /shares/1 与 /shares/2 的返回值相同
        // 返回/shares/{number}

        String[] split = s.split("/");

        return Arrays.stream(split)
                .map(string -> {
                    if (NumberUtils.isNumber(string)) {
                        return "{number}";
                    }
                    return string;
                })
                .reduce((a, b) -> a + "/" + b)
                .orElse("");
    }
}
