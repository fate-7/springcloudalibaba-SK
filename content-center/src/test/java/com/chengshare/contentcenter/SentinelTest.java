package com.chengshare.contentcenter;

import com.chengshare.contentcenter.domain.dto.user.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

/**
 * @Author fate7
 * @Date 2020/4/11 5:23 下午
 **/

@Slf4j
public class SentinelTest extends ContentCenterApplicationTests{


    /**
     * Sentinel规则中的流控模式 关联模式 将需求优先级别高的关联到优先级别低的接口中 优先级高的达到阈值，级别低的就会被限流
     */
    @Test
    public void sharetest() {
        RestTemplate restTemplate = new RestTemplate();
        for (int i = 0; i < 10000; i++) {
            restTemplate.getForObject("http://localhost:8010/actuator/sentinel", String.class);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 测试排队等待
     */
    @Test
    public void commontest() {
        RestTemplate restTemplate = new RestTemplate();
        for (int i = 0; i < 10000; i++) {
            String object = restTemplate.getForObject("http://localhost:8010/test-a", String.class);
            log.info("--{}", object);
        }

    }
}
