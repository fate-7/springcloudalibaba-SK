package com.chengshare.usercenter.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Service;

/**
 * @Author fate7
 * @Date 2020/4/13 6:34 下午
 **/
@Service
@Slf4j
public class TestStreamConsumer {

    @StreamListener(Sink.INPUT)
    public void receive(String messageBody) {
        log.info("收到的消息={}", messageBody);
    }

}
