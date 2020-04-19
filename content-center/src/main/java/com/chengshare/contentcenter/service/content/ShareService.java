package com.chengshare.contentcenter.service.content;

import com.alibaba.fastjson.JSONObject;
import com.chengshare.contentcenter.dao.content.ShareMapper;
import com.chengshare.contentcenter.domain.dto.content.ShareAudioDTO;
import com.chengshare.contentcenter.domain.dto.content.ShareDTO;
import com.chengshare.contentcenter.domain.dto.enums.AuditStatusEnum;
import com.chengshare.contentcenter.domain.dto.message.UserAddBonusMsgDTO;
import com.chengshare.contentcenter.domain.dto.user.UserDTO;
import com.chengshare.contentcenter.domain.entity.content.Share;
import com.chengshare.contentcenter.feignclient.UserCenterFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;


/**
 * @Author fate7
 * @Date 2020/4/7 9:21 下午
 **/

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class ShareService {

    private final ShareMapper shareMapper;

    private final UserCenterFeignClient userCenterFeignClient;

    private final AmqpTemplate amqpTemplate;

    public ShareDTO findbyId(Integer id, String token) {
        //获取分享详情
        Share share = shareMapper.selectByPrimaryKey(id);
        //发布人id
        Integer userId = share.getUserId();

        //调用用户微服务的/users/{userId}
        UserDTO userDTO = this.userCenterFeignClient.findById(userId, token);
        //消息装配
        ShareDTO shareDTO = new ShareDTO();
        BeanUtils.copyProperties(share, shareDTO);
        shareDTO.setWxNickname(userDTO.getWxNickname());

        return shareDTO;

    }

    @Transactional(rollbackFor = Exception.class)
    public Share auditById(Integer id, ShareAudioDTO audioDTO) {
        //查询share是否存在，不存在或者当前的audit——status不为未审核
        Share share = this.shareMapper.selectByPrimaryKey(id);
        if (share == null) {
            throw new IllegalArgumentException("参数非法，该分享不存在");
        }
        if (!Objects.equals("NOT_YET", share.getAuditStatus())) {
            throw new IllegalArgumentException("参数非法，该分享已审核通过或拒绝发布");
        }
        //审核资源，将状态设为PASS/REJECT

        share.setAuditStatus(audioDTO.getAuditStatusEnum().toString());
        share.setReason(audioDTO.getReason());
        this.shareMapper.updateByPrimaryKey(share);

        //如果是PASS，那么为发布人添加积分 -> 异步执行MQ
        /**
         * MQ的适用场景
         * 1. 异步处理
         * 2. 流量削峰填谷
         * 3. 解耦微服务
         *
         */

        this.amqpTemplate.convertAndSend("add-bouns",
                JSONObject.toJSONString(
                        UserAddBonusMsgDTO.builder().userId(share.getUserId()).bonus(50).build()
                ));
        return share;

    }
}
