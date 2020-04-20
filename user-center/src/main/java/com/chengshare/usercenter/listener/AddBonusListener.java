package com.chengshare.usercenter.listener;

import com.alibaba.fastjson.JSON;
import com.chengshare.usercenter.dao.BonusEventLog.BonusEventLogMapper;
import com.chengshare.usercenter.dao.user.UserMapper;
import com.chengshare.usercenter.domain.dto.message.UserAddBonusMsgDTO;
import com.chengshare.usercenter.domain.entity.BonusEventLog.BonusEventLog;
import com.chengshare.usercenter.domain.entity.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Author fate7
 * @Date 2020/4/13 4:06 下午
 **/
@Component
@RabbitListener(queuesToDeclare = @Queue("add-bouns"))
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AddBonusListener{

    private final UserMapper userMapper;

    private final BonusEventLogMapper bonusEventLogMapper;

    @RabbitHandler
    @Transactional(rollbackFor = Exception.class)
    public void process(String msg) {
        log.info("【接收到消息】=> {}" , msg);
        //当收到消息的时候执行的业务
        UserAddBonusMsgDTO userAddBonusMsgDTO = JSON.parseObject(msg, UserAddBonusMsgDTO.class);
        //1. 为用户加积分
        User user = this.userMapper.selectByPrimaryKey(userAddBonusMsgDTO.getUserId());
        if (user == null) {
            throw new IllegalArgumentException("此用户不存在");
        }
        Integer bonus = user.getBonus() + userAddBonusMsgDTO.getBonus();
        user.setBonus(bonus);
        this.userMapper.updateByPrimaryKey(user);
        //2. 记录日志到bonus_event_log表中
        this.bonusEventLogMapper.insert(
                BonusEventLog.builder()
                .userId(userAddBonusMsgDTO.getUserId())
                .value(userAddBonusMsgDTO.getBonus())
                .event(userAddBonusMsgDTO.getEvent())
                .createTime(new Date())
                .description(userAddBonusMsgDTO.getDescription())
                .build());
    }
}
