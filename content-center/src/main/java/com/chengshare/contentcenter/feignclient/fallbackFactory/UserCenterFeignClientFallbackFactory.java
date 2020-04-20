package com.chengshare.contentcenter.feignclient.fallbackFactory;

import com.chengshare.contentcenter.domain.dto.user.UserAddBonseDTO;
import com.chengshare.contentcenter.domain.dto.user.UserDTO;
import com.chengshare.contentcenter.feignclient.UserCenterFeignClient;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author fate7
 * @Date 2020/4/11 11:36 下午
 **/
@Slf4j
@Component
public class UserCenterFeignClientFallbackFactory implements FallbackFactory<UserCenterFeignClient> {
    @Override
    public UserCenterFeignClient create(Throwable throwable) {

        return new UserCenterFeignClient() {
            @Override
            public UserDTO findById(Integer id) {
                log.warn("远程调用被限流/降级了", throwable);
                UserDTO userDTO = new UserDTO();
                userDTO.setWxNickname("一个新用户");
                return userDTO;
            }

            @Override
            public UserDTO addBonus(UserAddBonseDTO userAddBonseDTO) {
                log.warn("远程调用被限流/降级了", throwable);
                return null;
            }
        };
    }
}
