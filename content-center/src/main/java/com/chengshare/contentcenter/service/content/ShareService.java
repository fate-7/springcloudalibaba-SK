package com.chengshare.contentcenter.service.content;

import com.chengshare.contentcenter.dao.content.ShareMapper;
import com.chengshare.contentcenter.domain.dto.content.ShareDTO;
import com.chengshare.contentcenter.domain.dto.user.UserDTO;
import com.chengshare.contentcenter.domain.entity.content.Share;
import com.chengshare.contentcenter.feignclient.UserCenterFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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

}
