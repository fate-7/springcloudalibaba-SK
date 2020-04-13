package com.chengshare.contentcenter.mq;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * 自定义接口发送消息
 * @Author fate7
 * @Date 2020/4/13 6:45 下午
 **/
public interface MySource {

    String MY_OUPUT = "my-ouptut";

    @Output(MY_OUPUT)
    MessageChannel output();
}
