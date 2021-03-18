package com.chengshare.contentcenter.service.content;

import com.alibaba.fastjson.JSONObject;
import com.chengshare.contentcenter.dao.content.MidUserShareMapper;
import com.chengshare.contentcenter.dao.content.ShareMapper;
import com.chengshare.contentcenter.domain.dto.content.ShareAudioDTO;
import com.chengshare.contentcenter.domain.dto.content.ShareDTO;
import com.chengshare.contentcenter.domain.dto.content.ShareRequestDTO;
import com.chengshare.contentcenter.domain.dto.message.UserAddBonusMsgDTO;
import com.chengshare.contentcenter.domain.dto.user.UserAddBonseDTO;
import com.chengshare.contentcenter.domain.dto.user.UserDTO;
import com.chengshare.contentcenter.domain.entity.content.MidUserShare;
import com.chengshare.contentcenter.domain.entity.content.Share;
import com.chengshare.contentcenter.feignclient.UserCenterFeignClient;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * @Author fate7
 * @Date 2020/4/7 9:21 下午
 **/

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class ShareService {

    private final ShareMapper shareMapper;

    private final MidUserShareMapper midUserShareMapper;

    private final UserCenterFeignClient userCenterFeignClient;

    private final AmqpTemplate amqpTemplate;

    public ShareDTO findbyId(Integer id) {
        //获取分享详情
        Share share = shareMapper.selectByPrimaryKey(id);
        //发布人id
        Integer userId = share.getUserId();

        //调用用户微服务的/users/{userId}
        UserDTO userDTO = this.userCenterFeignClient.findById(userId);
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
                        UserAddBonusMsgDTO.builder()
                                .userId(share.getUserId())
                                .bonus(50)
                                .description("发布文章")
                                .event("CONTENRIBUTE")
                                .build()
                ));
        return share;

    }

    public PageInfo<Share> q(String title, Integer pageNo, Integer pageSize, Integer userId) {
        //用户未登录 downloadUrl全部设置未空

        //如果用户登录但是在mid_user_share表中不存在，置为downloadUrl为空
        PageHelper.startPage(pageNo, pageSize);
        List<Share> shares = this.shareMapper.selectByParam(title);

        List<Share> shareDealed;
        if (userId == null) {
            shareDealed = shares.stream().peek(share -> {
                share.setDownloadUrl(null);
            }).collect(Collectors.toList());
        } else {
            shareDealed = shares.stream().peek(share -> {
                MidUserShare midUserShare = this.midUserShareMapper.selectOne(
                        MidUserShare.builder()
                                .shareId(share.getId())
                                .userId(userId)
                                .build()
                );
                if (midUserShare == null) {
                    share.setDownloadUrl(null);
                }
            }).collect(Collectors.toList());
        }
        return new PageInfo<>(shareDealed);

    }

    @Transactional(rollbackFor = Exception.class)
    public Share exchangeById(Integer id, HttpServletRequest request) {
        Share share = this.shareMapper.selectByPrimaryKey(id);
        if (share == null) {
            throw new IllegalArgumentException("该分享不存在");
        }

        Integer userId = (Integer) request.getAttribute("id");
        Integer price = share.getPrice();


        MidUserShare midUserShare = this.midUserShareMapper.selectOne(
                MidUserShare.builder()
                        .userId(userId)
                        .shareId(id)
                        .build()
        );
        //如果用户已兑换过直接返回
        if (midUserShare != null) {
            return share;
        }

        UserDTO userDTO = this.userCenterFeignClient.findById(userId);
        if (userDTO.getBonus() < price) {
            throw new IllegalArgumentException("用户积分不够");
        }
        share.setBuyCount(share.getBuyCount() + 1);
        this.shareMapper.updateByPrimaryKey(share);
        this.userCenterFeignClient.addBonus(
                UserAddBonseDTO.builder().userId(userId).bonus(0 - price).build()
        );
        this.midUserShareMapper.insert(
                MidUserShare.builder()
                        .userId(userId)
                        .shareId(id)
                        .build()
        );

        return share;
    }

    public Share createContribute(ShareRequestDTO shareRequestDTO, HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("id");
        String auditStatus = "NOT_YET";
        Boolean showFlag = false;

        Share share = new Share();

        BeanUtils.copyProperties(shareRequestDTO, share);
        share.setBuyCount(0);
        share.setUserId(userId);
        share.setAuditStatus(auditStatus);
        share.setShowFlag(showFlag);
        share.setCreateTime(new Date());
        share.setUpdateTime(new Date());
        this.shareMapper.insertSelective(share);
        return this.shareMapper.selectOne(share);
    }

    public Share updateContribute(Integer id, ShareRequestDTO shareRequestDTO, HttpServletRequest request) {
        Share share = this.shareMapper.selectByPrimaryKey(id);
        if (share == null) {
            throw new IllegalArgumentException("该分享不存在");
        }

        Integer userId = (Integer) request.getAttribute("id");
        String auditStatus = "NOT_YET";
        Boolean showFlag = false;

        BeanUtils.copyProperties(shareRequestDTO, share);
        share.setAuditStatus(auditStatus);
        share.setShowFlag(showFlag);
        share.setUpdateTime(new Date());
        this.shareMapper.updateByPrimaryKeySelective(share);
        return share;
    }

    public List<Share> myShares(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("id");
        List<MidUserShare> select = this.midUserShareMapper.select(MidUserShare.builder().userId(userId).build());
        List<Integer> shareIds = select.stream().map(midUserShare -> midUserShare.getShareId()).collect(Collectors.toList());
        List<Share> shares = shareIds.stream().map(shareId -> {
            return this.shareMapper.selectByPrimaryKey(shareId);
        }).collect(Collectors.toList());
        return shares;
    }

    public List<Share> myContributions(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("id");
        return this.shareMapper.select(Share.builder().userId(userId).build());
    }

    public Share preview(Integer id) {
        return this.shareMapper.selectByPrimaryKey(id);
    }
}

