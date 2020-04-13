package com.chengshare.usercenter.mq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @Author fate7
 * @Date 2020/4/13 6:56 下午
 **/
public interface MySink {

    String MY_INPUT = "my-input";

    @Input(MY_INPUT)
    public SubscribableChannel input();
}
