package com.chengshare.contentcenter.feignclient.fallback;

import com.chengshare.contentcenter.domain.dto.user.UserAddBonseDTO;
import com.chengshare.contentcenter.domain.dto.user.UserDTO;
import com.chengshare.contentcenter.feignclient.UserCenterFeignClient;
import org.springframework.stereotype.Component;

/**
 * @Author fate7
 * @Date 2020/4/11 11:26 下午
 **/
@Component
public class UserCenterFeignClientFallback  implements UserCenterFeignClient {

    @Override
    public UserDTO findById(Integer id) {
        UserDTO userDTO = new UserDTO();
        userDTO.setWxNickname("一个新用户");
        return userDTO;
    }

    @Override
    public UserDTO addBonus(UserAddBonseDTO userAddBonseDTO) {
        return null;
    }
}
